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

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MultivaluedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A parser that converts {@link MultivaluedMap<String, String>} to form objects.
 * 
 * @author nikku
 */
public class FormParser {

  private static final Logger LOG = LoggerFactory.getLogger("support.form.parser");

  
  public static <T> T toForm(Class<T> type, MultivaluedMap<String, String> bodyMap) {

    T instance = createInstance(type);

    bodyMap.keySet().forEach(key -> {
      populateField(type, instance, key, bodyMap.get(key));
    });

    return instance;
  }


  public static <T> T createInstance(Class<T> type) {
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

  static void populateField(Class<?> type, Object instance, String key, List<String> values) {

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

  static Field getField(Class<?> type, String fieldName) {

    try {
      return type.getDeclaredField(fieldName);
    } catch (NoSuchFieldException | SecurityException e) {
      LOG.debug("Not a field: {}#{}", type.getName(), fieldName, e);

      throw new BadRequestException("Invalid field " + fieldName, e);
    }
  }

  static <T> T safeAccess(Field field, Function<Field, T> accessFn) {

    boolean oldAccessible = field.isAccessible();

    try {
      field.setAccessible(true);
      return accessFn.apply(field);
    } finally {
      field.setAccessible(oldAccessible);
    }
  }

  static <T> T getOrInitialize(Object o, Field field, Supplier<T> create) {

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


  static void set(Class<?> type, Object o, Field field, String value) {

    Object actualValue;

    if (value.isEmpty()) {
      return;
    }

    if (field.getType().isAssignableFrom(Boolean.class)) {
      actualValue = Boolean.parseBoolean(value);

    } else if (field.getType().isAssignableFrom(Integer.class)) {
      actualValue = Integer.parseInt(value, 10);

    } else if (field.getType().isAssignableFrom(Long.class)) {
      actualValue = Long.parseLong(value, 10);

    } else if (field.getType().isAssignableFrom(Double.class)) {
      actualValue = Double.parseDouble(value);

    } else if (field.getType().isAssignableFrom(Date.class)) {
      try {
        actualValue = FormUtil.parseDate(value);
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
