package org.lambda;

import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.function.Consumer;

public interface LambdaStreamExc {
  /**
   * Create a String stream from array
   *
   * @param strings
   * @return
   */
  public Stream<String> createStrStream(String ...strings);

  /**
   * Convert all strings to uppercase
   *
   * @param strings
   * @return
   */
  public Stream<String> toUpperCase(String ...strings);

  /**
   * filter strings that contains the pattern
   *
   * @param stringStream
   * @param pattern
   * @return
   */
  public Stream<String> filter(Stream<String> stringStream, String pattern);

  /**
   * Create a intStream from a arr[]
   *
   * @param arr
   * @return
   */
  public IntStream createIntStream(int[] arr);

  /**
   * Convert a intStream to list
   *
   * @param stream
   * @return
   * @param <E>
   */
  public <E> List<E> toList(Stream<E> stream);

  /**
   * Convert intStream to list
   * @param stream
   * @return
   */
  public List<Integer> toList(IntStream stream);

  /**
   * Create a IntStream range from start to end inclusive
   * @param start
   * @param end
   * @return
   */
  public IntStream createIntStream(int start, int end);

  /**
   * Convert a intStream to a doubleStream
   * and compute square root of each element
   * @param intStream
   * @return
   */
  public DoubleStream squareRootIntStream(IntStream intStream);

  /**
   * filter all even number and return odd numbers from a intStream
   * @param intStream
   * @return
   */
  public IntStream getOdd(IntStream intStream);

  /**
   * Return a lambda function that print a message with a prefix and suffix
   * @param prefix
   * @param suffix
   * @return
   */
  public Consumer<String> getLambdaPrinter(String prefix, String suffix);

  /**
   * Print each message with a given printer
   * @param messages
   * @param printer
   */
  public void printMessage(String[] messages, Consumer<String> printer);

  /**
   * Print each message with a given printer
   * @param intStream
   * @param printer
   */
  public void printOdd(IntStream intStream, Consumer<String> printer);

  /**
   * Print all odd number from the input
   * @param ints
   * @return
   */
  public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints);
}
