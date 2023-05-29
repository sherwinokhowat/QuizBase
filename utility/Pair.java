package utility;

import java.io.Serializable;

/**
 * A utility class that can hold two values of any type. Null values are
 * permitted. Note that this class is immutable.
 *
 * @author Ricky Qin
 */
public class Pair<A, B> implements Serializable {

  /**
   * The first value
   */
  private A a;

  /**
   * The second value
   */
  private B b;

  /**
   * Constructs a new Pair object with the specified values
   *
   * @param a The first value
   * @param b The second value
   */
  public Pair(A a, B b) {
    this.a = a;
    this.b = b;
  }

  /**
   * Checks if this Pair is equal to another Pair
   *
   * @param other The other Pair
   * @return If the contents of this Pair is equal to the other Pair.
   */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Pair)) {
      return false;
    }
    Pair<?, ?> p2 = (Pair<?, ?>) other;
    return (((a == null) ? (p2.a == null) : a.equals(p2.a)) &&
            ((b == null) ? (p2.b == null) : (b.equals(p2.b))));
  }

  /**
   * Outputs a string representation of this Pair
   *
   * @return <code>{first, second}</code>
   */
  @Override
  public String toString() {
    return "{" + (a == null ? "null" : a) + ", " + (b == null ? "null" : b) + "}";
  }

  /**
   * Gets the first value in this Pair.
   *
   * @return The first value
   */
  public A first() {
    return a;
  }

  /**
   * Gets the second value in this Pair.
   *
   * @return The second value
   */
  public B second() {
    return b;
  }
}
