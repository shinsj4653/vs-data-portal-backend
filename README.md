## V사의 데이터와 서비스를 한 곳에서 제공하고 통합하는 웹 기반의 플랫폼
> 최종목표 : V사의 모든 데이터(정형, 비정형)를 통합하고 표준화하여 비상교육 임직원들에게 데이터를 손쉽게 검색 및 제공하기 위한 서비스

 
![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/367266cb-9af1-4253-8b22-163d991b42cd)


## 목차
- [포털의 목적](#포털의-목적)
- [포털의 기능](#포털의-기능)
- [사용 기술 스택](#사용-기술-스택)
- [ERD](#erd)
- [Architecture](#Architecture)
- [ElasticSearch vs RDBMS](#ElasticSearch-vs-RDBMS)
- [주요 구현 기능](#주요-구현-기능)
  * [1. 추천 검색어 조회](#1-추천-검색어-조회)
  * [2. 인기 검색어 조회](#2-인기-검색어-조회)
 
- [검색 품질 및 성능 개선](#검색-품질-및-성능-개선)
  - [품질 개선](#품질-개선)
    * [1. 동의어, 사용자 사전 도입](#1-동의어,-사용자-사전-도입)
    * [2. 메타 데이터 검색 쿼리 개선](#2-메타-데이터-검색-쿼리-개선)
  - [성능 개선](#성능-개선)
    * [1. ES와 RDBMS 동기화 statement 튜닝](#1-ES와-RDBMS-동기화-statement-튜닝)
    * [2. primary, replica 샤드 개수 조정](#2-primary,-replica-샤드-개수-조정)
    * [3. 검색 로그 Index - force merge](#3-검색-로그-Index---force-merge)

- [추가 구현 기능](#추가-구현-기능)
   * [1. ALB를 통한 포트 기반 라우팅](#1-ALB를-통한-포트-기반-라우팅)

- [향후 개선 사항](#향후-개선-사항)
  - [ ] 1. 삭제된 행에 대한 동기화
  - [ ] 2. 캐싱을 통한 실시간 검색어 순위 결과 개선
- [참고 사항](#참고-사항)

## 포털의 목적  

![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/c0455df3-3780-4789-869e-55fab5d5b2b8)

## 포털의 기능

![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/d9590b21-c055-461e-9784-32aa6e4ccdd9)


이 중에서 `메타 데이터 검색` 기능을 담당함

## 사용 기술 스택
- `Language` : Java 11, JUnit 4
- `Framework` : SpringBoot 2.7.14, MyBatis
- `Database` : PostgreSQL 42.5.0, AWS RDS
- `Deploy` : Github Actions, AWS S3 & CodeDeploy
- `API Docs` : Swagger 3.0.0
- `Logging` : Logback
- `Elastic Stack` : ElasticSearch, Logstash, Kibana
- `ElasticSearch Client` : HighLevel REST Client
- `Controller Testing` : MockMVC

## ERD
![image](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/cb38098c-ac34-40f5-9c1f-11ce7010658e)

## Architecture
![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/6a089527-b97e-4ae4-9a34-6d067895a20d)

## ElasticSearch vs RDBMS
ElasticSearch를 한번도 사용하지 않은 분들은 향후 글이 이해하기 어려울 수 있을거라 판단하여, 간단한 비교 그래프를 게시하였다.  

![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/3dc0b426-493c-402c-9aad-357c161aebd5)


## 주요 구현 기능
### 1. 추천 검색어 조회
> 목표 : 검색어 입력 시, 다음과 같이 추천 검색어 보여주기  
![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/3ea332ee-d4c6-4f8b-99b3-9158310c5e0a)

- 메타 데이터 -> 테이블ID, 코멘트(설명), 카테고리(하위주제)로 분류
- 검색 대상은 `카테고리` 로 설정

![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/f0d6ba20-cad9-4e7c-9846-ecc682c8f9e6)  


카테고리 값을 다음 기준으로 토큰화 함으로써, 검색어에 일치하는 토큰이 있을 경우 해당 카테고리 값을 반환해줬다.  
토큰이 많아질수록 ES 메모리를 더 많이 쓴다는 단점이 있지만, 더욱 세밀한 범위까지 검색이 가능해져 검색 정확도를 높일 수 있다.
- 한글 형태소
- 오타 교정
- 초성 검색
- 영 -> 한, 한 -> 영
- 자동완성

그리고, 각각의 결과는 다음과 같다.  
|기준|결과|상세설명|  
|:---:|:---:|:---:|
|한글 형태소|![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/e63cc1df-e1e8-433f-ac85-8c6b19fd0da2)|`월별매출 및 조정내역`을 Nori Tokenizer를 이용하여 `월별`, `매출`, `및`, `조정`, `내역`으로 토큰화하였으므로, `월별`로 검색하였을 때 해당 결과를 얻기 가능|
|오타 교정|![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/a5735e53-81f9-4512-9c43-f2010e5324a5)|`교자` 검색 시, Fuzzy Query를 통해 `교재`라는 교정된 검색어를 추천|
|초성 검색|![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/be7a792a-ef56-489f-a3af-5289eee1be57)|hanhinsam 초성 플러그인을 통해, `ㄱㅈ`으로 토큰화되는 카테고리를 모두 추천|
|영한 변환|![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/6f7869b3-ca5d-4de5-9082-5f6a8c60045d)|hanhinsam 영한 변환 플러그인을 통해, `CMS`의 한글 결과인 `츤`이 토큰으로 저장됨|
|자동완성|![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/7eb1376a-88f0-4e74-bd73-e32ae5ef6cff)|Edge NGram으로 인해 `교재`의 자동완성 토큰에 `굦`이 포함되므로, `굦`으로 검색 시 `교재`가 결과로 조회됨|

Edge NGram 전에는 일반 NGram을 사용하여 자동완성을 해줬다.  
![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/89dc6b83-f8ca-4f29-8a02-742de0f31003)  
*일반 NGram의 토큰 처리*

그러나, 사람들은 보통 단어의 `앞에서부터` 검색하는 경우가 많은 것을 고려하여, 앞쪽을 기준으로 자동완성된 결과를 토큰으로 만들기 위해 `Edge NGram`을 사용하였다.  
![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/90e05b80-0b42-4e9a-9b59-8e53591eb3ee)  
*Edge Ngram의 토큰 처리*


### 2. 인기 검색어 조회
> 목표 : 금일 기준으로 7일간의 검색어 순위를 조회

- 이전 전략 1. 하나의 Index에 모든 검색 로그 관리
ES Index의 Document 삭제는 삭제가 아닌 `불용 처리`를 해주기 때문에 필요한 저장 공간이 계속 증가하여 검색 성능이 낮아질 위험이 있다.

- 이전 전략 2. 일별로 검색 로그 Index 관리 및 자동삭제
Kibana의 Index Management 설정을 통해 Index 생성 후, 7일이 지나면 자동적으로 삭제되도록 하려했다. 하지만, 7일이라는 기준은 아직 임시적이고, 언제 기준이 바뀔지 모르므로, 모든 로그 Index는 보존하기로 결정함.

- 최종 전략 : `Alias - Index Grouping` 사용
Alias : Index의 또 다른 이름
7일 동안의 Index 7개를 하나의 Alias인 `last-7-days`에 등록함. 또한, 7일이 지난 Index는 Alias에서 제외시켜줌으로써 `슬라이딩 윈도우` 방식으로 일별 로그 인덱스를 관리하였다.


## 검색 품질 및 성능 개선


### 품질 개선

1. 동의어, 사용자 사전 도입  
> 목표
> 1. "시험" 이라고 검색 시, "TEST"가 동의어로 인식되어 "TEST"에 대한 결과도 조회되도록 구현
> 2. 동의어로 등록한 단어가 nori tokenizer 결과와 일치하지 않는 문제 해결
* * *  
`구현 1. 동의어 사전`  


![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/6dee5f4a-7965-4301-b778-f723e1419485)  
*Analyzer의 토큰화 절차*

각 Document의 field는 `Analyzer`를 통해 토큰화가 되는데, `Tokenizer`를 통해 먼저 나눠지고, `Filter`를 통해 토큰화된 결과들을 변경하거나 추가, 삭제해준다.
동의어 처리를 위해서는, ES에서 제공하는 `synonym` 필터를 사용하면 된다.  
 

기본적으로 filter영역 내 synonyms 항목에서 직접 동의어를 입력하는 방법과 `동의어 사전 파일을 만들어 synonyms_path로 지정하는 방법`이 있다. 동의어 사전의 양과 잦은 수정을 고려하여 두 번째 방법을 택하기로 하였다.  


메타 데이터의 테이블 ID는 `TAB_TT_TEST` 와 같이 영어 단어들이 underscore로 구분된 형태이다. 이미 underscore를 기준으로 테이블 id 값을 토큰화하였기 때문에, 분리된 영어단어들에 해당하는 동의어들을 정의해주면 검색 품질을 더욱 개선할 수 있을거라 생각하였다.  

`시험`과 `TEST`가 동의어로 인식되기 위해 다음 형식으로 파일 내용을 구성하였다.  
```text
시험, TEST
```  
쉼표와 공백으로 둘을 구분하면, 서로가 서로의 동의어로 인식된다. 모든 동의어들은 회사의 `표준단어정의서` 사전을 참고하여 만들었다. 공통표준단어명과 공통표준단어영문약어명을 등록함으로써, 한글 검색 시 사전을 기준으로 같은 의미로 해석되는 영어 단어명이 함께 검색되도록 하였다.  
* * *
`구현 2. 사용자 사전`  

synonym 필터 사용 시, 다음과 같은 오류가 발생하였다.  

```text
"term: 가맹점 analyzed to a token (가맹) with position increment != 1 (got: 0)"
```
즉, 이미 nori tokenizer가 `가맹점`을 `가맹`과 `점`으로 토큰화 하였지만, 동의어 사전에는 여전히 `가맹점`이 존재하기 때문에 생기는 오류였다.  
이를 해결하기 위해 nori tokenizer의 `user_dictionary(사용자 사전)` 옵션을 사용하여, 토큰화 되지 않을 단어들을 정의해줬다.  
표준단어정의서의 공통표준단어명(한글)들을 모두 등록해줌으로써 해당 오류를 해결할 수 있었다.  
* * *
`최종결과`


https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/575156da-36ba-47a4-9ce4-9de8a7e6f068  


*동의어 사전 적용 전 `시험` 검색 결과*  


https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/9e48e7fe-3a8b-4444-8115-6f4a0cd410c9  


*동의어 사전 적용 후 `시험` 검색 결과*  
`시험`의 표준용어사전의 동의어인 `TEST`에 해당하는 검색 결과도 함께 조회된다.

2. 메타 데이터 검색 쿼리 개선  
> 목표  
> 1. 메타 데이터의 3개 필드 조회에서 1개의 필드만 조회하도록 검색 쿼리 개선
> 2. `문제은행`으로 검색 시, `문제`, `은행` 으로 나뉘어져 검색되지 않고 `문제은행` 에만 매칭되는 결과 반환
> 3. `모든`과 같은 부사를 검색하여도 해당되는 검색결과 반환
* * *
`구현 1. copy_to를 통해 검색 쿼리 개선`  

기존에는 테이블 id, 코멘트, 카테고리 -> 총 3개의 필드에 대해서 `multi_match`를 수행하여 검색하고 있었다.  
```text
POST tb_table_meta_info/_search
{
  "query" :{
    "multi_match": {
      "query": "정보",
      "fields" : ["table_id", "table_comment", "small_clsf_name"]
    }
  }
}
```
위 3개의 필드를 `copy_to`를 사용하여 하나의 필드로 복사하여 해당 필드만으로 관리 가능하다.  
copy_to로 복사한 필드에 지정된 값은 _source에는 저장되지 않고 역인덱스 구조에만 저장되어 공간 절약도 가능하다.
```text
"table_id": {
        "type": "text",
        "copy_to": ["metaDataText"]
        , "fields": {
          "keyword": {
            "type": "keyword"
          }
        }
      },
      "table_comment": {
        "type": "text",
        "copy_to": [
          "metaDataText"], 
          "fields" : {
            "keyword": {
                "type": "keyword"
              }
          }
      },
        "small_clsf_name" :  {
          "type": "text",
          "copy_to": [
          "metaDataText"
          ], 
          "fields" : {
            "keyword": {
                "type": "keyword"
              }
          }
        },
```
`metaDataText` 필드명으로 copy_to를 시행하여 다음과 같이 쿼리 개선이 가능하다.  
```text
POST tb_table_meta_info/_search
{
  "query": {
 
          "match": {
            "metaDataText": {
              "query": "폭신폭신"
            }
            
          }
          
        }
      
  
}
```
* * *
`구현 2. minimum_should_match`  

`문제은행`으로 검색 시, `문제` 혹은 `은행` 키워드로 토큰화되어 BANK 관련 데이터가 조회되는 문제가 있었다.  
이를 방지하기 위해 `minimum_should_match` 옵션을 사용하였다.  
```text
POST tb_table_meta_info/_search?pretty
{
  "query": {
 
          "match": {
            "metaDataText": {
              "query": "문제은행",
              "minimum_should_match": "100%"
            }
            
          }
          
        }
      
  
}
```
정확히 `문제은행`에만 일치하는 메타 데이터 값만 조회되도록 하려면 수치를 `100%`로 설정하면 된다.  
`operator: and` 옵션도 이와 같은 효과를 발휘할 수 있지만, `minimum_should_match` 옵션은 수치 조정을 통해 조회되는 검색 결과를 수정할 수 있고, 무엇보다 여러 테스트를 거쳐 최적의 수치를 설정할 수 있는 장점이 있기에 이를 사용하였다.
* * *  
`구현 3. match + term 검색`  

- `match` : analyzer를 거쳐서 형태소 분석이 된 후, 해당 결과를 이용  
- `term` : analyzer를 거치지 않고 text 그대로 equal 검색  
`metaDataText` field에 사용되는 analyzer에 `nori_part_of_speech` 필터를 적용하여 저렴`한` 양말`과` 신발에서 `한`,`과` 같은 조사나 부사, 감탄사, 특수문자를 제거한 후 토큰화 되도록 하였다.  
하지만, 메타 데이터의 테이블 설명 field 중, `모든`이 들어가 있는 경우가 있었다.  
따라서, `analyzer를 거치는 text 타입의 필드와 거치지 않는 keyword 타입의 필드` 2개를 모두 사용하기로 하였다.  
두 기준 중, 하나의 조건에라도 부응하는 결과를 반환하기 위해 `bool의 should query`를 활용하여 다음과 같이 쿼리를 완성하였다.  
```text
POST tb_table_meta_info/_search
{
  "query": {
    "bool": {
      "should": [
        {
          "match": {
            "metaDataText": {
              "query": "모든",
              "minimum_should_match": "100%"
            }
          }
        },
        {
          "term": {
            "metaDataKeyword": "모든"
          }
        }
      ]
    
    }
  }
}
```
* * *  
`최종 결과`  

query 실행 시, `profile: true`옵션과 url에 query string으로 `human=true` 옵션을 주면, 검색 시 걸린 시간을 편하게 조회할 수 있다.  
- 이전의 3개 컬럼에 대한 multi_match query 결과
```text
"profile" : {
    "shards" : [
      {
        "id" : "[HfBvk1E9Tjep9IbcEVe1ag][tb_table_meta_info][0]",
        "searches" : [
          {
            "query" : [
              {
                "type" : "DisjunctionMaxQuery",
                "description" : "(table_comment:태블릿 | small_clsf_name:태블릿 | table_id:태블릿)",
                "time" : "8.9ms",
                "time_in_nanos" : 8902588,
...
...
...
"fetch" : {
          "type" : "fetch",
          "description" : "",
          "time" : "8.3ms",
          "time_in_nanos" : 8364694,
          "breakdown" : {
            "load_stored_fields" : 7746271,
            "load_stored_fields_count" : 228,
            "next_reader" : 27499,
            "next_reader_count" : 3
          },
```
- 개선된 match + term query 결과
```text
"profile" : {
    "shards" : [
      {
        "id" : "[HfBvk1E9Tjep9IbcEVe1ag][tb_table_meta_info][0]",
        "searches" : [
          {
            "query" : [
              {
                "type" : "BooleanQuery",
                "description" : "metaDataText:태블릿 metaDataKeyword:태블릿",
                "time" : "2.1ms",
                "time_in_nanos" : 2140644,
...
...
...
"fetch" : {
          "type" : "fetch",
          "description" : "",
          "time" : "6.5ms",
          "time_in_nanos" : 6573202,
          "breakdown" : {
            "load_stored_fields" : 6092941,
            "load_stored_fields_count" : 295,
            "next_reader" : 18944,
            "next_reader_count" : 3
          },
```

|쿼리 유형|시간 유형|소요 시간|
|:---:|:---:|:---:|
|multi_match with 3 fields|query time|8.9ms|
|match + term with 1 field|query time|2.1ms|
|multi_match with 3 fields|fetch time|8.3ms|
|match + term with 1 field|fetch time|6.5ms|  


### 성능 개선

1. primary, replica 샤드 개수 조정

> 목표
> 1. 포털 사이트에 적합한 ElasticSearch의 샤드 개수 설정

* * *
`구현 1. primary 샤드 개수 조정을 통한 검색 성능 비교`    
기본적으로 Index는 생성될 때, primary 샤드 1개 + replica 샤드 1개로 이뤄진다. 
Index 내 Document는 샤드를 거쳐 세그먼트에 저장된다. 샤드를 너무 많이 생성한다면 리소스 낭비와 모든 샤드에 접근해야하는 단점이 생긴다.  
반면, 개수가 너무 적다면 병렬 처리 효과를 크게 낼 수 없으면 분산도가 떨어진다.  
replica 샤드는 primary 샤드에 장애 발생 시, 이를 복구하기 위한 역할이므로 데이터 무결성 및 가용성을 위해 최소 1개 이상 세팅하는 것이 옳다고 본다. 그래서, primary 샤드 개수 조정을 하며 Indexing 테스트의 결과를 비교하였다.  


|값 유형|primary 샤드 개수|결과|
|:---:|:---:|:---:|
|index_time_in_millis|1개|5907ms|
|index_time_in_millis|2개|4973ms|
|size_in_bytes|1개|3419916 byte|
|size_in_bytes|2개|4275228 byte|  

저장공간의 크기는 primary 샤드가 2개일 때 더 큰 반면, 인덱싱 시간은 1개일때보다 더 적다.  

공식문서에서 제안한 방법 중, 샤드의 크기가 작으면 세그먼트가 작아져 오버헤드가 증가한다고 한다. 하지만, 이는 `강제 병합` 작업을 통해 더 큰 세그먼트로 변환하여 쿼리 성능을 향상할 수 있다고 한다.  
아직은 데이터 양이 많지 않아 primary 샤드를 1개로 세팅하였다. 향후 메타 데이터 양이 더욱 많아진다면 샤드 개수 조정이 필요할텐데, 공식 문서에 써져있는 것 처럼 샤드 개수를 하나의 노드에 설정한 힙 1GB당 20개 미만으로 유지하는 것을 따를 예정이다.

2. 검색 로그 Index - force merge
> 목표 : Index내 세그먼트 merge 작업을 통한 조회 쿼리 속도 개선
* * *
`구현 1. 읽기 전용 로그 Index의 force merge`  
primary 샤드 개수 조정을 하면서 세그먼트에 물리적 파일들로 실제 데이터가 저장되는 것을 알았다.  
문서가 수정 혹은 삭제 되어도 세그먼트의 `불변성` 때문에 기존 세그먼트는 그대로 두고 추가 세그먼트를 생성한다. 이렇게 되면 조회해야 하는 세그먼트가 많아져 성능 저하가 일어날 수 있으므로, `세그먼트 병합(merge)` 과정을 통해 검색 성능과 저장 공간을 절약 가능하다.  


![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/2c911a33-67ee-4a5d-873c-fa48fcdccabb)  

*문서 삭제 후 세그먼트 병합 과정*

검색 로그 인덱스는 하루 24시간 단위의 `시계열 인덱스`이다. 그러므로, 하루가 지난 로그 인덱스는 `읽기 전용 인덱스`가 되고, 이는 `force-merge`를 통해 `1개의 단일 세그먼트`로 관리 가능해진다.  

* * * 
`최종 결과`  
|결과 사진|force-merge 적용 전 후 여부|profile 종류|소요 시간|
|:---:|:---:|:---:|:---:|
|![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/cba4f2ef-d0d3-4715-97a2-fd8f3af2d85f)|전|Query|0.329ms|
|![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/8a8e4cd3-0ce9-447c-83f8-20e68b2ce003)|전|Aggregation|0.141ms|
|![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/99032a57-5d0c-42f7-8cc7-4465a6bbec3b)|후|Query|0.113ms|
|![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/d1ec14ff-e088-4c05-9e75-163ee2c0d266)|후|Aggregation|0.030ms|  

force-merge 시행 전과 후의 7일간 인기 검색어 조회 시, 2023년 12월 27일의 조회 성능 결과를 비교하였다. 그 결과, `0.47ms -> 0.14ms` 로 성능 개선을 이뤘다.  


## 추가 구현 기능

### 1. ALB를 통한 포트 기반 라우팅
> 목표 : 포트에 따라 다른 대상 그룹으로 매핑

V사의 데이터 포털 인스턴스와 Airflow 인스턴스를 모두 회사 내부망에서 관리할 수 있도록 private subnet에 배치시켰다.  
구식 CLB과는 다르게 ALB는 로드 밸런서 하나만으로 각 대상그룹에 라우팅 시켜준다.  
`path`뿐만 아니라 `port` 에 따라 대상그룹을 매핑시켜 줄 수 있는 장점이 있기에 활용한 점이 크다. 이는 도커 컨테이너 환경에서 아주 유용하게 작동할 수 있고 하나의 대상그룹에 더 많은 컨테이너를 넣어 비용을 최적화할 수 있기 때문이다.

## 향후 개선 사항

### 1. 삭제된 행에 대한 동기화
현재 DB내의 삭제 데이터는 감지 못하는 단점이 존재한다. 해당 문제 처리를 위해 다음 접근법을 고려할 수 있다.

- `is_deleted` 필드를 활용한 소프트 삭제를 시행하는 방법이다. ES와 DB 쿼리는 `is_deleted`가 참인 레코드/문서를 제외하기 위해 작성되어야 한다.

- PostgreSQL의 레코드 삭제를 담당하는 모든 시스템이 그 뒤에 명령을 실행하여 직접 ES 내의 문서를 삭제하도록 하는 것이다.

### 2. 캐싱을 통한 실시간 검색어 순위 결과 개선
단순히 7일간의 Index에 대한 aggs 연산이 아닌, 짧은 시간 내의 검색 키워드를 캐싱하여 이를 함께 활용한다면 인기 검색어 결과가 더욱 정확해질 것으로 예상된다.  

## 느낀점
- 단순히 ElasticSearch 기능들을 사용하는 걸로 끝이 아닌, 실제 성능 개선을 어떤식으로 할지 고민하면서 구현해보니, 알고있던 개념을 더욱 깊게 이해할 수 있었다.
- `검색` 도메인에 흥미가 생겼고, 사용자 입장에서 원하는 결과를 더욱 빠르고 정확하게 도출해내기 위해 `클러스터, 노드`와 같이 더 넓은 단위의 개념 학습 및 성능 최적화에 힘쓸 예정이다.


## 참고 사항
- 회사 프로젝트의 접근 권한은 private이기 때문에, 제 로컬 repo에 보이도록 하기 위해 `main 브랜치만 가져온 상태`입니다.
- 저의 로컬 repo에서는 Github Actions의 Deploy 실패 문구가 보이지만, `실제 현업에서는 정상작동` 하고 있습니다.

![image](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/787fcdc3-686f-4363-9066-adcf37970793)
*현업에서 사용되었던, 혹은 사용중인 브랜치명 목록들*  


![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/9ad17317-6407-44bd-94a2-d7e9779d7a99)  
*정상 작동한 Github Actions의 Workflows 이력*

## 참고 자료
- [NHN FORWARD 22 - 엘라스틱서치를 이용한 상품 검색 엔진 개발 일지](https://www.youtube.com/watch?v=fBfUr_8Pq8A)
- [ElasticSearch 동의어 사전](https://icarus8050.tistory.com/49)
- [Elasticsearch copy_to 활용](https://1995-dev.tistory.com/155)
- [elasticsearch term match 비교](https://cupeanimus.tistory.com/66)
- [엘라스틱서치 샤딩, 이 정도는 알고 사용하자](https://jinseong-dev.tistory.com/entry/ELK-%EC%97%98%EB%9D%BC%EC%8A%A4%ED%8B%B1%EC%84%9C%EC%B9%98-%EC%83%A4%EB%94%A9-%EC%9D%B4-%EC%A0%95%EB%8F%84%EB%8A%94-%EC%95%8C%EA%B3%A0-%EC%82%AC%EC%9A%A9%ED%95%98%EC%9E%90)
- [내가 운영하는 Elasticsearch 클러스터에 얼마나 많은 샤드가 필요할까?](https://www.elastic.co/kr/blog/how-many-shards-should-i-have-in-my-elasticsearch-cluster)
- [ElasticSearch 검색 성능 최적화](https://velog.io/@ehwnghks/Elasticsearch-%EA%B2%80%EC%83%89-%EC%84%B1%EB%8A%A5-%EC%B5%9C%EC%A0%81%ED%99%94)
- [엘라스틱 서치 구성 요소 및 구조](https://jeongxoo.tistory.com/17)

