FROM eclipse-temurin:25-jre
LABEL maintainer="halfbloodprincess9@gmail.com"

WORKDIR /app
COPY ./target/gateway.jar /app/gateway.jar

ENV JAVA_OPTS=""
CMD ["sh", "-c", "java  -Xmx200m -jar /app/gateway.jar"]

EXPOSE 4000
