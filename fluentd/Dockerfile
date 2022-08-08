ARG DOCKER_REGISTRY=""
FROM ${DOCKER_REGISTRY}fluent/fluentd:v1.12.0-debian-1.0
USER root
RUN gem uninstall -I elasticsearch && gem install elasticsearch -v 7.17.0
RUN ["gem", "install", "fluent-plugin-concat"]
RUN ["gem", "install", "fluent-plugin-elasticsearch", "--no-document", "--version", "5.2.1"]
RUN ["gem", "install", "fluent-plugin-multi-format-parser"]
COPY ./conf/fluent.conf /fluentd/etc/fluent.conf
