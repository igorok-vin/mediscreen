FROM mysql:latest

MAINTAINER Igor Nikolaienko

ENV MYSQL_DATABASE patient_service
ENV MYSQL_ROOT_PASSWORD password

ADD patient_service_data.sql /docker-entrypoint-initdb.d
EXPOSE 3306

