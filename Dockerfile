# Stage 1: Build Java Jar
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Runtime environment (Java 17 + Python + ML)
FROM eclipse-temurin:17-jre
WORKDIR /app

# Python runtime aur ML packages install karein
RUN apt-get update && \
    apt-get install -y --no-install-recommends python3 python3-pip && \
    pip3 install --no-cache-dir --break-system-packages scikit-learn numpy || pip3 install --no-cache-dir scikit-learn numpy && \
    rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar
COPY ai.py ./ai.py
COPY predict.py ./predict.py

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]