FROM mongo:6.0
LABEL maintainer="sqshq@sqshq.com"

COPY ./dump /dump
COPY ./docker-entrypoint-initdb.d/ /docker-entrypoint-initdb.d/
