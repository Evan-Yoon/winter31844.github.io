---
title: '[AWS CLF-C02][5회차] Database 핵심 정리'
slug: aws-clf-c02-day5-database-core
date: 2026-01-20
author: Evan Yoon
category: study
subcategory: certification
description: AWS Certified Cloud Practitioner(CLF-C02) 스터디 5회차 기록. RDS와 Aurora, DynamoDB,
  ElastiCache, Redshift의 차이와 각 서비스의 목적을 시험 관점과 실제 사용 관점에서 자세히 정리했다.
thumbnail: /images/posts/aws-clf-c02-series/cover.png
tags:
  - aws
  - clf-c02
  - cloud-practitioner
  - rds
  - aurora
  - dynamodb
  - elasticache
  - redshift
  - database
  - study
  - certification
readTime: 22
series: AWS CLF-C02
seriesOrder: 6
featured: false
draft: false
toc: true
---

## 이번 회차에서 다룬 내용

2026년 1월 20일 화요일, AWS Certified Cloud Practitioner(CLF-C02) 스터디 5회차를 진행했다. 이날의 주제는 **Database 핵심**이었다.

발표자 배정표 기준으로 5회차 범위는 다음과 같았다.

- A(대산): RDS
- B(대산): Aurora
- C(지현): DynamoDB
- D(지현): ElastiCache / Redshift

이번 회차는 앞선 Storage 파트와 매우 밀접하게 연결되어 있었다. 4회차에서 "어떤 저장소를 어떤 방식으로 쓸 것인가"를 배웠다면, 5회차에서는 "그 저장소를 데이터베이스라는 형태로 어떻게 활용할 것인가"를 배우는 느낌이었다.

특히 이번 회차는 단순 서비스 설명을 넘어서, **데이터 성격에 따라 어떤 데이터베이스를 선택할 것인가**라는 관점이 중요했다.

---

## 왜 데이터베이스 파트가 중요한가

Cloud Practitioner를 공부하다 보면 서비스 이름이 많아서 헷갈린다. 특히 데이터베이스 파트는 더 그렇다.

- RDS
- Aurora
- DynamoDB
- ElastiCache
- Redshift

이름은 모두 다르지만, 처음에는 전부 "데이터 저장하는 것"처럼 느껴진다. 그런데 실제로는 역할이 완전히 다르다.

예를 들면:

- 전통적인 애플리케이션용 관계형 DB가 필요한가
- 더 빠르고 확장성 높은 관계형 DB가 필요한가
- NoSQL 기반의 초고속 키-값 저장소가 필요한가
- DB 앞단에서 읽기 성능을 높이는 캐시가 필요한가
- 대규모 분석과 리포트를 위한 데이터 웨어하우스가 필요한가

이 질문에 따라 정답이 달라진다.

즉, 이번 회차의 핵심은 "데이터베이스 이름을 외우는 것"이 아니라 **데이터 성격, 접근 패턴, 목적에 따라 어떤 서비스를 고를지 판단하는 것**이었다.

---

## 먼저 큰 그림: 이번 회차 서비스들의 역할

아주 짧게 줄이면:

- **RDS**: 관리형 관계형 데이터베이스
- **Aurora**: AWS가 만든 고성능 관계형 데이터베이스
- **DynamoDB**: 서버리스 NoSQL 데이터베이스
- **ElastiCache**: 메모리 기반 캐시 계층
- **Redshift**: 분석용 데이터 웨어하우스

즉, 다 같은 DB처럼 보여도 실제로는:

- 운영형 DB
- 고성능 운영형 DB
- NoSQL
- 캐시
- 분석형 DB

로 역할이 갈린다.

---

## RDS란 무엇인가

Amazon RDS(Relational Database Service)는 AWS가 제공하는 **관리형 관계형 데이터베이스 서비스**다.

관계형 데이터베이스(RDBMS)를 직접 서버에 설치하고 패치하고 백업하는 대신, AWS가 많은 운영 부담을 줄여주는 형태라고 생각하면 된다.

