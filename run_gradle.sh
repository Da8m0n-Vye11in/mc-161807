#!/usr/bin/env bash
set -e
# Clean and build using the Gradle wrapper
./gradlew clean build

# Try to run the application. Prefer 'gradlew run' if the project declares an application/run task.
if ./gradlew -q tasks --all | grep -q "\brun\b"; then
  echo "Running via 'gradlew run'..."
  ./gradlew run
  exit 0
fi

# Find a built jar and run it
JAR=$(ls build/libs/*.jar 2>/dev/null | head -n1 || true)
if [ -z "$JAR" ]; then
  echo "No executable jar was found. Please ensure build.gradle.kts configures the application plugin or run './gradlew run' if available." >&2
  exit 1
fi

echo "Running $JAR ..."
java -jar "$JAR"
