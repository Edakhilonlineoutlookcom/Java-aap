# ==========================================
# Stage 1: Build the WAR artifact with Maven
# ==========================================
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy project definition and dependencies first to leverage caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and package the application
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# Stage 2: Runtime Environment on Tomcat
# ==========================================
FROM tomcat:10.1-jdk17

# Remove default Tomcat root apps to ensure clean routing
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the compiled WAR artifact from the builder stage into Tomcat root
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Expose the standard servlet container port
EXPOSE 8080

# Start Tomcat in the foreground
CMD ["catalina.sh", "run"]
