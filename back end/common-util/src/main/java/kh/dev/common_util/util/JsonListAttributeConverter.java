package kh.dev.common_util.util;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

/**
 * Specialized convenience class for list JSON conversion.
 *
 * @param <T> the type of elements in the list
 */
public abstract class JsonListAttributeConverter<T> extends JsonGenericConverter<List<T>> {
  protected JsonListAttributeConverter(TypeReference<List<T>> typeRef) {
    super(typeRef);
  }
}
