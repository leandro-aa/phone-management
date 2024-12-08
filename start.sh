#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

# Define variables
CURRENT_DIR=".\\"
DOCKER_APP_DIR="$CURRENT_DIR/docker"
JAR_FILE_PATTERN="phone-management*.jar"
TARGET_JAR_FILE_NAME="app.jar"
DOCKER_COMPOSE_FILE="$DOCKER_APP_DIR/docker-compose.yaml"

# Function to display error messages and wait for user input
error_exit() {
    echo "[ERROR] $1"
    echo
    read -rp "Press Enter to exit..."
    exit 1
}

# Trap errors and handle them
trap 'error_exit "An unexpected error occurred."' ERR

# Step 1: Build the Spring Boot application
echo "[INFO] Building Spring Boot application..."
if [ -f "$CURRENT_DIR/mvnw" ]; then
    ./mvnw clean package -DskipTests || error_exit "Spring Boot build failed."
else
    error_exit "Maven wrapper (mvnw) not found in $CURRENT_DIR."
fi

# Step 2: Create Docker folder if it doesn't exist and move the matching JAR file
echo "[INFO] Preparing Docker folder and moving JAR file..."
mkdir -p "$DOCKER_APP_DIR"
MATCHING_JAR=$(find "$CURRENT_DIR/target" -type f -name "$JAR_FILE_PATTERN" | head -n 1)
if [ -n "$MATCHING_JAR" ]; then
    cp "$MATCHING_JAR" "$DOCKER_APP_DIR/$TARGET_JAR_FILE_NAME" || error_exit "Failed to move JAR file."
    echo "[INFO] Moved $MATCHING_JAR to $DOCKER_APP_DIR/$TARGET_JAR_FILE_NAME"
else
    error_exit "No JAR file matching pattern '$JAR_FILE_PATTERN' found in the target directory."
fi

# Step 3: Build the Docker image for the Spring Boot app
echo "[INFO] Building Docker image for Spring Boot application..."
if [ -f "$DOCKER_APP_DIR/Dockerfile" ]; then
    cd "$DOCKER_APP_DIR" || error_exit "Failed to change directory to $DOCKER_APP_DIR."
    docker build --tag=phone-management:latest . || error_exit "Docker build failed."
    cd - >/dev/null
else
    error_exit "Dockerfile not found in $DOCKER_APP_DIR."
fi

# Step 4: Run Docker Compose
echo "[INFO] Running Docker Compose..."
if [ -f "$DOCKER_COMPOSE_FILE" ]; then
    docker compose -f "$DOCKER_COMPOSE_FILE" up -d || error_exit "Docker Compose failed to start."
else
    error_exit "Docker Compose file $DOCKER_COMPOSE_FILE not found."
fi

echo "[INFO] Application setup complete and running!"

# Wait for user input to close the console
echo
read -rp "Press Enter to exit..."
