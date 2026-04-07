---
title: '[AWS CLF-C02][6회차] Network & Delivery 핵심 정리'
slug: aws-clf-c02-day6-network-delivery
date: 2026-01-22
author: Evan Yoon
category: study
subcategory: certification
description: AWS Certified Cloud Practitioner(CLF-C02) 스터디 6회차 기록. VPC, Security Group와
  NACL, Route 53, Load Balancer(ALB/NLB), CloudFront를 한 흐름으로 자세히 정리했다.
thumbnail: /images/posts/aws-clf-c02-series/cover.png
tags:
  - aws
  - clf-c02
  - cloud-practitioner
  - vpc
  - route53
  - load-balancer
  - cloudfront
  - network
  - study
  - certification
readTime: 22
series: AWS CLF-C02
seriesOrder: 7
featured: false
draft: false
toc: true
---

## 이번 회차에서 다룬 내용

2026년 1월 22일 목요일, AWS Certified Cloud Practitioner(CLF-C02) 스터디 6회차를 진행했다. 이번 회차 주제는 **Network & Delivery**였다.

발표자 배정표 기준으로 6회차 범위는 다음과 같았다.

- A(현석): VPC 기본 개념
- B(현석): Route 53 (DNS)
- C(지호): Load Balancer (ALB / NLB)
- D(지호): CloudFront (CDN)

이번 회차는 앞에서 배운 거의 모든 개념이 한 번에 이어지는 느낌이 있었다.

- 컴퓨팅 회차에서 EC2를 배웠고
- 스토리지 회차에서 S3와 EFS를 배웠고
- 글로벌 인프라 회차에서 Region, AZ, Edge Location을 배웠다

이제 6회차에서는 그 자원들이 **어떤 네트워크 안에 놓이고, 사용자는 어떤 이름으로 접근하며, 요청은 어디서 분산되고, 콘텐츠는 어디서 빨라지는가**를 한 번에 정리하게 된다.

Cloud Practitioner 전체 흐름에서 보면 이번 회차는 굉장히 중요하다. 서비스 이름을 따로 외우는 단계가 아니라, 사용자의 요청이 AWS 안에서 어떻게 흐르는지를 그리는 회차이기 때문이다.

---

## 왜 Network & Delivery가 중요한가

애플리케이션은 서버만 있다고 끝나지 않는다.

예를 들어 웹 서비스를 운영한다고 하면 이런 질문이 생긴다.

- 서버를 어느 네트워크 안에 둘 것인가
- 외부 인터넷과 연결되는 서버와 내부 전용 서버를 어떻게 나눌 것인가
- 사용자가 `example.com`을 입력했을 때 어디로 가게 할 것인가
- 여러 서버 중 누가 요청을 받을 것인가
- 전 세계 사용자에게 콘텐츠를 더 빠르게 주려면 어떻게 할 것인가

이 질문을 풀어주는 것이 이번 회차 서비스들이다.

즉:

- VPC는 네트워크 공간을 만들고
- Route 53은 이름을 주소로 바꾸고
- Load Balancer는 요청을 적절한 서버로 보내고
- CloudFront는 콘텐츠 전달을 더 빠르게 한다

이 흐름을 이해하면 AWS 서비스 구조가 훨씬 입체적으로 보인다.

---

## 6회차를 한 문장으로 요약하면

이번 회차를 아주 짧게 요약하면 이렇다.

- **VPC**: 서버들이 사는 네트워크 공간
- **Route 53**: 사용자가 입력한 도메인을 올바른 목적지로 연결하는 DNS
- **Load Balancer**: 여러 서버로 트래픽을 분산하는 장치
- **CloudFront**: 사용자 가까운 곳에서 콘텐츠를 빠르게 전달하는 CDN

즉, 사용자가 서비스에 접근하는 전체 경로를 구성하는 서비스들이다.

---

## VPC란 무엇인가

Amazon VPC(Virtual Private Cloud)는 AWS 안에서 만드는 **나만의 독립된 가상 네트워크**다.

쉽게 말하면:

