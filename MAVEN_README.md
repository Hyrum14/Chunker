# Chunker - Maven Project Setup

This project has been converted to a Maven project for easier dependency management and building.

## Project Structure

```
Chunker/
├── pom.xml                    # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/             # Source code
│   │   │   ├── app/
│   │   │   ├── utils/
│   │   │   └── Runner.java
│   │   └── resources/
│   └── test/
│       └── java/             # Test code
├── target/                   # Build output
│   ├── chunker.jar          # Executable fat JAR (all dependencies included)
│   └── classes/             # Compiled classes
├── mvn-build.sh             # Build script
├── mvn-run.sh               # Run script
└── run.sh                   # Legacy run script (still works)
```

## Prerequisites

- **Java 16+** (for pattern matching and switch expressions used in the code)
- **Maven 3.8.0+** installed

### Install Maven on macOS
```bash
brew install maven
```

### Verify Installation
```bash
mvn --version
java -version
```

## Building the Project

### Option 1: Using Maven directly
```bash
mvn clean package -DskipTests
```

This creates:
- `target/chunker.jar` - Executable fat JAR with all dependencies

### Option 2: Using the build script
```bash
./mvn-build.sh
```

## Running the Application

### Option 1: Run the executable JAR directly
```bash
java -jar target/chunker.jar
```

### Option 2: Using the run script
```bash
./mvn-run.sh
```

This will compile and run the project directly without creating a JAR first.

### Option 3: Legacy run script (uses local classpath)
```bash
./run.sh
```

## Development

### Compile the project
```bash
mvn compile
```

### Run tests
```bash
mvn test
```

### Clean build artifacts
```bash
mvn clean
```

### View dependency tree
```bash
mvn dependency:tree
```

## Dependencies

The project includes the following key dependencies:

- **Apache POI** (5.2.5) - Excel/OOXML support
- **Apache Commons** - Collections, IO, Lang, Logging, Digester, etc.
- **Log4j** (2.21.1) - Logging
- **SLF4J** (1.7.36) - Logging facade
- **JUnit 5** (5.8.1) - Testing

All dependencies are automatically downloaded from Maven Central Repository on first build.

## Troubleshooting

### "mvn: command not found"
Install Maven using `brew install maven` and ensure it's in your PATH.

### "Java version mismatch"
The project requires Java 16+. Check your Java version with `java -version` and update if needed.

### "BUILD FAILURE" during compilation
Ensure you have Java 16+ installed:
```bash
java -version
```

### Dependency issues
Clear Maven cache and retry:
```bash
rm -rf ~/.m2/repository
mvn clean package -DskipTests
```

## Notes

- The project uses Maven Shade Plugin to create a fat JAR with all dependencies bundled
- All dependencies are managed in `pom.xml` - no need to manually manage JARs in a `lib` folder
- The executable JAR (`target/chunker.jar`) is completely self-contained and portable
