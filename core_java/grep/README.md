# Introduction

This project is a Java application that mimics the Linux `grep` command by recursively searching files and extracting lines that match a given regular expression. Two implementations were developed: `JavaGrepImp` using core Java and `JavaGrepLambdaImp` using Java Streams and lambda expressions. The project utilizes core Java concepts such as OOP, collections, file I/O, exception handling, recursion, and regular expressions. Additional tools and technologies used include Maven for dependency management, SLF4J/Log4j for logging, IntelliJ IDEA as the IDE, Git/GitHub for source control, and Docker for application distribution.

# Quick Start

## Prerequisites

- Java 16+
- Maven
- Docker

## Package the application

```bash
mvn clean package
```

## Run JavaGrepImp

```bash
java -cp target/core_java-1.0-SNAPSHOT.jar \
ca.jrvs.apps.grep.JavaGrepImp \
"ERROR" \
"./logs" \
"./matched.txt"
```

## Run JavaGrepLambdaImp

```bash
java -cp target/core_java-1.0-SNAPSHOT.jar \
ca.jrvs.apps.grep.JavaGrepLambdaImp \
"ERROR" \
"./logs" \
"./matched.txt"
```

# Implementation
The application follows a modular workflow design where the `process()` method acts as the high-level controller of the program. Instead of implementing all logic in a single method, the workflow is separated into smaller helper methods following clean coding principles such as DRY and KISS. The application first recursively traverses all files under the given root directory, reads each file line-by-line, checks whether each line matches the provided regular expression, stores the matching results, and finally writes them into an output file.

Two implementations were developed:

- `JavaGrepImp`
  - Uses traditional core Java programming techniques such as loops, `BufferedReader`, and manual file traversal.

- `JavaGrepLambdaImp`
  - Uses Java Streams and lambda expressions to simplify the workflow using functional-style programming.
## Pseudocode

```text
matchedLines = []

for each file in listFiles(rootPath)
    lines = readLines(file)

    for each line in lines
        if containsPattern(line)
            matchedLines.add(line)

writeToFile(matchedLines)
```

## Performance Issue

The current implementation loads all matching lines and file contents into memory, which can cause high memory usage when processing large files or directories. This may eventually lead to `OutOfMemoryError`. A better approach would be processing files line-by-line using streams and writing matched lines directly to the output file instead of storing all matches in memory first.

# Test

The application was tested manually by creating sample directories containing text files with different contents and patterns. Multiple test cases were executed using different regular expressions to verify that only matching lines were written to the output file. Edge cases such as empty files, invalid paths, nested directories, and files without matches were also tested. The output was manually compared against expected grep results to ensure correctness. Debugging was performed using IntelliJ IDEA debugger and SLF4J logging.

# Deployment

The application was dockerized for easier distribution and execution across environments. A lightweight OpenJDK base image was used to build the Docker image. The generated JAR file was copied into the container and executed using the JVM.

## Build Docker Image

```bash
docker build -t grep-app .
```

## Run Docker Container

```bash
docker run grep-app "ERROR" "/data" "/output/result.txt"
```

Docker ensures consistent runtime behavior without requiring users to install Java or Maven locally.

# Improvement

1. Improve memory efficiency by streaming matching lines directly to the output file instead of storing all matches in memory.
2. Add JUnit and Mockito unit tests for automated testing and improved code coverage.
3. Enhance functionality by supporting additional grep features such as case-insensitive search, recursive depth control, and filename filtering.
