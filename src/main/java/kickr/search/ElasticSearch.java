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
package kickr.search;

import io.dropwizard.lifecycle.Managed;
import java.net.MalformedURLException;
import java.net.URL;
import kickr.search.config.ElasticConfiguration;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 *
 * @author nikku
 */
public class ElasticSearch implements Managed {

  private final ElasticConfiguration config;

  private Client client;

  public ElasticSearch(ElasticConfiguration config) {
    this.config = config;
  }

  private InetSocketTransportAddress createAddress(String urlStr) throws MalformedURLException {
    URL url = new URL(urlStr);
    
    return new InetSocketTransportAddress(url.getHost(), url.getPort());
  }

  @Override
  public void start() throws Exception {
    client = new TransportClient().addTransportAddress(createAddress(config.getUrl()));
  }

  @Override
  public void stop() throws Exception {
    client.close();
  }

  public Client getClient() {
    return client;
  }
}
