FROM bellsoft/liberica-openjdk-alpine:latest
ADD target/ui-service-0.0.1-SNAPSHOT.jar ui-service-0.0.1-SNAPSHOT.jar
EXPOSE 8070
ENTRYPOINT ["java","-jar","ui-service-0.0.1-SNAPSHOT.jar"]