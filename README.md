# VISANG Dataportal Web Service
## 비상교육의 데이터와 서비스를 한 곳에서 제공하고 통합하는 웹 기반의 플랫폼
> 최종목표 : 비상교육의 모든 데이터(정형, 비정형)를 통합하고 표준화하여 비상교육 임직원들에게 데이터를 손쉽게 검색 및 제공하기 위한 서비스

![image](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/a018f734-9114-4330-a257-0090f28e9a91)

접속 URL : http://43.201.105.114:3000/

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

- 메타 데이터 검색 기준 중, `한글로 구성된 테이블 설명 및 하위주제`에 `역색인화`를 도입한 빠른 검색 기능 구현을 위해, ElasticSearch의 한글 형태소 분석기인 `Nori Tokenizer` 를 도입
- `문제은행` 키워드로 검색 시, 도입 전에는 해당 문자열이 그대로 포함된 결과값만 나왔지만 도입 이후에는 `문제`, `은행` 과 같이 더 세밀한 단위까지 나뉘어져 문자열 검색을 수행한 결과를 반환해준다.

### 2. 실시간 검색어 순위

### 3. Filter 기반 XSS 공격 방지

### 4. ControllerAdvice를 이용한 예외처리

### 5. Mockito 기반 Controller 테스팅

## 향후 개선 사항
### 1. EC2 인스턴스에 ELK 플랫폼 성공적으로 연결
- 로컬 환경에서는 ELK 플랫폼을 성공적으로 구현하였지만, EC2 인스턴스에서는 docker-compose 사용으로 인한 서버 용량 초과 문제 발생으로 서버 환경이 급격하게 느려지는 현상이 발생하여 아직 도입에 성공하지 못하였다.
- 현재 `t3a.medium` 요금제에서 더 높은 성능의 요금제로 수정하거나 AWS의 OpenSearch 서비스를 활용해보는 방법을 도입해볼 예정이다.
