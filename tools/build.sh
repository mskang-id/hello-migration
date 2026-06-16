#!/usr/bin/env bash
# Build the multi-module reactor inside a JDK8 container. Host stays clean.
set -e
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
docker run --rm \
  -v "$ROOT":/app \
  -v shop-api-m2:/root/.m2 \
  -w /app \
  maven:3.9-eclipse-temurin-8 \
  mvn "$@"
