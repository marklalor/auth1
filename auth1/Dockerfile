FROM openjdk:11-jre-slim

ARG JAR_FILE=target/auth1-0.1-SNAPSHOT.jar
ARG PORT=50505
ARG DB_CONFIG=src/main/resources/database.example.cnf
ARG APP_CONFIG=src/main/resources/auth1.example.cnf

ENV PORT=${PORT}
ENV DB_CONFIG=${DB_CONFIG}
ENV APP_CONFIG=${APP_CONFIG}

VOLUME /tmp

COPY ${JAR_FILE} app.jar
COPY ${DB_CONFIG} db.cnf
COPY ${APP_CONFIG} app.cnf
COPY docker-entrypoint.sh /usr/local/bin/auth1-entrypoint.sh

CMD ["auth1-entrypoint.sh"]