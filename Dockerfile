# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/DpTuApp-1.0-SNAPSHOT.jar .
EXPOSE 53637
CMD ["java", "-jar", "DpTuApp-1.0-SNAPSHOT.jar"]