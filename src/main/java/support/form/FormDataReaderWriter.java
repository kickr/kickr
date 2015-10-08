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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Stream;
import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import org.glassfish.jersey.message.internal.AbstractFormProvider;

/**
 * A provider that serializes and deserializes HTTP forms.
 * 
 * @author nikku
 */
@Singleton
@Consumes("application/x-www-form-urlencoded")
@Produces("application/x-www-form-urlencoded")
public class FormDataReaderWriter extends AbstractFormProvider<Object> {

  private final Validator validator;

  public FormDataReaderWriter(Validator validator) {
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

    MultivaluedMap<String, String> bodyMap = readFrom(new MultivaluedHashMap<>(), mediaType, true, entityStream);

    Object instance = FormParser.toForm(type, bodyMap);

    if (isAnnotated(Valid.class, annotations)) {
      Set<ConstraintViolation<Object>> violations = validator.validate(instance);

      if (!violations.isEmpty()) {
        throw new ConstraintViolationException(violations);
      }
    }
    
    return instance;
  }

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    System.out.println(type + ": " + Serializable.class.isAssignableFrom(type));
    return Serializable.class.isAssignableFrom(type);
  }

  @Override
  public void writeTo(Object instance, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
    MultivaluedMap<String, String> bodyMap = FormSerializer.toMap(instance, type);

    writeTo(bodyMap, mediaType, entityStream);
  }
}
