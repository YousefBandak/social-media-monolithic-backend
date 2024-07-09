FROM mysql
ENV MYSQL_DATABASE=oop \
    MYSQL_ROOT_PASSWORD=

ADD schema.sql /docker-entrypoint-initdb.d

EXPOSE 3306