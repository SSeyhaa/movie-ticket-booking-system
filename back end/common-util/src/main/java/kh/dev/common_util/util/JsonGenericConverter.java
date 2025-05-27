package kh.dev.common_util.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import java.util.Collections;
import kh.dev.common_util.exception.JsonConversionException;

/**
 * Generic base class for converting objects or lists to and from JSON strings for Jakarta attribute
 * storage. This class unifies single object and list conversion functionality.
 *
 * @param <T> the type of the entity attribute (either a single object or a list)
 */
public abstract class JsonGenericConverter<T> implements AttributeConverter<T, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Class<T> clazz;
  private final TypeReference<T> typeRef;
  private final boolean isList;

  /**
   * Constructor for single object conversion.
   *
   * @param clazz the class type of the entity attribute
   */
  protected JsonGenericConverter(Class<T> clazz) {
    this.clazz = clazz;
    this.typeRef = null;
    this.isList = false;
  }

  /**
   * Constructor for list conversion using TypeReference.
   *
   * @param typeRef the type reference for the list or complex type
   */
  protected JsonGenericConverter(TypeReference<T> typeRef) {
    this.clazz = null;
    this.typeRef = typeRef;
    this.isList = true;
  }

  /**
   * Converts the entity attribute to a JSON string to be stored in the database.
   *
   * @param attribute the entity attribute
   * @return JSON string
   */
  @Override
  public String convertToDatabaseColumn(T attribute) {
    if (attribute == null) {
      return isList ? "[]" : null;
    }

    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new JsonConversionException("Failed to convert attribute to JSON", e);
    }
  }

  /**
   * Converts the JSON string from the database back to the entity attribute.
   *
   * @param dbData the JSON string from the database
   * @return the deserialized entity attribute
   */
  @Override
  @SuppressWarnings("unchecked")
  public T convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isBlank()) {
      return isList ? (T) Collections.emptyList() : null;
    }

    try {
      if (isList) {
        return objectMapper.readValue(dbData, typeRef);
      } else {
        return objectMapper.readValue(dbData, clazz);
      }
    } catch (JsonProcessingException e) {
      throw new JsonConversionException("Failed to convert JSON to attribute", e);
    }
  }
}
