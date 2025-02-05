package com.legend.common_util.util;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A thread-safe context for storing key-value pairs with type safety. This class extends {@link
 * ConcurrentHashMap} to allow dynamic storage and retrieval of objects with explicit type casting.
 */
public class ExecutionContext extends ConcurrentHashMap<String, Object> {

  /**
   * Retrieves a value associated with the specified key and casts it to the expected type.
   *
   * @param key the key whose associated value is to be returned
   * @param type the expected class type of the value
   * @param <T> the type of the value
   * @return the value if present, otherwise {@code null}
   * @throws ClassCastException if the value cannot be cast to the specified type
   */
  public <T> T get(String key, Class<T> type) {
    return Optional.ofNullable(super.get(key)).map(type::cast).orElse(null);
  }

  /**
   * Retrieves an {@link Optional} containing the value associated with the specified key, if
   * present, and casts it to the expected type.
   *
   * @param key the key whose associated value is to be returned
   * @param type the expected class type of the value
   * @param <T> the type of the value
   * @return an {@link Optional} containing the value if present, otherwise an empty {@link
   *     Optional}
   * @throws ClassCastException if the value cannot be cast to the specified type
   */
  public <T> Optional<T> find(String key, Class<T> type) {
    return Optional.ofNullable(super.get(key)).map(type::cast);
  }

  /**
   * Retrieves a value associated with the specified key and casts it to the expected type. If the
   * key is not found, returns the provided default value.
   *
   * @param key the key whose associated value is to be returned
   * @param type the expected class type of the value
   * @param defaultValue the default value to return if the key is not found
   * @param <T> the type of the value
   * @return the value if present, otherwise the default value
   * @throws ClassCastException if the value cannot be cast to the specified type
   */
  public <T> T getOrDefault(String key, Class<T> type, T defaultValue) {
    return find(key, type).orElse(defaultValue);
  }
}