### 쉽게 이해하면

직접 EC2 위에 MySQL, PostgreSQL, MariaDB 같은 DB를 설치해서 운영할 수도 있다. 하지만 그러면:

- 설치
- 패치
- 백업
- 장애 대응
- 모니터링

같은 작업을 직접 많이 해야 한다.

RDS는 이런 부담을 줄여준다.

즉, **DB 엔진은 익숙한 관계형 DB를 쓰되, 운영은 AWS가 많이 도와주는 구조**다.

---

## RDS가 적합한 경우

RDS는 전통적인 애플리케이션 데이터베이스에 잘 맞는다.

예:

- 사용자 정보 저장
- 주문/결제 데이터
- 게시판, 서비스 운영 데이터
- 트랜잭션 기반 애플리케이션

즉, SQL 기반의 구조화된 데이터, 관계, 조인, 트랜잭션이 중요한 경우 RDS가 기본 선택지가 되기 쉽다.

---

## RDS의 핵심 장점

Cloud Practitioner 수준에서 RDS를 이해할 때 중요한 포인트는 다음과 같다.

### 1. 관리형 서비스

직접 OS와 DB를 모두 운영하는 것보다 관리 부담이 적다.

### 2. 백업과 복구 기능

자동 백업, 스냅샷 같은 기능을 통해 데이터 보호를 쉽게 할 수 있다.

### 3. Multi-AZ 배포

고가용성을 위해 여러 AZ를 활용한 구성이 가능하다.

### 4. Read Replica

읽기 부하를 분산하기 위한 읽기 전용 복제본 개념과 연결된다.

시험에서는 특히:

- Multi-AZ = 고가용성
- Read Replica = 읽기 성능 확장

이 둘을 구분하는 문제가 자주 나온다.

---

## RDS에서 자주 헷갈리는 것: Multi-AZ vs Read Replica

이건 Cloud Practitioner에서 매우 자주 나오는 비교 포인트다.

### Multi-AZ

- 목적: 고가용성
- 장애 발생 시 빠른 전환
- 주 DB를 보호하는 구조에 가깝다

### Read Replica

- 목적: 읽기 성능 확장
- 읽기 요청 분산
- 보고서 조회나 읽기 부하가 많은 경우에 유리

즉:

- 장애 대응이 중요하다 → Multi-AZ
- 읽기 트래픽이 많다 → Read Replica

이렇게 구분하면 된다.

---

## Aurora란 무엇인가

Amazon Aurora는 AWS가 만든 **고성능 관계형 데이터베이스 엔진**이다.

관계형 DB라는 점에서는 RDS와 같은 계열로 볼 수 있지만, Aurora는 단순히 "RDS의 또 다른 옵션" 정도로 보면 부족하다. AWS가 클라우드 환경에 맞게 설계한 더 고도화된 DB라고 이해하는 편이 맞다.

### Aurora를 쉽게 이해하면

- MySQL 또는 PostgreSQL과 호환되면서
- AWS 환경에서 더 높은 성능과 가용성을 지향하는
- 관계형 데이터베이스

라고 볼 수 있다.

즉, 관계형 DB의 친숙함을 유지하면서 클라우드 최적화를 더 강하게 가져간 서비스다.

---

## Aurora가 강조되는 이유

시험에서 Aurora가 자주 나오는 이유는 "RDS보다 조금 더 빠른 DB" 수준이 아니라, **고성능과 고가용성이 함께 강조되는 AWS 고유 서비스**이기 때문이다.

### Aurora의 일반적인 특징

- MySQL/PostgreSQL 호환
- 고성능
- 높은 가용성
- AWS 관리형

Cloud Practitioner 수준에서는 Aurora 내부 구조를 깊게 파기보다, "관계형 DB가 필요하지만 더 높은 성능과 가용성을 원할 때 AWS가 제시하는 선택지" 정도로 이해하면 좋다.

---

## RDS와 Aurora를 어떻게 구분할까

이 둘은 자주 함께 묶여 나온다.

### RDS

