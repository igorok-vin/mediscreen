FROM bellsoft/liberica-openjdk-alpine:latest
ADD target/report-service-0.0.1-SNAPSHOT.jar report-service-0.0.1-SNAPSHOT.jar
EXPOSE 8073
ENTRYPOINT ["java","-jar","report-service-0.0.1-SNAPSHOT.jar"]