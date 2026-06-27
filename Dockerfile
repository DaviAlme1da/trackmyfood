FROM maven:3.9.4-amazoncorretto-21 AS builder

WORKDIR /trackmyfood

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS runner

WORKDIR /trackmyfood

COPY --from=builder /trackmyfood/target/*.jar trackmyfood.jar

CMD [ "java", "-jar", "trackmyfood.jar"]