- 관리형 관계형 DB 서비스라는 큰 범주
- 익숙한 관계형 엔진을 AWS에서 운영 쉽게 쓰는 느낌

### Aurora

- AWS가 설계한 고성능 관계형 DB
- MySQL/PostgreSQL 호환
- 더 높은 성능/가용성 포지션

시험에서는:

- 일반적인 관리형 관계형 DB → RDS
- 더 고성능, AWS 고유의 최적화된 관계형 DB → Aurora

처럼 생각하면 정리가 된다.

---

## DynamoDB란 무엇인가

이제부터는 관계형 DB에서 벗어나 **NoSQL** 영역으로 넘어간다.

Amazon DynamoDB는 AWS의 **완전관리형 서버리스 NoSQL 데이터베이스**다.

자료에서도 첫 장부터 강조된 키워드가 있었다.

- Serverless
- NoSQL
- Key-Value
- 낮은 지연 시간

즉, DynamoDB는 전통적인 SQL 관계형 구조와는 다른 방식으로 빠르고 유연하게 동작하는 DB다.

---

## DynamoDB를 어떻게 이해해야 하나

가장 쉬운 출발점은 이 질문이다.

"관계형 DB처럼 복잡한 조인과 스키마가 꼭 필요한가?"

만약 아니고,

- 단순한 키 기반 조회가 많고
- 매우 빠른 응답이 필요하고
- 서버 관리 없이 운영하고 싶다면

DynamoDB가 훨씬 잘 맞을 수 있다.

### 자료에서 강조된 핵심

- 완전관리형
- 서버리스
- Key-Value 중심
- 한 자릿수 밀리초 응답

즉, Cloud Practitioner 관점에서 DynamoDB는 **빠르고, 유연하고, 서버 관리가 거의 필요 없는 NoSQL 서비스**다.

---

## DynamoDB의 핵심 개념

### Partition Key

자료 콘솔 예시에서도 나왔듯이, DynamoDB는 기본 키 설계가 중요하다. 관계형 DB처럼 자유로운 조인보다, 어떻게 키를 잡느냐가 성능과 구조에 큰 영향을 준다.

### On-demand vs Provisioned

자료에서 특히 강조된 부분 중 하나였다.

- On-demand: 사용량 예측이 어려울 때, 쓴 만큼 비용
- Provisioned: 읽기/쓰기 용량을 미리 잡는 방식

시험에서는 이 차이를 자주 묻는다.

즉:

- 트래픽이 일정하지 않다 → On-demand
- 트래픽 패턴이 예측 가능하고 비용 최적화가 중요하다 → Provisioned

로 정리하면 된다.

### Global Tables

전 세계 리전에 걸쳐 데이터를 복제하는 개념과 연결된다. 이건 DynamoDB가 글로벌 환경에서도 자주 언급되는 이유 중 하나다.

---

## DynamoDB Accelerator(DAX)

자료에는 DAX도 함께 등장했다.

DAX(DynamoDB Accelerator)는 DynamoDB 전용 캐시라고 이해하면 된다.

핵심은:

- DynamoDB 읽기 성능을 더 높이고 싶고
- 코드를 크게 바꾸지 않고 접근하고 싶을 때

떠올릴 수 있다는 점이다.

Cloud Practitioner 시험에서는 DAX까지 깊게 묻지 않더라도, "DynamoDB 전용 성능 가속" 정도로 알고 있으면 정리에 도움이 된다.

---

## ElastiCache란 무엇인가

Amazon ElastiCache는 **인메모리 캐시 서비스**다.

이 서비스는 독립적인 메인 DB라기보다, 보통 **DB 앞단에서 성능을 높이기 위한 캐시 계층**으로 이해하는 것이 좋다.

쉽게 말하면:

- 자주 조회되는 데이터를
- 디스크 기반 DB에 매번 가지 않고
- 메모리(RAM)에 올려두고 빠르게 꺼내 쓰는 방식

이다.

### 왜 캐시가 필요한가

데이터베이스에 반복 조회가 몰리면 응답 속도와 비용 모두 부담이 커질 수 있다.

