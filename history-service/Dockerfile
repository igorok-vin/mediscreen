FROM bellsoft/liberica-openjdk-alpine:latest
ADD target/history-service-0.0.1-SNAPSHOT.jar history-service-0.0.1-SNAPSHOT.jar
EXPOSE 8072
ENTRYPOINT ["java","-jar","history-service-0.0.1-SNAPSHOT.jar"]