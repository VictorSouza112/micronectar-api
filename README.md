# Projeto - Micronectar API (Desafio Cidades ESGInteligentes)

A `micronectar-api` é uma API RESTful desenvolvida em Java com Spring Boot, servindo como backend para a plataforma Micronectar. O projeto visa conectar microempreendedores, clientes e investidores com um forte foco em práticas de ESG (Environmental, Social, and Governance), fomentando uma economia sustentável e consciente.

Esta documentação reflete um projeto maduro, com um ciclo de vida de desenvolvimento totalmente automatizado, desde os testes de integração até a implantação contínua em múltiplos ambientes na nuvem.

## Como executar localmente com Docker

O ambiente de desenvolvimento local é totalmente orquestrado com Docker, garantindo consistência e eliminando a necessidade de instalar manualmente um banco de dados ou um ambiente Java.

**Pré-requisitos:**
*   Git
*   Docker
*   Docker Compose

**Passos para Execução:**

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/seu-usuario/micronectar-api.git
    cd micronectar-api
    ```

2.  **Inicie os contêineres:**
    Execute o seguinte comando na raiz do projeto:
    ```bash
    docker-compose up --build
    ```
    *   Este comando irá construir a imagem Docker da API (se ainda não existir), iniciar um contêiner para o banco de dados Oracle XE e, somente após o banco de dados estar totalmente pronto (verificado pelo `healthcheck`), iniciará a aplicação.

3.  **Acesse a Aplicação e a Documentação:**
    *   A API estará disponível em `http://localhost:8080`.
    *   A aplicação está configurada para redirecionar a rota raiz (`/`) diretamente para a documentação interativa da API.
    *   Para explorar e testar os endpoints, acesse: **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

## Pipeline CI/CD

A automação do ciclo de vida da aplicação é gerenciada pelo **GitHub Actions**, garantindo que cada alteração no código seja testada e implantada de forma segura e eficiente. O processo é dividido em dois workflows principais.

### Workflow de Integração Contínua (CI)

*   **Arquivo:** `.github/workflows/continuous_integration.yaml`
*   **Gatilho:** Executado a cada `pull request` aberto para as branches `develop` e `main`.
*   **Função:** Sua principal responsabilidade é validar a qualidade e a integridade do código antes que ele seja mesclado.
*   **Etapas:**
    1.  **Configuração do Ambiente:** O job configura um ambiente Ubuntu com Java 21 e cache do Maven.
    2.  **Banco de Dados de Serviço:** Um contêiner temporário do Oracle (`gvenzl/oracle-xe`) é iniciado como um serviço (`services`) dentro do próprio job. Isso garante que os testes de integração sejam executados contra um banco de dados limpo e idêntico ao de produção, sem depender de recursos externos.
    3.  **Execução de Testes:** O comando `mvn test` é executado, rodando todos os testes unitários e de integração. O pipeline só é bem-sucedido se todos os testes passarem.

### Workflow de Entrega Contínua (CD)

*   **Arquivo:** `.github/workflows/continuous_delivery.yaml`
*   **Gatilho:** Executado a cada `push` nas branches `develop` e `main`.
*   **Função:** Automatiza a construção da imagem Docker e a implantação da aplicação nos ambientes correspondentes no Azure App Service.
*   **Etapas:**
    1.  **Build & Push:** A imagem Docker da aplicação é construída e enviada para o **Docker Hub**. A imagem é tagueada com o hash do commit (`${{ github.sha }}`), garantindo rastreabilidade completa.
    2.  **Deploy em Staging:** Se o push foi na branch `develop`, o workflow implanta a imagem recém-criada no ambiente de **Staging** no Azure.
    3.  **Deploy em Produção:** Se o push foi na branch `main`, o workflow implanta a imagem no ambiente de **Produção** no Azure.

### Gerenciamento de Segredos e Ambientes

*   **GitHub Secrets:** Todas as credenciais sensíveis (credenciais do Docker Hub, do Azure e segredos de banco de dados/JWT para cada ambiente) são armazenadas de forma segura no GitHub Secrets.
*   **GitHub Environments:** Os ambientes `staging` e `production` são configurados no GitHub para gerenciar segredos específicos de cada ambiente e proteger as implantações.

## Containerização e Orquestração

### Dockerfile: Imagem Otimizada

O projeto utiliza um `Dockerfile` com a estratégia de **multi-stage build** para criar uma imagem final otimizada, leve e segura.

```Dockerfile
# =============================================================================
# ESTÁGIO 1: Build da Aplicação com Maven e JDK 21
# =============================================================================
# Usamos a imagem oficial do Maven com o Temurin JDK 21.
# O alias 'AS build' nomeia este estágio para referência futura.
FROM maven:3.9.8-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do contêiner.
WORKDIR /app

# Otimização de cache do Docker:
# Copia primeiro o pom.xml e baixa as dependências. A camada de dependências
# só será invalidada se o pom.xml mudar, acelerando builds futuros.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o restante do código-fonte da aplicação.
COPY src ./src

# Compila a aplicação, empacota em um .jar e pula os testes (pois
# os testes serão executados em uma etapa separada no pipeline de CI).
RUN mvn clean package -DskipTests

# =============================================================================
# ESTÁGIO 2: Criação da Imagem Final com JRE Mínimo
# =============================================================================
# Começamos com uma imagem base Alpine, que é extremamente leve e segura,
# contendo apenas o Java Runtime Environment (JRE).
FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho na imagem final.
WORKDIR /app

# Copia APENAS o .jar gerado no estágio 'build' para a imagem final.
# O artefato é renomeado para 'app.jar' para simplificar o comando de execução.
COPY --from=build /app/target/micronectar-api-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080, que é a porta padrão do Spring Boot.
EXPOSE 8080

# Define o comando que será executado quando o contêiner iniciar.
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose: Orquestração do Ambiente Local

O `docker-compose.yml` define e orquestra os serviços necessários para rodar a aplicação localmente, simulando um ambiente de produção.

```yml
# Versão do Docker Compose. '3.8' é uma versão moderna e estável.
version: '3.8'

