## V사의 데이터와 서비스를 한 곳에서 제공하고 통합하는 웹 기반의 플랫폼
> 최종목표 : V사의 모든 데이터(정형, 비정형)를 통합하고 표준화하여 비상교육 임직원들에게 데이터를 손쉽게 검색 및 제공하기 위한 서비스

 
![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/367266cb-9af1-4253-8b22-163d991b42cd)


## 목차
- [포털의 목적](#포털의-목적)
- [포털의 기능](#포털의-기능)
- [사용 기술 스택](#사용-기술-스택)
- [ERD](#erd)
- [Architecture](#Architecture)
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



## 주요 구현 기능
### 1. 추천 검색어 조회
> 목표 : 검색어 입력 시, 다음과 같이 추천 검색어 보여주기  
![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/3ea332ee-d4c6-4f8b-99b3-9158310c5e0a)

- 메타 데이터 -> 테이블ID, 코멘트, 카테고리로 분류
- 검색 대상은 `카테고리(하위 주제)` 로 설정

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
|한글 형태소|![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/1c2bdcea-0e0b-4cea-96e4-eb73ba02fd21)|`월별매출 및 조정내역`을 Nori Tokenizer를 이용하여 `월별`, `매출`, `및`, `조정`, `내역`으로 토큰화하였으므로, `월별`로 검색하였을 때 해당 결과를 얻기 가능|
|오타 교정|![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/0906c8b6-f290-4df2-b558-b773e654eef8)|`교자` 검색 시, Fuzzy Query를 통해 `교재`라는 교정된 검색어를 추천|
|초성 검색|![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/be7a792a-ef56-489f-a3af-5289eee1be57)|hanhinsam 초성 플러그인을 통해, `ㄱㅈ`으로 토큰화되는 카테고리를 모두 추천|
|영한 변환|![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/6f7869b3-ca5d-4de5-9082-5f6a8c60045d)|hanhinsam 영한 변환 플러그인을 통해, `CMS`의 한글 결과인 `츤`이 토큰으로 저장됨|
|자동완성|![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/7eb1376a-88f0-4e74-bd73-e32ae5ef6cff)|`교재` |



### 2. 인기 검색어 조회


## 검색 품질 및 성능 개선


### 품질 개선

1. 동의어, 사용자 사전 도입

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

