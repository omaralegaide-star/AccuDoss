#!/bin/sh
# Lightweight Gradle Wrapper delegate to pre-installed system gradle
# Designed for fast builds in CI environments like Codemagic

if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
else
  echo "Error: gradle command not found in PATH."
  echo "Please install Gradle or run in an environment with Gradle pre-installed."
  exit 1
fi