- AWS 클라우드 안에
- 내가 쓰는 전용 네트워크 공간을 하나 만들고
- 그 안에 EC2, 데이터베이스, 로드 밸런서 같은 자원을 배치하는 구조

라고 볼 수 있다.

### 왜 VPC가 필요한가

모든 리소스를 그냥 인터넷에 노출시키면 보안도 불안하고, 구조도 복잡해진다. 실제 서비스는 보통:

- 외부에 공개되는 영역
- 내부에서만 쓰는 영역

을 분리해야 한다.

VPC는 바로 그 분리의 출발점이다.

---

## VPC의 핵심 구성 요소

자료에서도 강조됐듯, VPC를 이해할 때 같이 봐야 하는 것은 다음 요소들이다.

### 1. CIDR 블록

VPC를 만들 때 IP 주소 범위를 정한다. 즉, 이 네트워크 안에서 어떤 사설 IP 대역을 쓸지 미리 정하는 것이다.

Cloud Practitioner에서는 CIDR 계산을 깊게 하진 않더라도, **VPC는 생성 시 IP 주소 범위를 가진다**는 점을 기억해두는 것이 좋다.

### 2. 서브넷(Subnet)

VPC는 다시 더 작은 네트워크 구역인 서브넷으로 나뉜다.

서브넷은 보통 역할 분리에 사용된다.

예:

- 퍼블릭 서브넷
- 프라이빗 서브넷

### 3. AZ와의 관계

서브넷은 하나의 AZ에 속한다. 이건 2회차에서 배운 글로벌 인프라와 연결되는 부분이다.

즉:

- Region이 큰 단위
- AZ가 그 안의 물리 분산 단위
- 서브넷은 그 AZ 안의 네트워크 구역

이라고 보면 된다.

---

## 퍼블릭 서브넷과 프라이빗 서브넷

이건 VPC에서 반드시 구분해야 하는 개념이다.

### 퍼블릭 서브넷

인터넷 게이트웨이(IGW)를 통해 외부 인터넷과 직접 통신할 수 있는 서브넷이다.

보통 여기에 두는 것:

- 인터넷에서 접근해야 하는 웹 서버
- 퍼블릭 로드 밸런서
- 베스천 호스트 등

### 프라이빗 서브넷

외부 인터넷에서 직접 접근할 수 없는 서브넷이다.

보통 여기에 두는 것:

- 내부 애플리케이션 서버
- 데이터베이스
- 외부에 직접 노출되면 안 되는 자원

즉, 보안상 중요한 리소스는 프라이빗 서브넷에 넣는 구조가 기본 패턴이다.

---

## NAT Gateway는 왜 필요한가

자료에서는 NAT Gateway도 같이 언급됐다.

프라이빗 서브넷의 서버는 외부에서 직접 들어오면 안 되지만, 반대로 서버가 외부로 나가는 통신은 필요할 수 있다.

예를 들어:

- 패키지 다운로드
- 외부 API 호출
- 업데이트 서버 접근

이럴 때 쓰는 것이 NAT Gateway다.

즉:

- 외부 인터넷 → 프라이빗 서버 직접 접근은 안 됨
- 프라이빗 서버 → 외부 인터넷으로 나가는 통신은 가능하게 해줌

이 구조를 시험에서는 자주 묻는다.

정리하면:

- 퍼블릭 접근 필요 → Internet Gateway
- 프라이빗 서버의 outbound 인터넷 필요 → NAT Gateway

다.

---

## VPC 보안: Security Group vs NACL

6회차 자료에서 가장 시험형으로 중요한 부분 중 하나가 이 비교였다.

### Security Group

- 인스턴스 단위에 가깝게 적용
- Stateful
- Allow만 가능

### NACL(Network ACL)

- 서브넷 단위
- Stateless
- Allow / Deny 가능

이 비교는 시험에서 거의 정석 문제처럼 나온다.

---

## Stateful vs Stateless를 이해하는 법

### Security Group은 Stateful

요청을 허용하면 응답은 별도 규칙 없이도 허용된다.

