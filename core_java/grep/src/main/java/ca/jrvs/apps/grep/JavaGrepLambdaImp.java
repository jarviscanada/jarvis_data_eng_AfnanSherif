package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.BasicConfigurator;

public class JavaGrepLambdaImp extends JavaGrepImp {
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

    List<String> matchedLines =
        listFiles(getRootPath())
        .stream()
        .flatMap(file -> readLines(file))
        .filter(this::containsPattern)
        .collect(Collectors.toList());

    writeToFile(matchedLines);
  }
  @Override
  public Stream<String> readLines(File inputFile){

    try {
      return Files.lines(inputFile.toPath());

    } catch(IOException e){

      throw new RuntimeException(
          "Failed to read file: " + inputFile.getName(), e);
    }
  }
  @Override
  public void writeToFile(List<String> lines) throws IOException {
    Files.write(Paths.get(getOutFile()), lines);
  }
}
