FROM eclipse-temurin:25-jre
LABEL maintainer="halfbloodprincess9@gmail.com"

WORKDIR /app
COPY ./target/auth-service.jar /app/auth-service.jar

ENV JAVA_OPTS=""
CMD ["sh", "-c", "java  -Xmx200m -jar /app/auth-service.jar"]

EXPOSE 5000