즉, 들어오는 연결을 허용했으면 그 연결에 대한 응답은 자동으로 허용되는 식이다.

### NACL은 Stateless

요청과 응답을 따로 봐야 한다.

즉, inbound 규칙과 outbound 규칙을 각각 명시해야 한다.

처음에는 이 말이 추상적으로 느껴질 수 있는데, 시험에서는 보통:

- "상태 저장형 가상 방화벽" → Security Group
- "서브넷 단위의 stateless 필터" → NACL

로 연결하면 쉽게 풀린다.

---

## Security Group과 NACL을 어떻게 나눠서 생각할까

가장 쉬운 구분은 이렇다.

### Security Group

서버 하나하나의 출입 규칙

### NACL

서브넷 전체의 출입 규칙

즉:

- Security Group은 세밀한 인스턴스 보호
- NACL은 더 큰 범위의 네트워크 제어

에 가깝다.

Cloud Practitioner 수준에서는 Security Group이 더 자주 나오지만, 둘의 차이를 알고 있어야 문제를 정확히 풀 수 있다.

---

## Route 53이란 무엇인가

Amazon Route 53은 AWS의 **관리형 DNS 서비스**다.

도메인 이름을 IP 주소로 변환하는 역할을 한다고 이해하면 된다.

쉽게 말하면:

- 사용자는 `example.com` 같은 이름을 입력하고
- Route 53은 이 이름이 결국 어디를 가리키는지 결정하는 데 관여한다

는 것이다.

### 중요한 점

Route 53은 웹 요청 자체를 처리하는 서비스가 아니다.

즉:

- 실제 HTTP 트래픽을 받는 서버가 아니고
- DNS 레벨에서 목적지를 알려주는 역할

이다.

이건 시험에서도 아주 자주 나오는 함정 포인트다.

---

## DNS 흐름 안에서 Route 53은 무슨 역할을 하나

자료에서도 강조된 것처럼 DNS는 전 세계 공통 구조를 가진다.

대략적인 흐름은:

- 루트 DNS
- TLD(.com 등)
- 권한 네임서버(authoritative DNS)

순으로 이어진다.

이때 Route 53은 **최종적으로 도메인에 대한 답을 주는 권한 DNS 역할**을 한다고 보면 된다.

즉, "이 도메인은 결국 어디로 가야 하나"에 대해 최종 답을 주는 서비스다.

---

## Route 53 라우팅 정책

![Route 53 DNS 흐름 설명 이미지](/downloads/aws-clf-c02/2026-01-22/route-53.png)

Cloud Practitioner에서는 Route 53을 단순 DNS 서비스로만 외우면 부족하다. 자료에서도 나왔듯이, **라우팅 정책**이 자주 출제된다.

### Simple Routing

가장 기본적인 방식이다. 하나의 리소스로 보낸다고 생각하면 된다.

### Weighted Routing

트래픽 비율을 나누는 방식이다.

예:

- A 서버 70%
- B 서버 30%

배포 테스트나 점진적 전환 시나리오와 연결해 생각하면 이해가 쉽다.

### Latency Routing

사용자에게 가장 지연 시간이 낮은 리전으로 보내는 방식이다.

글로벌 서비스를 이야기할 때 자주 나온다.

### Failover Routing

장애가 나면 다른 대상지로 넘기는 방식이다.

재해 복구(Disaster Recovery)나 고가용성과 연결된다.

### Geolocation Routing

국가나 지역에 따라 다른 목적지로 보내는 방식이다.

예를 들어:

- 한국 사용자는 서울
- 미국 사용자는 북버지니아

처럼 지역별 분기가 필요할 때 연결해서 이해할 수 있다.

---

## Route 53은 언제 떠올려야 하나

시험에서 다음 같은 표현이 보이면 Route 53을 생각하면 된다.

- 도메인 이름 관리
- DNS
- 리전별 사용자 분산
- 장애 시 다른 리전으로 우회
- 지연 시간 기반 라우팅

