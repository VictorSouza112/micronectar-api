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