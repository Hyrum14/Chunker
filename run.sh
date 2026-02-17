#!/bin/bash
# Chunker - Word Chunking Application Runner

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR"

# Build classpath from all JARs in lib folder
CLASSPATH="out/production/Chunker:$(find lib -type f -name '*.jar' | tr '\n' ':' | sed 's/:$//')"

# Run the application
java -cp "$CLASSPATH" app.view.gui.GuiView "$@"
