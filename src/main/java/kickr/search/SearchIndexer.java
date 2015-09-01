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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kickr.db.dao.MatchDAO;
import kickr.db.entity.Match;
import kickr.search.dto.MatchDocument;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;

/**
 *
 * @author nikku
 */
public class SearchIndexer {
  
  private final MatchDAO matchDao;

  private final ElasticSearch elasticSearch;

  private final ObjectMapper objectMapper;

  public SearchIndexer(MatchDAO matchDao, ElasticSearch elasticSearch, ObjectMapper objectMapper) {
    this.matchDao = matchDao;
    
    this.elasticSearch = elasticSearch;
    
    this.objectMapper = objectMapper;
  }

  protected byte[] serialize(Object document) {
    try {
      return objectMapper.writeValueAsBytes(document);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Could not serialize document", e);
    }
  }
  
  public int index() {

    Client client = elasticSearch.getClient();

    List<Match> allMatches = matchDao.getMatches(null);

    int indexed = 0;

    ClusterHealthStatus status = client.admin().cluster()
                                    .prepareHealth()
                                      .get(TimeValue.timeValueMillis(10000))
                                      .getStatus();

    System.out.println("ElasticSearch cluster health status: " + status);

    System.out.println("Indexing " + allMatches.size());

    for (Match match: allMatches) {
      MatchDocument document = MatchDocument.fromMatch(match);

      if (!match.isIndexed()) {
        IndexResponse response = client.prepareIndex("matches", "match", "match")
              .setSource(serialize(document))
              .execute()
              .actionGet();

        System.out.println(response.isCreated());

        match.setIndexed(true);
        indexed++;
      }
    }

    System.out.println("Done.");

    return indexed;
  }
}
