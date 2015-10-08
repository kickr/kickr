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
package support.web.form;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import support.reflection.Types;

/**
 *
 * @author nikku
 * @param <T>
 */
public abstract class AbstractForm<T extends AbstractForm> implements Serializable {

  private final Class<T> formCls;

  public AbstractForm() {
    this.formCls = (Class<T>) Types.inferActualType(this.getClass(), 0);
  }

  private Set<ConstraintViolation<T>> errors;

  public T withGenericErrors(Set<ConstraintViolation<?>> errors) {

    Set<ConstraintViolation<T>> castedErrors =
            errors.stream()
              .map((e) -> (ConstraintViolation<T>) e)
              .collect(Collectors.toSet());
    
    return withErrors(castedErrors);
  }

  public T withErrors(Set<ConstraintViolation<T>> errors) {
    return chain(() -> this.errors = errors);
  }

  public T withError(ConstraintViolation<T> error) {

    Set<ConstraintViolation<T>> errors = new HashSet<>();
    errors.add(error);
  
    return withErrors(errors);
  }

  public Set<ConstraintViolation<T>> getErrors() {
    return errors;
  }

  public T chain(Runnable r) {
    r.run();

    return formCls.cast(this);
  }
}
