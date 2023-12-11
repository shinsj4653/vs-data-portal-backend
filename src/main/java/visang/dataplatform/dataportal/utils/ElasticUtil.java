package visang.dataplatform.dataportal.utils;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.util.ObjectBuilder;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.search.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.DeleteAliasRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.action.admin.indices.alias.Alias;
import co.elastic.clients.elasticsearch.ElasticsearchClient;

import org.junit.jupiter.api.ClassOrderer;
import org.springframework.stereotype.Component;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchKeywordRankDto;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest.*;

@Slf4j
@Component
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ElasticUtil {

    private static ElasticUtil self;
    private RestHighLevelClient client;
    private ElasticsearchClient esClient;
    private RestClient httpClient;

    public ElasticUtil(String hostname, Integer port) {
        httpClient = RestClient.builder(
                new HttpHost(hostname, port)
        ).build();

        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(hostname, port)));

        // Create the Java API Client with the same low level client
        ElasticsearchTransport transport = new RestClientTransport(
                httpClient,
                new JacksonJsonpMapper()
        );

        esClient = new ElasticsearchClient(transport);
    }

    public static ElasticUtil getInstance(String hostname, Integer port) {
        if (self == null)
            self = new ElasticUtil(hostname, port);
        return self;
    }

    public <T> SearchResponse<T> getTotalTableSearch(
            String index, String keyword, List<String> fields, Integer pageNo, Integer amountPerPage, Class<T> className
    ) throws IOException {

        Integer fromNo = (pageNo - 1) * amountPerPage;
        Integer sizeNum = amountPerPage;

        return esClient.search(s -> s
                        .index(index)
                        .query(q -> q
                                .multiMatch(m -> m
                                        .query(keyword)
                                        .fields(fields))
                        )
                        .from(fromNo)
                        .size(sizeNum),
                 className);
    }

    public <T> SearchResponse<T> getAutoCompleteSearchWords(String index, String searchCondition, String keyword, Class<T> className) throws IOException {

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // BoolQueryBuilder
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // Add fuzzy query
        FuzzyQueryBuilder fuzzyQuery = QueryBuilders.fuzzyQuery(searchCondition + ".keyword", keyword).fuzziness(Fuzziness.ONE);
        boolQueryBuilder.should(fuzzyQuery);

        // Add match phrase prefix query
        MatchPhrasePrefixQueryBuilder matchPhrasePrefixQuery = QueryBuilders.matchPhrasePrefixQuery(searchCondition, keyword);
        boolQueryBuilder.should(matchPhrasePrefixQuery);

        return esClient.search(s -> s.index(index)
                .query(q -> q
                        .bool(b -> b
                                .should(sh -> sh
                                        .fuzzy(f -> f
                                                .field(searchCondition + ".keyword")
                                                .value(keyword)
                                                .fuzziness(String.valueOf(Fuzziness.ONE)))
                                )
                                .should(sh -> sh
                                        .matchPhrasePrefix(m -> m
                                                .field(searchCondition)
                                                .query(keyword))))), className);
    }


    public List<TableSearchKeywordRankDto> getTableSearchRank(
            String requestURI, String logType, Integer logResultSize, Integer rankResultSize
    ) {

        List<TableSearchKeywordRankDto> list = new ArrayList<>();

        try {

            // 오늘부터 7일전까지의 Index의 그룹 Alias 명 => "last-7-days"
            String aliasName = "last-7-days";

            // 오늘 날짜
            String todayIndex = "search_logs-" + getCurrentDate();

            log.info("todayIndex : {}", todayIndex);

            // 검색 하기 전
            // last-7-days aliases 관리
            // 오늘의 검색 로그 index는 add, 7일전 날짜의 검색 로그는 delete

            // 만약 현 날짜에 해당하는 검색 로그 Index 없을 시, 새로 생성
//            if (isTodayIndexExist(client, todayIndex)) {
//                log.info("isTodayIndexExist");
//                addIndexToAlias(client, aliasName, todayIndex);
//            }
//            else {
//                // 존재한다면, "last-7-days" Alias에 추가
//                log.info("addIndexToAlias");
//                createTodayIndex(client, todayIndex);
//            }

            // 7일 이후의 날짜에 해당하는 Index는 Alias에서 제거
            removeOldIndicesFromAlias(client, aliasName);

            // Get the actual indices associated with the "last-7-days" alias
            GetAliasesRequest getAliasesRequest = new GetAliasesRequest("last-7-days");
            org.elasticsearch.client.GetAliasesResponse getAliasesResponse = client.indices().getAlias(getAliasesRequest, RequestOptions.DEFAULT);

            String[] indices = getAliasesResponse.getAliases().keySet().toArray(String[]::new);

            SearchRequest searchRequest = new SearchRequest(indices);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            // match -> message : URI
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery.filter(QueryBuilders.termQuery("requestURI.keyword", requestURI));
            boolQuery.filter(QueryBuilders.termQuery("logType.keyword", logType));

            searchSourceBuilder.query(boolQuery);

            // time, requestURI, keyword 필드만 받아오도록
            String[] includes = new String[]{"time", "requestURI", "keyword"};
            searchSourceBuilder.fetchSource(includes, null);

            TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("SEARCH_RANK")
                                                                            .field("keyword.keyword")
                                                                            .size(rankResultSize)
                                                                            .minDocCount(1)
                                                                            .order(BucketOrder.aggregation("_count", false));
            searchSourceBuilder.aggregation(aggregationBuilder);

            // set size
            if (logResultSize != null) {
                searchSourceBuilder.size(logResultSize);
            }

            searchRequest.source(searchSourceBuilder);
            searchRequest.scroll(TimeValue.timeValueMinutes(1));

            org.elasticsearch.action.search.SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            log.info("about to enter aggs");

            RestStatus status = response.status();
            if (status == RestStatus.OK) {

                log.info("status OK");
                Aggregations aggregations = response.getAggregations();
                Terms keywordAggs = aggregations.get("SEARCH_RANK");
                for (Terms.Bucket bucket : keywordAggs.getBuckets()) {
                    list.add(new TableSearchKeywordRankDto(bucket.getKey().toString(), (int) bucket.getDocCount()));
                }
            }

        } catch (IOException e) {
            log.error("getTableSearchRank error : {}", e.getMessage());
        }

        return list;
    }

    private static boolean isTodayIndexExist(RestHighLevelClient client, String index) throws IOException {

        GetIndexRequest request = new GetIndexRequest(index);
        return client.indices().exists(request, RequestOptions.DEFAULT);

    }

    private static void createTodayIndex(RestHighLevelClient client, String index) {

        try {
            // Create index request
            CreateIndexRequest request = new CreateIndexRequest(index);

            // Set Alias at index creation time
            request.alias(new Alias("last-7-days"));

            // Execute the request
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);

            // Check if the index was created successfully
            if (createIndexResponse.isAcknowledged()) {
                log.info("Index created successfully: " + index);
            } else {
                log.info("Failed to create index: " + index);
            }
        } catch (IOException e) {
            // Handle IO exception
            log.error(e.getMessage());
        }

    }

    private static void addIndexToAlias(RestHighLevelClient client, String alias, String index) throws IOException {

        IndicesAliasesRequest request = new IndicesAliasesRequest();
        AliasActions aliasAction =
                new AliasActions(AliasActions.Type.ADD)
                        .index(index)
                        .alias(alias);
        request.addAliasAction(aliasAction);

        ActionListener<AcknowledgedResponse> listener =
                new ActionListener<AcknowledgedResponse>() {
                    @Override
                    public void onResponse(AcknowledgedResponse indicesAliasesResponse) {
                        log.info("Add index to alias successful");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        log.error("Add index to alias failed");
                        log.error("error message : {}", e.getMessage());
                    }
                };

        // Alias에 Index 추가하는 요청 실행
        client.indices().updateAliasesAsync(request, RequestOptions.DEFAULT, listener);
//            if (acknowledgedResponse.isAcknowledged()) {
//                log.info("Add index to alias successful");
//            } else {
//                log.info("Add index to alias failed");
//            }

    }

    private static void removeOldIndicesFromAlias(RestHighLevelClient client, String alias) throws IOException {
        GetAliasesRequest getAliasesRequest = new GetAliasesRequest().indices(alias);
        GetAliasesResponse getAliasesResponse = client.indices().getAlias(getAliasesRequest, RequestOptions.DEFAULT);

        Map<String, Set<AliasMetadata>> aliases = getAliasesResponse.getAliases();
        for (String index : aliases.keySet()) {
            // Check if the index is older than 7 days
            if (isIndexOlderThan7Days(index)) {
                client.indices().deleteAlias(new DeleteAliasRequest(index, alias), RequestOptions.DEFAULT);
                log.info("Removed index {} from alias {}", index, alias);
            }
        }
    }

    private static String getCurrentDate() {
        LocalDate today = LocalDate.now();
        log.info("today : {}", today);
        return today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private static boolean isIndexOlderThan7Days(String index) {
        LocalDate indexDate = LocalDate.parse(index.substring("search_logs-".length()), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        log.info("indexDate : {}", index);
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        return indexDate.isBefore(sevenDaysAgo);
    }

}
