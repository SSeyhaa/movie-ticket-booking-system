package kh.dev.common_util.file.csv;

import org.springframework.stereotype.Component;

/**
 * Interface for mapping between Java objects and CSV representations.
 *
 * @param <O> the type of the Java object
 * @param <C> the type of the CSV representation
 */
@Component
public interface CSVMapper<O, C> {

  /**
   * Creates a Java object from a CSV representation.
   *
   * @param c the CSV representation
   * @return the Java object
   */
  O mapTo(C c);

  /**
   * Creates a CSV representation from a Java object.
   *
   * @param o the Java object
   * @return the CSV representation
   */
  C unmapFrom(O o);
}
