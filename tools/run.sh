#!/usr/bin/env bash
# Run the web module (tomcat7:run) inside a JDK8 container, expose 8080.
set -e
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
docker run --rm --name shop-api-run \
  -p 8080:8080 \
  -v "$ROOT":/app \
  -v shop-api-m2:/root/.m2 \
  -w /app \
  maven:3.9-eclipse-temurin-8 \
  mvn -q -pl shop-api-web -am tomcat7:run
