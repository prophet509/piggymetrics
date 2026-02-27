FROM eclipse-temurin:25-jre
LABEL maintainer="halfbloodprincess9@gmail.com"

WORKDIR /app
COPY ./target/turbine-stream-service.jar /app/turbine-stream-service.jar

ENV JAVA_OPTS=""
CMD ["sh", "-c", "java  -Xmx200m -jar /app/turbine-stream-service.jar"]

EXPOSE 8989
