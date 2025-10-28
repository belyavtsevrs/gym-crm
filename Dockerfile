
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw

COPY discovery-service/pom.xml discovery-service/
COPY gym-core/pom.xml gym-core/
COPY gym-security/pom.xml gym-security/
COPY workload-service/pom.xml workload-service/

RUN ./mvnw -B dependency:go-offline

COPY . .

RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:17-jdk AS runtime
WORKDIR /app

ARG SERVICE_NAME
ENV SERVICE_NAME=${SERVICE_NAME}

COPY --from=builder /app/${SERVICE_NAME}/target/${SERVICE_NAME}-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
