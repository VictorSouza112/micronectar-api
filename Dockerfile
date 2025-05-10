# 1. Imagem Base: Usa uma imagem OpenJDK 21 sobre Alpine Linux
FROM eclipse-temurin:21-jre-alpine

# 2. Volume: Define um ponto de montagem para dados temporários (boa prática)
VOLUME /tmp

# 3. Expor Porta: Informa que a aplicação no contêiner ouvirá na porta 8080
EXPOSE 8080

# 4. Argumento para o JAR: Define uma variável para o caminho do JAR
#    (ajuste o nome do JAR se for diferente)
ARG JAR_FILE=target/micronectar-api-0.0.1-SNAPSHOT.jar

# 5. Adicionar JAR: Copia o arquivo JAR do build para dentro da imagem como 'app.jar'
ADD ${JAR_FILE} app.jar

# 6. Ponto de Entrada: Define o comando para executar a aplicação quando o contêiner iniciar
ENTRYPOINT ["java","-jar","/app.jar"]