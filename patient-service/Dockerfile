FROM bellsoft/liberica-openjdk-alpine:latest
ADD target/patient-service-0.0.1-SNAPSHOT.jar patient-service-0.0.1-SNAPSHOT.jar
EXPOSE 8071
ENTRYPOINT ["java","-jar","patient-service-0.0.1-SNAPSHOT.jar"]