# Define a rede customizada para nossa aplicação.
# Isso garante um melhor isolamento e permite que os serviços se encontrem pelo nome.
networks:
  micronectar-net:
    driver: bridge

# Define os volumes nomeados para persistência de dados.
volumes:
  db_data:
    driver: local

# Define os serviços (contêineres) que compõem a nossa aplicação.
services:
  # --------------------------------------------------------------------------
  # Serviço do Banco de Dados Oracle
  # --------------------------------------------------------------------------
  db:
    # Imagem do Oracle XE 21c, otimizada para startups rápidos.
    image: gvenzl/oracle-xe:21-slim-faststart
    container_name: micronectar-db
    networks:
      - micronectar-net
    ports:
      # Mapeia a porta 1521 do contêiner para a porta 1521 da sua máquina local,
      # permitindo que você se conecte com o SQL Developer em localhost:1521.
      - "1521:1521"
    volumes:
      # Monta o volume nomeado para persistir os dados do banco.
      - db_data:/opt/oracle/oradata
    environment:
      # Define a senha para o usuário 'system'. DEVE ser a mesma usada pela API.
      - ORACLE_PASSWORD=oracle
      - APP_USER=micronectar_app
      - APP_USER_PASSWORD=micronectar_pass
    healthcheck:
      test: [ "CMD-SHELL", "sqlplus -S system/oracle@//localhost:1521/XEPDB1 <<< \"select open_mode from v\\$$pdbs where name = 'XEPDB1';\" | grep -q 'READ WRITE'" ]
      interval: 15s
      timeout: 10s
      retries: 10
      start_period: 120s

  # --------------------------------------------------------------------------
  # Serviço da API Micronectar
  # --------------------------------------------------------------------------
  api:
    container_name: micronectar-api
    networks:
      - micronectar-net
    # Constrói a imagem a partir do Dockerfile na pasta atual ('.').
    build: .
    ports:
      # Mapeia a porta 8080 do contêiner para a porta 8080 da sua máquina.
      - "8080:8080"
    environment:
      # Ativa o perfil 'local', que carregará application-local.properties
      - SPRING_PROFILES_ACTIVE=local

      # As variáveis abaixo podem sobrescrever os padrões do application-local.properties se necessário,
      # mas aqui elas correspondem aos padrões, o que é uma boa prática.
      - DB_URL=jdbc:oracle:thin:@//db:1521/XEPDB1
      - DB_USERNAME=micronectar_app
      - DB_PASSWORD=micronectar_pass
      - JWT_SECRET=bf7458ffe3ef48193acd8b686a7d37ca9befed868d31cfdb
    depends_on:
      db:
        condition: service_healthy
```
*   **Destaques:** A configuração utiliza um `healthcheck` que verifica ativamente se o Pluggable Database (PDB) do Oracle está pronto para aceitar conexões, resolvendo condições de corrida. A API só é iniciada (`depends_on`) após a confirmação de que o banco está saudável.

## Prints do funcionamento

### Pipeline de CI com sucesso em um Pull Request
<img width="1862" height="701" alt="image" src="https://github.com/user-attachments/assets/8366735d-01e0-4fce-ae93-7719a4ea2c6f" />

### Pipeline de CD com sucesso
<img width="1847" height="708" alt="image" src="https://github.com/user-attachments/assets/cf763a14-f200-40f7-bc4e-db897acaf4b7" />

### Imagem Docker publicada no Docker Hub com a tag do commit
<img width="1903" height="764" alt="image" src="https://github.com/user-attachments/assets/cc411c72-30fb-4f39-a45e-050ed442353d" />

### Página do Azure App Service mostrando o ambiente de Produção online
<img width="1912" height="1079" alt="image" src="https://github.com/user-attachments/assets/373e94c6-b4e5-4ec2-a251-eee9adb0db56" />

### Swagger UI rodando no ambiente de Produção no Azure
<img width="1884" height="966" alt="image" src="https://github.com/user-attachments/assets/108bec5b-7995-454b-ab21-932e158d2496" />

### Teste de um endpoint protegido no Swagger UI (com token)
#### Sem ter colocado um token válido no authorize
<img width="1583" height="1057" alt="image" src="https://github.com/user-attachments/assets/156e7eef-efc5-4cb9-8204-ee748d76f4dd" />

#### Após ter colocado um token válido
<img width="1586" height="1034" alt="image" src="https://github.com/user-attachments/assets/76e8ac54-b4d0-4490-906f-6d78038d94d7" />



## Tecnologias utilizadas

*   **Backend:** Java 21, Spring Boot 3.2.5 (Web, Data JPA, Security, Validation)
*   **Database:** Oracle Database, Flyway (para migrações de schema)
*   **Build & Dependências:** Apache Maven
*   **Segurança:** Autenticação via JWT (JSON Web Token)
*   **Documentação da API:** SpringDoc OpenAPI (Swagger UI)
*   **Containerização:** Docker
*   **Orquestração Local:** Docker Compose
*   **CI/CD:** GitHub Actions
*   **Cloud Provider:** Microsoft Azure (App Service for Containers)
