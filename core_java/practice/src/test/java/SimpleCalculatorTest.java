package src.test.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.main.java.org.example.SimpleCalculator;
import src.main.java.org.example.SimpleCalculatorImpl;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCalculatorTest {
  SimpleCalculator calculator;

  @BeforeEach
  void setUp() {
    calculator = new SimpleCalculatorImpl();
  }

  @Test
  void add() {
    int expected = 2;
    int actual = calculator.add(1, 1);
    assertEquals(expected, actual);
  }

  @Test
  void subtract() {
    int expected = 1;
    int actual = calculator.subtract(2, 1);
    assertEquals(expected, actual);
  }

  @Test
  void multiply() {
    int expected = 2;
    int actual = calculator.multiply(2, 1);
    assertEquals(expected, actual);
  }

  @Test
  void divide() {
    int expected = 10;
    double actual = calculator.divide(20, 2);
    assertEquals(expected, actual);
  }
}
