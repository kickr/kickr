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
package support.web.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import support.reflection.Types;
import support.web.form.AbstractForm;

/**
 *
 * @author nikku
 * @param <T>
 * @param <F>
 */
public class FormView<T extends FormView, F extends AbstractForm> extends AbstractView<T> {

  @JsonIgnore
  private final Class<F> formType;

  private F form;

  public FormView(String template) {
    super(template);

    this.formType = (Class<F>) Types.inferActualType(this.getClass(), 1);
  }

  public T withForm(F form) {
    return chain(() -> this.form = form);
  }

  public F getForm() {
    return form;
  }
}
