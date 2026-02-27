FROM eclipse-temurin:25-jre
LABEL maintainer="halfbloodprincess9@gmail.com"

WORKDIR /app
COPY ./target/statistics-service.jar /app/statistics-service.jar

ENV JAVA_OPTS=""
CMD ["sh", "-c", "java  -Xmx200m -jar /app/statistics-service.jar"]

EXPOSE 7000
