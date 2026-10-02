# Stage 1: Maven se Java Spring Boot project build karein
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Runtime environment (Java 17 + Python + ML Libraries)
FROM openjdk:17-jdk-slim
WORKDIR /app

# Python, pip aur required ML libraries install karein (predict.py ke liye)
RUN apt-get update && \
    apt-get install -y --no-install-recommends python3 python3-pip && \
    pip3 install --no-cache-dir scikit-learn numpy && \
    rm -rf /var/lib/apt/lists/*

# Maven build se jar file copy karein
COPY --from=build /app/target/*.jar app.jar

# Python scripts copy karein taaki Java code inhe direct access kar sake
COPY ai.py ./ai.py
COPY predict.py ./predict.py

# Render default port
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]