즉, Route 53은 단순 DNS를 넘어서 **"어느 목적지로 보낼 것인가"를 DNS 차원에서 결정하는 서비스**라는 점이 중요하다.

---

## Load Balancer란 무엇인가

Elastic Load Balancing(ELB)은 여러 대상(target)으로 트래픽을 분산하는 서비스다.

쉽게 말하면:

- 사용자의 요청이 들어왔을 때
- 한 서버에 몰리지 않게
- 여러 서버에 나눠 보내는 장치

다.

### 왜 필요할까

서버가 한 대만 있으면:

- 트래픽이 몰릴 때 부담이 집중되고
- 장애가 나면 서비스가 멈출 수 있다

로드 밸런서는 이런 문제를 줄이는 데 도움이 된다.

즉, 고가용성과 확장성 측면에서 매우 중요하다.

---

## ALB와 NLB

자료에서 시험 핵심으로 나온 부분은 **ALB(Application Load Balancer)** 와 **NLB(Network Load Balancer)** 비교였다.

### ALB

- L7 계층
- HTTP/HTTPS 기반
- URL 경로, 호스트 헤더 등 애플리케이션 레벨 분기 가능

즉, 웹 애플리케이션이나 API 서비스처럼 요청 내용에 따라 분기해야 하는 경우 잘 맞는다.

예:

- `/api/*` 는 API 서버로
- `/images/*` 는 다른 서버 그룹으로

### NLB

- L4 계층
- TCP/UDP 기반
- 초고속 처리
- 고정 IP 같은 요구와 연결되기 쉬움

즉, 더 낮은 계층에서 빠르게 네트워크 트래픽을 처리하는 데 적합하다.

---

## L4 vs L7을 쉽게 이해하는 법

처음 보면 이 표현이 네트워크 전공 용어처럼 느껴질 수 있다. 시험에서는 아주 단순하게 잡아도 충분하다.

### L4

포트와 프로토콜 수준

### L7

HTTP 요청 내용, URL, 헤더 같은 애플리케이션 수준

즉:

- "웹 요청의 내용까지 보고 나눠야 한다" → ALB
- "네트워크 레벨에서 빠르게 전달하면 된다" → NLB

로 생각하면 된다.

자료 폴더에 있던 `L7L4.JPG`도 이 비교를 이해하는 보조 이미지로 보였다.

---

## CloudFront란 무엇인가

![Load Balancer 계층 비교 보조 이미지](/downloads/aws-clf-c02/2026-01-22/L7L4.JPG)

Amazon CloudFront는 AWS의 **CDN(Content Delivery Network)** 서비스다.

핵심 목적은 매우 분명하다.

- 사용자 가까운 위치에서 콘텐츠를 전달해
- 더 빠른 응답 속도를 제공하는 것

이다.

2회차에서 배운 Edge Location 개념이 여기서 다시 살아난다.

CloudFront는 전 세계 Edge Location을 활용해 콘텐츠를 캐싱하고, 사용자에게 더 가까운 위치에서 응답한다.

---

## CloudFront가 필요한 이유

원본 서버가 한 리전에만 있을 때, 전 세계 사용자들이 모두 그 리전까지 직접 요청하면 지연 시간이 커질 수 있다.

CloudFront는 이런 문제를 줄여준다.

### 예를 들어

- 원본 서버는 서울 리전에 있고
- 유럽 사용자가 접속한다고 하자

이때 CloudFront가 없으면 유럽 사용자는 서울까지 요청을 보내야 한다. 하지만 CloudFront가 있으면 더 가까운 Edge Location에서 캐시된 콘텐츠를 받을 수 있다.

즉:

- 속도가 빨라지고
- 원본 서버 부하가 줄고
- 글로벌 사용자 경험이 좋아진다

---

## Route 53, ELB, CloudFront는 어떻게 다를까

이 세 서비스는 자주 같이 나오기 때문에 비교가 중요하다.

### Route 53

- DNS
- 어디로 갈지 정한다
- 트래픽 자체를 처리하지는 않는다

### ELB

