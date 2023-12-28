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

![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/9c8cc649-a91c-4e33-976e-ab602aff014a)

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
![image](https://github.com/shinsj4653/vs-data-portal-backend/assets/49470452/9184b4c1-bdce-4950-b91a-e037c20346b4)


## 주요 구현 기능
### 1. 추천 검색어 조회


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

