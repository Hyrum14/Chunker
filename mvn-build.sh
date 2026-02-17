#!/bin/bash
# Chunker - Build Maven Project
# Creates an executable JAR file

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Please install Maven first."
    echo "On macOS with Homebrew: brew install maven"
    exit 1
fi

# Build the project with Maven Shade plugin
echo "Building Chunker executable JAR..."
mvn clean package -DskipTests

if [ -f "target/chunker.jar" ]; then
    echo ""
    echo "✓ Build successful!"
    echo "Executable JAR created: target/chunker.jar"
    echo ""
    echo "To run the application:"
    echo "  java -jar target/chunker.jar"
else
    echo "✗ Build failed"
    exit 1
fi
