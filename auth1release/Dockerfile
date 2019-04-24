FROM auth1:working

ARG DB_CONFIG=database.cnf
ARG APP_CONFIG=auth1.cnf
ARG REDIS_CONFIG=redis.cnf

COPY ${DB_CONFIG} db.cnf
COPY ${APP_CONFIG} app.cnf
COPY ${REDIS_CONFIG} redis.cnf

