# 비상교육의 데이터와 서비스를 한 곳에서 제공하고 통합하는 웹 기반의 플랫폼, VISANG Dataportal Service
> 최종목표 : 비상교육의 모든 데이터(정형, 비정형)를 통합하고 표준화하여 비상교육 임직원들에게 데이터를 손쉽게 검색 및 제공하기 위한 서비스

![image](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/a018f734-9114-4330-a257-0090f28e9a91)

## 포털의 기능

![image](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/5cd08a25-b7d5-4d38-bf50-e588cab52f0b)

## 사용 기술 스택
- `Language` : Java 11, JUnit 4
- `Framework` : SpringBoot 2.7.14, MyBatis
- `Database` : PostgreSQL 42.5.0, AWS RDS
- `Deploy` : Github Actions, AWS S3 & CodeDeploy
- `API Docs` : Swagger 3.0.0
- `Logging` : Logback, Logstash
- `ElasticSearch` : RestHighLevelClient, Query DSL API
- `Controller Testing` : Mockito

## ERD
![image](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/cb38098c-ac34-40f5-9c1f-11ce7010658e)

## 나의 주요 구현 기능

### 1. 메타 데이터 검색 
![es_before_gif](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/ae236471-1955-406e-acb8-abd805fc2d99)
*ElasticSearch 도입 전*  

  

![es_after_gif](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/44dacf5a-a735-4dcf-88c0-0f5015356b02)
*ElasticSearch 도입 후*

- 메타 데이터 검색에서, `한글로 구성된 검색 기준`에 `역색인화`를 도입한 빠른 검색 기능 구현을 위해, ElasticSearch의 한글 형태소 분석기인 `Nori Tokenizer` 를 도입
- `문제은행` 키워드로 검색 시, 도입 전에는 

### 2. 실시간 검색어 순위

### 3. Filter 기반 XSS 공격 방지

### 4. ControllerAdvice를 이용한 예외처리

### 5. Mockito 기반 Controller 테스팅
