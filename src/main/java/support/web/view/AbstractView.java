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
import com.google.common.base.Charsets;
import io.dropwizard.views.View;
import java.util.ArrayList;
import java.util.List;
import kickr.db.entity.user.User;
import support.reflection.Types;

/**
 *
 * @author nikku
 * @param <T>
 */
public abstract class AbstractView<T extends AbstractView> extends View {

  public static class Message {

    private String title;
    private String content;

    public Message(String content) {
      this(null, content);
    }

    public Message(String title, String content) {
      this.title = title;
      this.content = content;
    }

    public String getTitle() {
      return title;
    }

    public String getContent() {
      return content;
    }
  }

  @JsonIgnore
  private final Class<T> viewCls;

  @JsonIgnore
  private User user;

  @JsonIgnore
  private List<Message> messages = new ArrayList<>();

  @JsonIgnore
  private List<Message> errors = new ArrayList<>();

  @JsonIgnore
  protected boolean layout = true;


  public AbstractView(String template) {
    super(template, Charsets.UTF_8);
    
    this.viewCls = (Class<T>) Types.inferActualType(this.getClass(), 0);
  }

  public List<Message> getMessages() {
    return messages;
  }

  public List<Message> getErrors() {
    return errors;
  }

  public User getUser() {
    return user;
  }

  public T addMessage(String content) {
    return this.addMessage(new Message(content));
  }

  public T addMessage(String title, String content) {
    return this.addMessage(new Message(title, content));
  }

  public T addMessage(Message message) {
    return chain(() -> this.messages.add(message));
  }

  public T addError(String content) {
    return this.addError(new Message(content));
  }

  public T addError(Message message) {
    return chain(() -> this.errors.add(message));
  }
  
  public T withUser(User user) {
    return chain(() -> this.user = user);
  }

  public boolean isLayout() {
    return layout;
  }

  public T useLayout(boolean layout) {
    return chain(() -> this.layout = layout);
  }
  
  protected T chain(Runnable runnable) {
    runnable.run();

    return viewCls.cast(this);
  }
}
