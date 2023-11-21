package visang.dataplatform.dataportal.utils;

import lombok.*;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchKeywordRankDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            String index, String keyword, List<String> fields, Integer pageNo, Integer amountPerPage
    ) {

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // multi-match query
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, fields.toArray(new String[fields.size()])));

        // set from -> 검색결과 시작 지점(0부터 count)
        searchSourceBuilder.from(pageNo * amountPerPage);

        // set size -> 검색결과 반환 갯수
        searchSourceBuilder.size(amountPerPage);

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

    public List<TableSearchKeywordRankDto> getTableSearchRank(
            String index, String uri, String gte, String lte, Integer logResultSize, Integer rankResultSize
    ) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // match -> message : URI
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("message", uri));

        // range -> gte to lte
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("time")
                .gte(gte)
                .lte(lte);
        boolQuery.must(rangeQuery);

        searchSourceBuilder.query(boolQuery);

        // time, requestURI, keyword 필드만 받아오도록
        String[] includes = new String[]{"time", "requestURI", "keyword"};
        searchSourceBuilder.fetchSource(includes, null);

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("KEYWORD_RANK")
                                                                        .field("keyword.keyword")
                                                                        .size(rankResultSize);
        searchSourceBuilder.aggregation(aggregationBuilder);

        // set size
        if (logResultSize != null) {
            searchSourceBuilder.size(logResultSize);
        }

        searchRequest.source(searchSourceBuilder);
        searchRequest.scroll(TimeValue.timeValueMinutes(1));

        List<TableSearchKeywordRankDto> list = new ArrayList<>();
        try (RestHighLevelClient client = new RestHighLevelClient(restClientBuilder)) {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            RestStatus status = response.status();
            if (status == RestStatus.OK) {
                Aggregations aggregations = response.getAggregations();
                Terms keywordAggs = aggregations.get("KEYWORD_RANK");
                for (Terms.Bucket bucket : keywordAggs.getBuckets()) {
                    list.add(new TableSearchKeywordRankDto(bucket.getKey().toString(), (int) bucket.getDocCount()));
                }
            }

        } catch (IOException e) {}

        return list;
    }

}
