ARG DOCKER_REGISTRY=""
FROM ${DOCKER_REGISTRY}maven:3.8.4-eclipse-temurin-17 as build
WORKDIR /workspace/app
COPY . .
RUN mvn  -DskipTests package
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM ${DOCKER_REGISTRY}eclipse-temurin:17.0.2_8-jre-focal
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.cargo.booking.internal.gateway.InternalApiGatewayApplication"]