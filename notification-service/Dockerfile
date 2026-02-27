FROM eclipse-temurin:25-jre
LABEL maintainer="halfbloodprincess9@gmail.com"

WORKDIR /app
COPY ./target/notification-service.jar /app/notification-service.jar

ENV JAVA_OPTS=""
CMD ["sh", "-c", "java  -Xmx200m -jar /app/notification-service.jar"]

EXPOSE 8000
