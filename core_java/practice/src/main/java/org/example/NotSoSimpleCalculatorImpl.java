package src.main.java.org.example;
import java.lang.Math;

public class NotSoSimpleCalculatorImpl implements NotSoSimpleCalculator {
  private SimpleCalculator calc;

  public NotSoSimpleCalculatorImpl(SimpleCalculator calc) {
    this.calc = calc;
  }

  @Override
  public double power(double x, double y) {
    return Math.pow(x, y);
  }

  @Override
  public int abs(int x) {
    return Math.abs(x);
  }

  @Override
  public double sqrt(int x) {
    return Math.sqrt(x);
  }


}
