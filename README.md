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

`구현 1. 동의어 사전`  

![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/6dee5f4a-7965-4301-b778-f723e1419485)  
*Analyzer의 토큰화 절차*

각 Document의 field는 `Analyzer`를 통해 토큰화가 되는데, `Tokenizer`를 통해 먼저 나눠지고, `Filter`를 통해 토큰화된 결과들을 변경하거나 추가, 삭제해준다.
동의어 처리를 위해서는, ES에서 제공하는 `synonym` 필터를 사용하면 된다.  
 

기본적으로 filter영역 내 synonyms 항목에서 직접 동의어를 입력하는 방법과 `동의어 사전 파일을 만들어 synonyms_path로 지정하는 방법`이 있다. 동의어 사전의 양과 잦은 수정을 고려하여 두 번째 방법을 택하기로 하였다.  


메타 데이터의 테이블 id는 tab_tt_test 와 같이 영어 단어들이 underscore로 구분된 형태이다. 이미 underscore를 기준으로 테이블 id 값을 토큰화하였기 때문에, 분리된 영어단어들에 해당하는 동의어들을 정의해주면 검색 품질을 더욱 개선할 수 있을거라 생각하였다.  

“시험”과 “TEST”가 동의어로 인식되기 위해 다음 형식으로 파일 내용을 구성하였다.  
```text
시험, TEST
```  
쉼표와 공백으로 둘을 구분하면, 서로가 서로의 동의어로 인식된다. 모든 동의어들은 회사의 `표준단어정의서` 사전을 참고하여 만들었다. 공통표준단어명과 공통표준단어영문약어명을 등록함으로써, 한글 검색 시 사전을 기준으로 같은 의미로 해석되는 영어 단어명이 함께 검색되도록 하였다.  

`구현 2. 사용자 사전`  

synonym 필터 사용 시, 다음과 같은 오류가 발생하였다.  

```text
"term: 가맹점 analyzed to a token (가맹) with position increment != 1 (got: 0)"
```
즉, 이미 nori tokenizer가 `가맹점`을 `가맹`과 `점`으로 토큰화 하였지만, 동의어 사전에는 여전히 `가맹점`이 존재하기 때문에 생기는 오류였다.  
이를 해결하기 위해 nori tokenizer의 `user_dictionary` 옵션을 사용하여, 토큰화 되지 않을 단어들을 정의해줬다.  
표준단어정의서의 공통표준단어명(한글)들을 모두 등록해줌으로써 해당 오류를 해결할 수 있었다.  

> 최종결과


2. 메타 데이터 검색 쿼리 개선

### 성능 개선

1. ES와 RDBMS 동기화 statement 튜닝


2. primary, replica 샤드 개수 조정


3. 검색 로그 Index - force merge


## 추가 구현 기능

### 1. ALB를 통한 포트 기반 라우팅


## 향후 개선 사항

### 1. 삭제된 행에 대한 동기화


### 2. 캐싱을 통한 실시간 검색어 순위 결과 개선



## 참고 사항
- 회사 프로젝트의 접근 권한은 private이기 때문에, 제 리포지토리에 보이도록 하기 위해 `main 브랜치만 가져온 상태`입니다.
- 제 레포에서는 Github Actions의 Deploy 실패 문구가 보이지만, `실제 현업에서는 정상작동` 하고 있습니다.

![image](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/787fcdc3-686f-4363-9066-adcf37970793)
*현업에서 사용되었던, 혹은 사용중인 브랜치명 목록들*  



![image](https://github.com/shinsj4653/vs-data-service-backend/assets/49470452/811ab1df-f5c6-4c97-9fd3-b5f73935c673)
*정상 작동한 Github Actions의 Workflows 이력*

