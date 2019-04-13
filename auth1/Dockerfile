FROM openjdk:11-jre-slim

ARG JAR_FILE=target/auth1-0.1-SNAPSHOT.jar
ARG PORT=50505
ARG DB_CONFIG=src/main/resources/database.docker.cnf
ARG APP_CONFIG=src/main/resources/auth1.docker.cnf
ARG REDIS_CONFIG=src/main/resources/redis.docker.cnf

ENV PORT=${PORT}

VOLUME /tmp

COPY ${JAR_FILE} app.jar
COPY ${DB_CONFIG} db.cnf
COPY ${APP_CONFIG} app.cnf
COPY ${REDIS_CONFIG} redis.cnf
COPY docker-entrypoint.sh /usr/local/bin/auth1-entrypoint.sh

CMD ["auth1-entrypoint.sh"]
