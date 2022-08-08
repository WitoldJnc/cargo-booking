ARG DOCKER_REGISTRY=""
FROM ${DOCKER_REGISTRY}maven:3.8.4-eclipse-temurin-17
WORKDIR /workspace/app
COPY . .
RUN mvn -DskipTests install