캐시는 이런 상황에서:

- 읽기 부하를 줄이고
- 응답 속도를 높이고
- 전체 애플리케이션 성능을 개선한다

는 역할을 한다.

---

## Redis vs Memcached

자료에서도 가장 먼저 이 둘을 비교하고 있었다.

### Redis

- 더 다양한 기능
- 백업 가능
- Multi-AZ
- 정렬 집합(Sorted Sets) 같은 구조 지원

즉, 기능이 많고 조금 더 고도화된 캐시/데이터 구조 용도로 자주 연결된다.

### Memcached

- 더 단순한 캐시
- 단순 키-값 저장
- 데이터가 날아가도 괜찮은 경우에 적합

즉, "단순하고 가볍게 캐시"하는 쪽에 더 가깝다.

시험에서는:

- 랭킹, 복잡한 자료구조, 고가용성, 백업 → Redis
- 단순 캐시 → Memcached

로 기억하면 도움이 된다.

---

## ElastiCache의 핵심 포인트

이 서비스는 "DB를 대체한다"기보다 **DB 앞단 성능 최적화 계층**으로 이해하는 것이 좋다.

예:

- 자주 보는 상품 정보
- 세션 데이터
- 랭킹 데이터
- 반복 조회되는 API 결과

이런 것들을 메모리에 올려두면 훨씬 빠르게 응답할 수 있다.

즉, ElastiCache는 데이터의 원본 저장소라기보다 **속도를 위한 보조 계층**이다.

---

## Redshift란 무엇인가

Amazon Redshift는 AWS의 **데이터 웨어하우스 서비스**다.

이건 운영형 DB와는 목적 자체가 다르다.

쉽게 말하면:

- 앱이 실시간으로 주문을 저장하는 DB가 아니라
- 많은 데이터를 모아두고
- 분석, 집계, 리포트, BI 용도로 조회하는 DB

에 가깝다.

### 핵심 키워드

자료에서도 분명하게 나왔다.

- Data Warehouse
- OLAP
- Columnar Storage
- BI Tools

즉, Redshift는 분석을 위한 DB다.

---

## 왜 Redshift가 따로 필요한가

운영형 DB(RDS 등)는 트랜잭션 중심의 작업에 강하다.

예:

- 회원 가입
- 주문 생성
- 결제 기록
- 게시글 작성

반면 분석형 작업은 성격이 다르다.

예:

- 월별 매출 집계
- 지역별 사용자 수 분석
- 여러 기간 데이터를 합산해 리포트 생성

이런 작업은 대량 데이터에 대한 집계와 분석이 많다. Redshift는 이런 용도에 맞춰 설계된 서비스다.

---

## Redshift의 핵심 특징

### 1. 분석용(OLAP)

운영형 OLTP보다 분석형 OLAP 맥락에서 이해하는 것이 중요하다.

### 2. Columnar Storage

자료에서도 강조되었듯이, Redshift는 열 기반 저장을 통해 집계와 분석에 유리하다.

예를 들어 특정 컬럼의 합계, 평균 같은 작업을 빠르게 처리하기 좋다.

### 3. BI 도구와 연결

분석 결과를 시각화하거나 리포팅하는 흐름과 잘 연결된다.

### 4. S3와의 연계

자료에는 Redshift Spectrum도 등장했다. 이는 S3에 있는 데이터를 Redshift로 직접 로드하지 않고도 쿼리할 수 있는 기능으로 정리되어 있었다.

Cloud Practitioner 수준에서는 "분석과 S3 연계" 정도로 기억하면 충분하다.

---

## 다섯 서비스를 한 번에 비교해보기

### RDS

- 관계형 DB
- 관리형
- 전통적인 애플리케이션 운영 DB

### Aurora

- 고성능 관계형 DB
- AWS 최적화
- MySQL/PostgreSQL 호환

### DynamoDB

- NoSQL
- 서버리스
- 키-값 중심
- 초고속 응답

### ElastiCache

