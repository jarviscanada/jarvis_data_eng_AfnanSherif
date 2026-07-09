package ca.jrvs.apps.grep;

import java.io.File;
import java.util.stream.Stream;

public interface JavaGrepLambda extends JavaGrep {

    Stream<String> readLines(File inputFile);
}