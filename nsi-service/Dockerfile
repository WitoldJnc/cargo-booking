ARG DOCKER_REGISTRY=""
FROM ${DOCKER_REGISTRY}maven:3.8.4-eclipse-temurin-17-alpine as build
WORKDIR /workspace/app
COPY . .
RUN --mount=type=cache,target=/root/.m2 --mount=type=bind,from=docker-reg.centre-it.com/maven-settings-cit,source=settings.xml,target=/root/.m2/settings.xml mvn -Dpmd.skip=true -DskipTests package
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM ${DOCKER_REGISTRY}eclipse-temurin:17.0.2_8-jre-alpine
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","dme.cargo.nsi.NsiServiceApplication"]