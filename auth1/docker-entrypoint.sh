#!/usr/bin/env sh

java -jar /app.jar \
-Djava.security.egd=file:/dev/./urandom \
--server.port=$PORT \
--config.databaseConfigurationPath=/db.cnf \
--config.auth1ConfigurationPath=/app.cnf \
--config.redisConfigurationPath=/redis.cnf