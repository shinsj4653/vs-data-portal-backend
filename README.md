# VISANG Dataportal Web Service
## 비상교육의 데이터와 서비스를 한 곳에서 제공하고 통합하는 웹 기반의 플랫폼
> 최종목표 : 비상교육의 모든 데이터(정형, 비정형)를 통합하고 표준화하여 비상교육 임직원들에게 데이터를 손쉽게 검색 및 제공하기 위한 서비스

![image](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/a018f734-9114-4330-a257-0090f28e9a91)

포털 퍼블릭 URL : [DataPortal Public URL](http://15.164.6.183:3000)  

포트폴리오 첨부 : [Portfolio - Google Drive Link](https://drive.google.com/file/d/1_urQlpdor0qqJrpDbI_zxVH9__oygEIi/view?usp=sharing)

## 목차
- [포털의 기능](#포털의-기능)
- [사용 기술 스택](#사용-기술-스택)
- [ERD](#erd)
- [VPC Architecture](#VPC-Architecture)
- [나의 주요 구현 기능](#나의-주요-구현-기능)
  * [1. AWS VPC를 통한 망분리](#1-AWS-VPC를-통한-망분리)
  * [2. 메타 데이터 검색 정확도 향상](#2-메타-데이터-검색-정확도-향상)
  * [3. 실시간 검색어 순위 조회](#3-실시간-검색어-순위-조회)
  * [4. MockMvc 기반 Controller 테스팅](#4-mockmvc-기반-controller-테스팅)
  * [5. Filter 기반 XSS 공격 방지](#5-filter-기반-xss-공격-방지)
 
- [향후 개선 사항](#향후-개선-사항)
  * [1. EC2 인스턴스에 ELK 플랫폼 성공적으로 연결](#1-ec2-인스턴스에-elk-플랫폼-성공적으로-연결)
- [참고 사항](#참고-사항)

## 포털의 기능

![image](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/5cd08a25-b7d5-4d38-bf50-e588cab52f0b)

## 사용 기술 스택
- `Language` : Java 11, JUnit 4
- `Framework` : SpringBoot 2.7.14, MyBatis
- `Database` : PostgreSQL 42.5.0, AWS RDS
- `Deploy` : Github Actions, AWS S3 & CodeDeploy
- `API Docs` : Swagger 3.0.0
- `Logging` : Logback
- `ELK Stack` : ElasticSearch, Logstash, Kibana, Bucket Aggregation
- `ElasticSearch with Java` : Java High Level REST Client, Search API
- `Controller Testing` : MockMVC

## ERD
![image](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/cb38098c-ac34-40f5-9c1f-11ce7010658e)

## VPC Architecture
![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/9184b4c1-bdce-4950-b91a-e037c20346b4)


## 나의 주요 구현 기능

### 1. AWS VPC를 통한 망분리

> 포털 내 DB 테이블에 `개인정보 존재 가능성` 발견
> -> 해당 정보 유출을 막기 위해 public, private 서브넷으로 분리

- 서브넷 분리 이유
1. 내부 인스턴스에 인터넷을 통한 접근 통제
2. 외부에 노출되는 면접 최소화
3. 회사 내 ISMS 보안 규정 만족하기 위해

- 사용한 주요 기능
1. 인터넷 접속을 위한 `Internet Gateway`
2. 한 서브넷에서 다른 서브넷으로 가기 위한 `Routing Table`
3. 특정 경로 및 포트에 대상 그룹 매핑을 통해 서버의 부하 분산을 해주는 `Application Load Balancer`
4. 내부에서 외부로의 접속 가능하게 해주는 `NAT 게이트웨이`
5. 외부에서 SSH 방식으로 인스턴스에 접속하는 것을 허용해주는 `Bastion Host`

- 로드밸런서 도메인 주소 접속에 대한 리버스 프록시 설정

![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/1e2a31ae-1a6e-4975-b844-7fb379318033)  
- ALB는 IP 고정 불가능하여 항상 `도메인 기반으로 사용` 해야함
- 해당 도메인 주소에 대한 CORS 오류를 `리버스 프록시`를 통해 해결


### 2. 메타 데이터 검색 정확도 향상

https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/86e0f1cd-6621-409f-b176-d5bd5f7b2a82  


*ElasticSearch 도입 전*  



https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/a82fe416-162c-4125-a922-1fb7e569e124

*ElasticSearch 도입 후*

- 메타 데이터 검색 기준 중, `한글로 구성된 테이블 설명 및 하위주제`에 `역색인화`를 도입한 빠른 검색 기능 구현을 위해, ElasticSearch의 한글 형태소 분석기인 `Nori Tokenizer` 를 설치 후, 메타 데이터 컬럼에 적용
- `문제은행` 키워드로 검색 시, 도입 전에는 해당 문자열이 그대로 포함된 결과값만 나왔지만 도입 이후에는 `문제`, `은행` 과 같이 더 세밀한 단위까지 나뉘어진 문자열 검색을 수행한 결과를 반환해줌

### 3. 실시간 검색어 순위 조회


https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/1cd75b06-36e7-46c3-be88-bf176d95f53c

*Kibana Console에서 실행한 실시간 검색어 순위 집계 결과*  

```java
log.info("{} {}", keyValue("requestURI", "/metadata/search/total"), keyValue("keyword", keyword));
```

- 검색 기능 사용시, 위와 같은 형식으로 `검색 기록 로그`를 전송하였고 이를 `logback-spring.xml` 파일을 이용하여 `Logstash를 거친 후 ElasticSearch로` 로그가 전송되도록 함

```yaml
# List of pipelines to be loaded by Logstash
#
# This document must be a list of dictionaries/hashes, where the keys/values are pipeline settings.
# Default values for omitted settings are read from the `logstash.yml` file.
# When declaring multiple pipelines, each MUST have its own `pipeline.id`.
#
# Example of two pipelines:
#
- pipeline.id: id_database
  pipeline.workers: 1
  path.config: "../bin/logstash-database.conf"

- pipeline.id: id_searchlog
  pipeline.workers: 1
  path.config: "../bin/logstash-searchlog.conf"
```

*logstash의 multi pipeline 기능 활용*  

 
- 검색 기능에 사용할 `DB 테이블 저장용 설정 파일`과 `검색 로그 전송 관리용 설정 파일`을 따로 분리한 후, 동시에 실행시키기 위해 Logstash의 `pipelines.yml` 파일을 활용

```java
<!-- Logstash -->
<appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
    <destination>localhost:4560</destination>
    <encoder class="net.logstash.logback.encoder.LogstashEncoder">
        <timeZone>GMT+9</timeZone>
        <timestampPattern>yyyy-MM-dd'T'HH:mm:ss</timestampPattern>
        <fieldNames>
            <timestamp>time</timestamp>
        </fieldNames>
    </encoder>
</appender>
```

*Logstash Logback Encoder의 timeZone 및 timestampPattern 옵션 활용*  

- 한국 시간대 기준 포맷을 완성하기 위해, `Logstash Logback Encoder` 기능 활용
- 이를 통해, 명확한 시간 형식이 담긴 로그를 일라스틱 서치로 전송할 수 있었음

```java
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
```
- 로그 필터링 시, requestURI 값과 검색 키워드, 그리고 검색을 시도한 시간대 범위(gte, lte)를 지정한 후, `QueryDSL` 요청을 통해 조건에 맞는 로그를 필터링 함
- 이후, ElasticSearch의 `Bucket Aggregation` 기능을 통해 같은 검색 키워드 별로 `집계 수`를 계산하여 반환해주는 로직을 구현함
- 집계된 결괏값을 자바 환경내에서 받기 위해 `Java High Level REST Client` 를 활용함


https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/109960da-a5fe-45a7-ac0c-4ebe86bf7b2b
```java
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
```
*JAVA 프로젝트 기반 실시간 검색어 순위 결과 조회 API 구현*  

- 검색 키워드 별로 집계된 실시간 순위 결과를 최종적으로 `JSON Object로 가공된 형태로 반환해주는 API`를 완성시킴

### 4. MockMvc 기반 Controller 테스팅
- `@InjectMocks` 를 통해 테스트할 대상의 가짜 객체를 주입받을 수 있다는 점을 활용
- 컨트롤러 테스팅을 위한 Http 호출을 담당하는 `MockMvc` 객체를 중심으로 테스트 코드 작성

```java
@ExtendWith(MockitoExtension.class)
public class DataMapControllerTest {

    @Mock
    private DataMapService dataMapService;

    @InjectMocks
    private DataMapController dataMapController;

    private MockMvc mockMvc;


    @Test
    @DisplayName("데이터 맵 대분류 정보 반환 API 테스트")
    public void 데이터맵_대분류_반환() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(dataMapController).build();

        mockMvc.perform(get("/datamap/category/main"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("데이터 맵 대분류 단위까지의 데이터 조회에 성공하였습니다."));

    }

    @Test
    @DisplayName("데이터 맵 중분류 정보 반환 API 테스트")
    public void 데이터맵_중분류_반환() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(dataMapController).build();

        mockMvc.perform(get("/datamap/category/sub"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("데이터 맵 중분류 단위까지의 데이터 조회에 성공하였습니다."));

    }

    @Test
    @DisplayName("데이터 맵 주요 데이터셋 반환 API 테스트")
    public void 데이터맵_주요_데이터셋_반환() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(dataMapController).build();

        mockMvc.perform(get("/datamap/dataset/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("데이터 맵 모든 주요 데이터 셋 조회에 성공하였습니다."));

    }
}
```

### 5. Filter 기반 XSS 공격 방지
- 기존에 사용한 `lucy-xss-servlet-filter`는 form data 전송 방식에는 적용되지만, `@RequestBody` 로 전달되는 JSON 요청은 처리해주지 않으므로, `MessageConverter`를 사용하는 방법을 택함.
```java
// HTMLCharacterEscapes.java
public class HTMLCharacterEscapes extends CharacterEscapes {
    private static final long serialVersionUID = 1L;
    private final int[] asciiEscapes;

    public HTMLCharacterEscapes() {
        //XSS 방지 처리할 특수 문자 지정
        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['&'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;

    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {
        //Escape 처리
        return new SerializedString(StringEscapeUtils.escapeHtml4(Character.toString((char) ch)));
    }
}

// XSSConfig.java
@Configuration
@RequiredArgsConstructor
public class XssConfig implements WebMvcConfigurer {

    //이미 기존에 등록된 ObjectMapper Bean이 있다면 JSON 요청/응답에서 사용하기 위해 의존성 주입을 받아 사용한다.
    private final ObjectMapper objectMapper;

    // XSS 공격에 대한 Filter 적용
    @Bean
    public FilterRegistrationBean<XssEscapeServletFilter> getFilterRegistrationBean() {
        FilterRegistrationBean<XssEscapeServletFilter> xssRegistrationBean = new FilterRegistrationBean<>();
        xssRegistrationBean.setFilter(new XssEscapeServletFilter());
        xssRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        xssRegistrationBean.addUrlPatterns("/*");
        return xssRegistrationBean;
    }

    // lucy-xss-servlet-filter는 @RequestBody로 전달되는 JSON 요청은 처리해주지 않는다.
    // 따라서, MessageConverter로 처리해줘야한다.
    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(new HTMLCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(copy);
    }


}
```
- `CharacterEscapes` 를 상속하는 클래스 `HtmlCharacterEscapes` 를 만들어 처리해야 할 특수문자를 지정하고 변환한 후, `ObjectMapper`에 `HtmlCharacterEscapes` 를 설정하고 `MessageConverter`에 등록하여 Response가 클라이언트로 넘어가기 전에 처리해주는 로직 구현

## 향후 개선 사항
### 1. EC2 인스턴스에 ELK 플랫폼 성공적으로 연결
- EC2 인스턴스에서 `docker-compose` 를 활용하여 ELK 플랫폼 연동에 성공하였지만, 인스턴스의 서버 용량 초과 문제 발생으로 서버 실행 속도가 급격하게 느려지는 현상이 발생하여 현재는 로컬 환경으로 세팅해둔 상태이다.
- 현재의 `t3a.medium` 요금제에서 더 높은 성능의 요금제로 업그레이드 하거나 `AWS의 OpenSearch 서비스`를 활용해보는 방법을 도입해볼 예정이다.

## 참고 사항
- 회사 프로젝트의 접근 권한은 private이기 때문에, 제 리포지토리에 보이도록 하기 위해 `main 브랜치만 가져온 상태`입니다.
- 제 레포에서는 Github Actions의 Deploy 실패 문구가 보이지만, `실제 현업에서는 정상작동` 하고 있습니다.

![image](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/787fcdc3-686f-4363-9066-adcf37970793)
*현업에서 사용되었던, 혹은 사용중인 브랜치명 목록들*  



![image](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/811ab1df-f5c6-4c97-9fd3-b5f73935c673)
*정상 작동한 Github Actions의 Workflows 이력*

