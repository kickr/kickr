/*
 * The MIT License
 *
 * Copyright 2015 nikku.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package support.form;

import io.dropwizard.jersey.validation.ValidationErrorMessage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nikku
 */
@Singleton
@Consumes("application/x-www-form-urlencoded")
public class FormDataMessageBodyReader implements MessageBodyReader<Object> {

  private static final Logger LOG = LoggerFactory.getLogger("support.form");

  private final Validator validator;

  public FormDataMessageBodyReader(Validator validator) {
    this.validator = validator;
  }

  public boolean isAnnotated(Class<?> annotationCls, Annotation[] annotations) {
    return Stream.of(annotations)
                .filter(a -> annotationCls.isInstance(a))
                .findAny()
                .isPresent();
  }

  @Override
  public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return isAnnotated(FormData.class, annotations);
  }

  @Override
  public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {

    MultivaluedMap<String, String> bodyMap = FormBodyParser.readFrom(new MultivaluedHashMap<>(), mediaType, true, entityStream);

    Object instance = createInstance(type);

    bodyMap.keySet().forEach(key -> {
      populateField(type, instance, key, bodyMap.get(key));
    });

    if (isAnnotated(Valid.class, annotations)) {
      Set<ConstraintViolation<Object>> violations = validator.validate(instance);

      if (!violations.isEmpty()) {
        throw new ConstraintViolationException(violations);
      }
    }
    
    return instance;
  }

  protected Object createInstance(Class type) {
    LOG.debug("Instantiate {}", type.getName());

    try {
      return type.newInstance();
    } catch (IllegalAccessException | InstantiationException e) {
      
      LOG.error(
        "Failed to instantiate {}. " +
        "Does it define an accessible default constructor?", e);
      
      throw new InternalServerErrorException(e);
    }
  }

  protected void populateField(Class<?> type, Object instance, String key, List<String> values) {

    LOG.debug("Populate {}#{} with values {}", type.getName(), key, values);

    String propertyName = key;
    String nestedPropertyName = null;

    boolean collectionProperty = false;

    int nestedSeparatorIdx = key.indexOf(".");

    if (nestedSeparatorIdx != -1) {
      propertyName = key.substring(0, nestedSeparatorIdx);
      nestedPropertyName = key.substring(nestedSeparatorIdx + 1);
    }

    if (propertyName.endsWith("[]")) {
      propertyName = propertyName.substring(0, propertyName.length() - 2);
      collectionProperty = true;
    }

    Field field = getField(type, propertyName);

    if (collectionProperty && nestedPropertyName != null) {
      LOG.debug("Cannot populate nested field on collection property {}", key);

      throw new UnsupportedOperationException("Invalid field: " + key);
    }

    if (collectionProperty) {
      // set collection value
      List list = (List) getOrInitialize(instance, field, () -> new ArrayList());

      values.forEach(v -> list.add(v));
    } else {

      if (nestedPropertyName != null) {
        // set nested value
        Class<?> nestedType = field.getType();

        Object nestedObject = getOrInitialize(instance, field, () -> createInstance(nestedType));

        populateField(nestedType, nestedObject, nestedPropertyName, values);
      } else {

        // set single value
        if (values.size() > 1) {
          LOG.debug("Multiple values in non-collection property {}", key);
          
          throw new BadRequestException("Multiple values in non-collection property " + key);
        }

        set(type, instance, field, values.get(0));
      }
    }
  }

  private Field getField(Class<?> type, String fieldName) {

    try {
      return type.getDeclaredField(fieldName);
    } catch (NoSuchFieldException | SecurityException e) {
      LOG.debug("Not a field: {}#{}", type.getName(), fieldName, e);

      throw new BadRequestException("Invalid field " + fieldName, e);
    }
  }

  private <T> T safeAccess(Field field, Function<Field, T> accessFn) {

    boolean oldAccessible = field.isAccessible();

    try {
      field.setAccessible(true);
      return accessFn.apply(field);
    } finally {
      field.setAccessible(oldAccessible);
    }
  }

  private <T> T getOrInitialize(Object o, Field field, Supplier<T> create) {

    return safeAccess(field, (f) -> {

      try {
        T instance = (T) f.get(o);

        if (instance == null) {
          LOG.debug("Initialize {}#{}", o.getClass().getName(), f.getName());

          instance = create.get();
          f.set(o, instance);
        }

        return instance;
      } catch (ClassCastException | IllegalAccessException | IllegalArgumentException e) {

        LOG.error("Could not get/initialize field {}#{}", o.getClass().getName(), f.getName(), e);

        throw new InternalServerErrorException(e);
      }
    });
  
  }

  
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

  private void set(Class<?> type, Object o, Field field, String value) {

    Object actualValue;

    if (value.isEmpty()) {
      return;
    }

    if (field.getType().isAssignableFrom(Boolean.class)) {
      actualValue = Boolean.parseBoolean(value);

    } else if (field.getType().isAssignableFrom(Integer.class)) {
      actualValue = Integer.parseInt(value, 10);

    } else if (field.getType().isAssignableFrom(Double.class)) {
      actualValue = Double.parseDouble(value);

    } else if (field.getType().isAssignableFrom(Date.class)) {
      try {
        actualValue = DATE_FORMAT.parse(value);
      } catch (ParseException e) {
        LOG.debug("Invalid date value: {}", value, e);

        throw new BadRequestException("Invalid date: " + value, e);
      }

    } else {
      actualValue = value;
    }

    LOG.debug("Coherced {} to {}", value, actualValue);

    safeAccess(field, (f) -> {

      try {
        f.set(o, actualValue);
        return null;
      } catch (IllegalAccessException | IllegalArgumentException e) {
        LOG.error("Failed to populate field {}#{}", type.getName(), f.getName(), e);

        throw new InternalServerErrorException(
            "Failed to populate " + type.getName() + "#" + f.getName(), e);
      }
    });
  }
}
