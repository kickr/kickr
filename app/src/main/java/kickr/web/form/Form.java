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
package kickr.web.form;

import java.util.Set;
import javax.validation.ConstraintViolation;
import kickr.util.Types;

/**
 *
 * @author nikku
 * @param <T>
 */
public class Form<T extends Form> {

  private final Class<T> formCls;

  public Form() {
    this.formCls = (Class<T>) Types.inferActualType(this.getClass(), 0);
  }

  private Set<ConstraintViolation<T>> errors;

  public T withErrors(Set<ConstraintViolation<T>> errors) {
    return chain(() -> this.errors = errors);
  }

  public Set<ConstraintViolation<T>> getErrors() {
    return errors;
  }

  public T chain(Runnable r) {
    r.run();

    return formCls.cast(this);
  }
}
