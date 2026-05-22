package org.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {

  public static void main(String[] args) {

    LambdaStreamExc lse = new LambdaStreamExcImp();

    // 1. createStrStream
    System.out.println("1. createStrStream:");
    lse.createStrStream("a", "b", "c")
      .forEach(System.out::println);

    // 2. toUpperCase
    System.out.println("\n2. toUpperCase:");
    lse.toUpperCase("hello", "world")
      .forEach(System.out::println);

    // 3. filter
    System.out.println("\n3. filter:");
    lse.filter(Stream.of("apple", "dog", "banana", "fish"), "a")
      .forEach(System.out::println);

    // 4. createIntStream from array
    System.out.println("\n4. createIntStream from array:");
    int[] arr = {1, 2, 3, 4, 5};
    lse.createIntStream(arr)
      .forEach(System.out::println);

    // 5. toList from Stream
    System.out.println("\n5. toList from Stream:");
    List<String> stringList = lse.toList(Stream.of("Java", "Python", "SQL"));
    System.out.println(stringList);

    // 6. toList from IntStream
    System.out.println("\n6. toList from IntStream:");
    List<Integer> intList = lse.toList(lse.createIntStream(1, 5));
    System.out.println(intList);

    // 7. createIntStream range
    System.out.println("\n7. createIntStream range:");
    lse.createIntStream(0, 5)
      .forEach(System.out::println);

    // 8. squareRootIntStream
    System.out.println("\n8. squareRootIntStream:");
    lse.squareRootIntStream(lse.createIntStream(new int[]{1, 4, 9, 16}))
      .forEach(System.out::println);

    // 9. getOdd
    System.out.println("\n9. getOdd:");
    lse.getOdd(lse.createIntStream(0, 10))
      .forEach(System.out::println);

    // 10. getLambdaPrinter
    System.out.println("\n10. getLambdaPrinter:");
    lse.getLambdaPrinter("start>", "<end")
      .accept("Message body");

    // 11. printMessages
    System.out.println("\n11. printMessages:");
    String[] messages = {"a", "b", "c"};
    lse.printMessage(
      messages,
      lse.getLambdaPrinter("msg:", "!")
    );

    // 12. printOdd
    System.out.println("\n12. printOdd:");
    lse.printOdd(
      lse.createIntStream(0, 5),
      lse.getLambdaPrinter("odd number:", "!")
    );

    // 13. flatNestedInt
    System.out.println("\n13. flatNestedInt:");
    Stream<List<Integer>> nestedInts = Stream.of(
      Arrays.asList(1, 2),
      Arrays.asList(3, 4),
      Arrays.asList(5)
    );

    lse.flatNestedInt(nestedInts)
      .forEach(System.out::println);
  }
}
