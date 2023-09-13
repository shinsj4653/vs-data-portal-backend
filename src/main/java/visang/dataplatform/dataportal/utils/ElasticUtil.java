package visang.dataplatform.dataportal.utils;

import lombok.*;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ElasticUtil {

    private static ElasticUtil self;
    private RestClientBuilder restClientBuilder;

    public ElasticUtil(String hostname, int port) {

        HttpHost host = new HttpHost(hostname, port);
        restClientBuilder = RestClient.builder(host);
    }

    public static ElasticUtil getInstance(String hostname, int port) {
        if (self == null)
            self = new ElasticUtil(hostname, port);
        return self;
    }

    public List<Map<String, Object>> getTotalTableSearch(
            String index, String query, List<String> fields, Integer size
    ) {

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // multi-match query
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(query, fields.toArray(new String[fields.size()])));

        // set size
        if (size != null) {
            searchSourceBuilder.size(size);
        }
        searchRequest.source(searchSourceBuilder);

        List<Map<String, Object>> list = new ArrayList<>();
        try (RestHighLevelClient client = new RestHighLevelClient(restClientBuilder)) {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = response.getHits();
            for (SearchHit hit : searchHits) {
                Map<String, Object> sourceMap = hit.getSourceAsMap();
                list.add(sourceMap);
            }
        } catch (IOException e) {}

        return list;

    }

    public List<Map<String, Object>> getTableSearchRank(
            String index, String query, List<String> fields, Integer size
    ) {

    }

}
