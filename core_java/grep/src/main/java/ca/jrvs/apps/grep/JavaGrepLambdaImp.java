package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class JavaGrepLambdaImp extends JavaGrepImp {
  public static void main(String[] args) {
    JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();

    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRegex(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try {
      javaGrepLambdaImp.process();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  @Override
  public void process() throws IOException {

    List<String> matchedLines = listFiles(getRootPath())
      .stream()
      .flatMap(file -> readLines(file).stream())
      .filter(this::containsPattern)
      .collect(Collectors.toList());
    writeToFile(matchedLines);
  }
  @Override
  public List<String> readLines(File inputFile){
    try {
      return Files.lines(inputFile.toPath())
        .collect(Collectors.toList());
    }catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<File> listFiles(String rootDir){
    try {
      return Files.walk(Paths.get(rootDir))
        .filter(Files::isRegularFile)
        .map(Path::toFile)
        .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  @Override
  public void writeToFile(List<String> lines) throws IOException {
    Files.write(Paths.get(getOutFile()), lines);
  }
}
