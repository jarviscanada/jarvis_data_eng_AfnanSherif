package org.regex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegexExcImp implements RegexExc {
  public boolean matchJpeg(String filename) {
    filename = filename.toLowerCase();
    String ext = filename.substring(filename.lastIndexOf(".")+1);
    return ext.equals("jpeg") || ext.equals("jpg");
  }

  public boolean matchIp(String ip) {
    return ip.matches("(\\d{1,3}\\.){3}\\d{1,3}");
  }

  @Override
  public boolean isEmptyLine(String line) {
    return line.matches("^(\\s*|(\\d{1,3}\\.){3}\\d{1,3})$");
  }
  public List<String> readLines(File inputFile) {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
      String line;
      while ((line = reader.readLine()) != null){
        lines.add(line);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return lines;
  }

  public static void main(String[] args) {
    RegexExcImp tool = new RegexExcImp();

    // 1. Test matchJpeg
    System.out.println("--- Testing JPEG Matcher ---");
    System.out.println("image.jpg: " + tool.matchJpeg("image.jpg"));
    System.out.println("photo.jpeg: " + tool.matchJpeg("photo.jpeg"));
    System.out.println("file.png: " + tool.matchJpeg("file.png"));

    // 2. Test matchIp
    System.out.println("\n--- Testing IP Matcher ---");
    System.out.println("1.2.3.4: " + tool.matchIp("1.2.3.4"));
    System.out.println("192.168.0.1: " + tool.matchIp("192.168.0.1"));
    System.out.println("abc.def.ghi.jkl: " + tool.matchIp("abc.def.ghi.jkl"));

    // 3. Test isEmptyLine
    System.out.println("\n--- Testing Empty Line Matcher ---");
    System.out.println("empty string: " + tool.isEmptyLine(""));
    System.out.println("spaces only: " + tool.isEmptyLine("   "));
    System.out.println("normal text: " + tool.isEmptyLine("hello"));

    // 4. Test readLines
    System.out.println("\n--- Testing readLines ---");

    File myFile = new File("test.txt");

    try {
      List<String> lines = tool.readLines(myFile);

      for (String line : lines) {
        System.out.println("Line: [" + line + "]");
      }

    } catch (RuntimeException e) {
      System.out.println("Could not read file: " + e.getMessage());
    }
  }
}
