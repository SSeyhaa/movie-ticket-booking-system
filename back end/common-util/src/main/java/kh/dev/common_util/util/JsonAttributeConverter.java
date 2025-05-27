package kh.dev.common_util.util;

/**
 * Specialized convenience class for single object JSON conversion.
 *
 * @param <T> the type of the entity attribute
 */
public abstract class JsonAttributeConverter<T> extends JsonGenericConverter<T> {
  protected JsonAttributeConverter(Class<T> clazz) {
    super(clazz);
  }
}
