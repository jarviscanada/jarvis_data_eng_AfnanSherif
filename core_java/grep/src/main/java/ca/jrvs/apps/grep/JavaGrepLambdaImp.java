
package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepLambdaImp implements JavaGrepLambda {

  private final Logger logger =
          LoggerFactory.getLogger(JavaGrepLambdaImp.class);

  private String regex;
  private String rootPath;
  private String outFile;
  private Pattern pattern;

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException(
              "USAGE: JavaGrepLambdaImp regex rootPath outFile");
    }

    BasicConfigurator.configure();

    JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try {
      javaGrepLambdaImp.process();
    } catch (Exception e) {
      javaGrepLambdaImp.logger.error(
              "Unable to process grep operation", e);
    }
  }

  @Override
  public void process() throws IOException {
    List<String> matchedLines;

    try (Stream<Path> paths = listFiles(rootPath)) {
      matchedLines = paths
              .map(Path::toFile)
              .flatMap(this::readLines)
              .filter(this::containsPattern)
              .collect(Collectors.toList());
    }

    writeToFile(matchedLines);
  }

  @Override
  public Stream<Path> listFiles(String rootDir) {
    try {
      return Files.walk(Paths.get(rootDir))
              .filter(Files::isRegularFile);
    } catch (IOException e) {
      throw new RuntimeException(
              "Failed to traverse directory: " + rootDir, e);
    }
  }

  @Override
  public Stream<String> readLines(File inputFile) {
    try {
      return Files.lines(inputFile.toPath());
    } catch (IOException e) {
      throw new RuntimeException(
              "Failed to read file: " + inputFile.getPath(), e);
    }
  }

  @Override
  public boolean containsPattern(String line) {
    return line != null && pattern.matcher(line).find();
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    Files.write(Paths.get(outFile), lines);
  }

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
    this.pattern = Pattern.compile(regex);
  }

  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }
}
