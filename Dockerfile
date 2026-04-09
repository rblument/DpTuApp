# Build stage
FROM maven:3.9.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=build /app/target/DpTuApp-1.0-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-jar", "DpTuApp-1.0-SNAPSHOT.jar"]