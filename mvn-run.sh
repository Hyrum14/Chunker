#!/bin/bash
# Chunker - Maven Runner
# Compiles and runs the project using Maven

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Please install Maven first."
    echo "On macOS with Homebrew: brew install maven"
    exit 1
fi

# Run Maven to build and run
mvn clean compile exec:java -Dexec.mainClass="app.view.gui.GuiView"