- 실제 요청을 여러 서버에 분산한다
- 가용성과 확장성을 돕는다

### CloudFront

- 캐싱을 통해 콘텐츠 전달을 빠르게 한다
- Edge Location 기반

즉, 역할이 완전히 다르다.

쉽게 말하면:

- Route 53: 주소를 알려줌
- ELB: 요청을 나눠줌
- CloudFront: 더 가까운 곳에서 빨리 줌

---

## VPC부터 CloudFront까지 전체 흐름으로 보기

이번 회차는 서비스별로 끊어 외우기보다 흐름으로 이해하는 것이 중요했다.

예를 들어 사용자가 웹 서비스에 접속할 때를 생각해보면:

1. 사용자가 도메인 입력
2. Route 53이 목적지 결정
3. 필요하면 CloudFront가 가까운 엣지에서 콘텐츠 전달
4. 원본 요청은 Load Balancer로 감
5. Load Balancer가 여러 EC2로 분산
6. 이 서버들은 VPC 안의 적절한 서브넷에 배치

이렇게 보면 6회차 서비스들이 따로 노는 것이 아니라, 하나의 요청 흐름 안에서 각자 역할을 나눠 가진다는 게 더 잘 보인다.

---

## 이번 회차에서 남겨둘 포인트

이번 회차를 하면서 가장 크게 느낀 건, 네트워크 서비스는 "어떤 일을 하느냐"보다 **어느 단계에서 어떤 역할을 맡느냐**로 이해해야 덜 헷갈린다는 점이었다.

예를 들어:

- Route 53도 분산처럼 보이고
- ELB도 분산처럼 보이고
- CloudFront도 속도 개선처럼 보인다

그래서 처음에는 비슷하게 느껴진다.

그런데 정리해보면:

- Route 53은 DNS 단계
- ELB는 애플리케이션 트래픽 분산 단계
- CloudFront는 콘텐츠 전달 최적화 단계

로 역할이 다르다.

VPC도 마찬가지다. VPC는 트래픽을 처리하는 서비스가 아니라, **그 서비스들이 놓이는 네트워크 공간 자체**다.

이 구분이 이번 회차의 핵심이었다.

---

## 시험 전에 다시 볼 포인트

짧게 압축하면:

- VPC는 AWS 안의 독립된 가상 네트워크다.
- 퍼블릭 서브넷은 인터넷과 직접 통신 가능하고, 프라이빗 서브넷은 NAT Gateway를 통해서만 outbound 인터넷 접근이 가능하다.
- Security Group은 인스턴스 단위의 stateful 방화벽이다.
- NACL은 서브넷 단위의 stateless 필터다.
- Route 53은 DNS 서비스이며 라우팅 정책이 중요하다.
- ALB는 L7, NLB는 L4다.
- CloudFront는 CDN이며 Edge Location을 활용한다.
- Route 53, ELB, CloudFront는 서로 역할이 다르다.

6회차까지 오고 나니 AWS 서비스가 조금씩 지도처럼 보이기 시작했다. 컴퓨팅, 스토리지, 데이터베이스, 네트워크가 따로 있는 것이 아니라, 실제 서비스 운영 흐름 안에서 다 연결된다는 감각이 조금씩 생겼다.

---

## 자료 다운로드

reference 폴더 기준으로 6회차와 직접 연결되는 자료 파일은 아래 4개였다. 복습용으로 바로 내려받을 수 있게 정적 경로로 옮겨두었다.

- [261122_AWS Network Service.html](/downloads/aws-clf-c02/2026-01-22/261122_AWS%20Network%20Service.html)
  VPC, Route 53, ELB, CloudFront 핵심 발표 자료.

- [261122_AWS Network Service_page.pdf](/downloads/aws-clf-c02/2026-01-22/261122_AWS%20Network%20Service_page.pdf)
  같은 발표 자료의 PDF 버전.

다음 글에서는 1월 27일 7회차 주제였던 보안과 계정 관리, 즉 Shared Responsibility Model, IAM, IAM Policy, 암호화와 MFA를 정리할 예정이다.