- 인메모리 캐시
- 속도 향상용
- Redis / Memcached

### Redshift

- 데이터 웨어하우스
- 분석/리포트용
- 컬럼 기반 저장

즉:

- 운영형 관계형 → RDS / Aurora
- 운영형 NoSQL → DynamoDB
- 캐시 → ElastiCache
- 분석형 → Redshift

로 정리하면 훨씬 덜 헷갈린다.

---

## 시험에서 자주 나오는 선택 패턴

### SQL 기반 운영 DB가 필요하다

→ RDS

### 더 높은 성능과 AWS 최적화된 관계형 DB가 필요하다

→ Aurora

### 키-값 NoSQL, 서버리스, 낮은 지연 시간이 필요하다

→ DynamoDB

### 읽기 성능을 높이고 DB 부하를 줄이고 싶다

→ ElastiCache

### 대량 집계와 분석, BI 리포트가 필요하다

→ Redshift

시험은 결국 이런 사용 사례를 보고 적절한 서비스를 연결하게 만든다.

---

## 이번 회차에서 남겨둘 포인트

이번 회차를 하면서 가장 크게 느낀 건, 데이터베이스도 결국 **"저장"이 아니라 "사용 방식"에 따라 달라진다**는 점이었다.

예를 들어:

- 정합성과 조인이 중요하다 → 관계형
- 초고속 단순 조회가 중요하다 → NoSQL
- 읽기 속도 보완이 필요하다 → 캐시
- 분석과 리포트가 목적이다 → 데이터 웨어하우스

즉, 데이터베이스 선택은 기술 취향 문제가 아니라 **업무 성격과 접근 패턴을 보는 문제**다.

그리고 이 관점은 Cloud Practitioner 시험에서도 그대로 반영된다. 기능 설명을 길게 외우기보다, 요구사항을 보면 어느 쪽 계열인지 먼저 떠오를 수 있어야 한다.

---

## 시험 전에 다시 볼 포인트

짧게 압축하면:

- RDS는 관리형 관계형 데이터베이스다.
- Aurora는 AWS 고성능 관계형 데이터베이스다.
- DynamoDB는 서버리스 NoSQL DB다.
- ElastiCache는 인메모리 캐시 서비스다.
- Redshift는 분석용 데이터 웨어하우스다.
- Multi-AZ와 Read Replica는 목적이 다르다.
- DynamoDB는 On-demand와 Provisioned 용량 모드를 구분해야 한다.
- Redis와 Memcached는 기능 범위가 다르다.
- Redshift는 OLAP와 컬럼 기반 저장을 떠올리면 된다.

5회차는 Cloud Practitioner 전체 흐름에서도 중요한 축이었다. 여기까지 오면 AWS가 단순히 서버와 스토리지만 제공하는 것이 아니라, **데이터의 성격에 따라 최적화된 관리형 서비스들을 조합해 쓰는 플랫폼**이라는 점이 더 선명해진다.

---

## 자료 다운로드

reference 폴더 기준으로 5회차와 직접 연결되는 자료 파일은 아래 3개였다. 복습용으로 바로 내려받을 수 있게 정적 경로로 옮겨두었다.

- [RDS & Aurora.pptx](/downloads/aws-clf-c02/2026-01-20/RDS%20%26%20Aurora.pptx)
  5회차 앞 파트 발표 자료. RDS와 Aurora 중심.

- [05_Database 심화_지현.html](/downloads/aws-clf-c02/2026-01-20/05_Database%20%EC%8B%AC%ED%99%94_%EC%A7%80%ED%98%84.html)
  DynamoDB, ElastiCache, Redshift 중심의 발표 자료.

- [05_Database 심화_add_지현.pptx](/downloads/aws-clf-c02/2026-01-20/05_Database%20%EC%8B%AC%ED%99%94_add_%EC%A7%80%ED%98%84.pptx)
  5회차 보강 자료.

다음 글에서는 1월 22일 6회차 주제였던 Network & Delivery, 즉 VPC, Route 53, Load Balancer, CloudFront를 정리할 예정이다.
