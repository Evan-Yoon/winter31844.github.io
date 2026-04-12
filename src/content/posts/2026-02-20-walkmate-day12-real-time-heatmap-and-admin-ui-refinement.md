---
title: |
  [Walkmate] Day 12
  실시간 히트맵과 관리자 화면
slug: walkmate-day12-heatmap-and-admin-ui-refinement
date: 2026-02-20
author: Evan Yoon
category: project
subcategory: team-project
description: "[Walkmate] 실시간 위험 지역 히트맵 시각화, Admin UI 다크모드 최적화 및 복합 HTTP 에러 해결 기록."
thumbnail: /images/posts/walkmate/cover12.png
tags:
  - Heatmap
  - AdminUI
  - Leaflet
  - Docker
  - PostgreSQL
  - Troubleshooting
  - team-project
readTime: 12
series: Walkmate
seriesOrder: 7
featured: false
draft: false
toc: true
---

> **"데이터는 시각화될 때 비로소 가치를 지니며, 시스템은 예외 상황을 정복하며 단단해진다."**
>
> 프로젝트 12일 차. 오늘은 수집된 위험 데이터를 사용자에게 직관적으로 전달하기 위한 '실시간 히트맵' 구현에 집중했다. 또한, 관리자 페이지의 완성도를 높이고 그동안 우리를 괴롭혔던 다양한 HTTP 에러들을 하나씩 정복하며 시스템의 내실을 다지는 시간을 가졌다.

---

## 🚀 Today's Mission & Results

사용자 위치 기반의 시각화 도구를 완성하고, 관리자 도구의 안정성을 대폭 향상했다.

| 목표 항목                               |  상태   | 비고                                      |
| :-------------------------------------- | :-----: | :---------------------------------------- |
| 실시간 위험 지역 히트맵 구현            | ✅ 완료 | Leaflet 기반 히트맵 및 내 위치 표시       |
| Admin UI 고도화 및 테마 최적화          | ✅ 완료 | 다크모드/라이트모드 인터페이스 정밀 조정  |
| 이미지 전처리 로직(Letterbox) 적용 완료 | ✅ 완료 | 사물 왜곡 방지 로직 전면 적용             |
| 프로젝트 개발 환경 표준화 (Docker)      | ✅ 완료 | PostgreSQL, Redis 포함 Dev Container 구축 |
| 복합 HTTP 에러(404, 405, 422) 해결      | ✅ 완료 | 통신 규격 일치 및 필드 스키마 업데이트    |

---

## 💻 GitHub Commit History

관리자 패널의 핵심 기능과 실시간 모니터링 시스템 구축을 위해 총 8건의 주요 프로젝트 업데이트가 진행되었다.

| Hash      | Message                                                 | 주요 내용                                               |
| :-------- | :------------------------------------------------------ | :------------------------------------------------------ |
| `1a3b48a` | **feat: Add Dev Container setup**                       | Docker, PostgreSQL, Redis 기반 개발 환경 표준화         |
| `8d4e0f0` | **feat: Introduce admin panel with hazard management**  | 신고 관리, soft deletion 기능 및 백엔드 엔드포인트 구축 |
| `fb9c5c8` | **feat: Implement real-time heatmap and vision camera** | 관리자용 실시간 히트맵 및 사용자 카메라 뷰 연동         |
| `79c9892` | **feat: Implement admin UI with dark mode**             | 상세 모달 뷰 및 테마 최적화가 적용된 Admin UI           |
| `b06ea94` | **feat: Implement Admin dashboard and user report API** | 대시보드 통계 및 사용자 신고 데이터 연동 체계 구축      |
| `2ffe66c` | **feat: Implement admin UI components**                 | 테이블, 모달 등 핵심 UI 컴포넌트 라이브러리화           |
| `be54f05` | **feat: Implement real-time hazard monitoring**         | 사용자-관리자 간 실시간 모니터링 대시보드 완성          |
| `d27dbd0` | **feat: Implement admin UI TestMonitor page**           | 거리/방향 정보가 포함된 실시간 모니터링 페이지 구현     |

---

## 🛠 Tech Stack & Implementation

### 1. 실시간 위험 지역 히트맵 (Heatmap)

사용자들이 신고한 위험 요소(점자블록 파손, 장애물 등)의 밀집도를 시각화하기 위해 Leaflet 기반의 히트맵 레이어를 구현했다.

- **내 위치 표시**: 사용자의 현재 위치를 빨간색 화살표(SVG)로 표시하여 주변 위험 지역과의 거리를 한눈에 파악할 수 있도록 했다.
- **시각적 직관성**: 위험도가 높은 지역은 짙은 빨간색으로, 낮은 곳은 노란색으로 그라데이션 처리하여 시각적 정보를 강화했다.

### 2. 관리자 UI 고도화 (Admin UI)

- **테마 최적화**: 다크모드와 라이트모드에서의 가독성을 높이기 위해 Tailwind CSS의 색상 팔레트를 재정비했다.
- **타입 안정성**: `TestMonitor.tsx`에서 발생하던 복잡한 데이터 구조의 TypeScript 타입 에러를 인터페이스 정의를 통해 깔끔하게 해결했다.

### 3. 개발 환경의 표준화

Docker와 Dev Container를 도입하여 팀원 간의 개발 환경 차이로 발생하는 '내 컴퓨터에선 되는데' 문제를 원천 차단했다. PostgreSQL과 Redis를 컨테이너로 묶어 서비스 실행의 편의성을 높였다.

---

## ⚠️ Issue Situation & Troubleshooting <!-- short: Troubleshooting -->

### 이슈 1: HTTP 에러 정복 (404, 405, 422) <!-- short: 이슈 1 -->

오늘 하루는 HTTP 상태 코드와의 전쟁이었다고 해도 과언이 아니다.

- **404 에러**: DB에 저장된 S3 이미지 주소에 백엔드 주소가 중복으로 붙는 로직 오류를 발견하여 수정했다.
- **405 에러**: 어제에 이어 FastAPI의 슬래시(/) 규칙과 ngrok의 브라우저 경고 페이지를 우회하는 헤더를 추가하여 해결했다.
- **422 에러**: 카메라 앱에서 서버로 데이터를 보낼 때 필수 값인 `distance`와 `direction`이 누락된 것을 확인했다. 안드로이드 API 호출 스키마를 업데이트하여 필수 필드를 보강했다.

### 이슈 2: 지도 렌더링 '회색 잘림' 현상

- **문제**: 지도 컨테이너의 크기가 동적으로 변할 때, Leaflet이 이를 인지하지 못해 지도의 일부가 회색으로 노출되는 현상이 발생했다.
- **해결**: 컴포넌트 렌더링 직후 `invalidateSize()` 함수를 호출하여 지도가 컨테이너 크기에 맞춰 다시 그려지도록 강제 조정했다.

### 이슈 3: Git Conflict와 Push 지옥

- **문제**: 여러 팀원이 동시에 설정을 건드리며 로컬과 원격 저장소의 데이터가 꼬여 Push가 거부되었다.
- **해결**: 강제 Push 대신 정석대로 Pull을 통해 변경 사항을 내려받은 후, 충돌 부분을 수동으로 병합(Merge)하여 깨끗한 히스토리를 유지했다.

---

## 💭 Reflections

404부터 422까지 에러를 하나씩 잡아 나가는 과정이 지루하면서도 시스템 구조에 대한 이해를 꽤 높여줬다. 슬래시 하나 차이가 통신 전체를 흔드는 걸 직접 보니, 규격 확인을 습관적으로 하게 됐다.

Letterbox 전처리를 전체에 적용한 게 이날의 가장 의미 있는 작업이었다. 정확도 문제가 모델보다 입력 처리에 있었다는 게 다시 확인됐다.

---

## **Next Plan:** FE는 TMAP API 정밀도를 높이고, BE는 마커 필터링 및 관리자용 통계 API 개발을 마무리할 예정이다. 🚧 <!-- short: Next Plan -->

## 실시간 히트맵이 갖는 의미

실시간 히트맵은 단순 시각화 요소가 아니라 서비스가 현장의 문제를 어떻게 학습할 것인가와 연결되는 기능이었다. 사용자의 제보, 위험 지역, 반복적으로 발생하는 불편 구간을 지도 위에 누적해서 볼 수 있으면 팀은 더 이상 감으로 기능 우선순위를 정하지 않아도 된다. 어떤 지역에서 신고가 자주 들어오는지, 어떤 유형의 위험이 반복되는지, 운영자가 어디를 먼저 확인해야 하는지 빠르게 판단할 수 있다.

관리자 UI를 다듬는 작업도 같은 맥락이었다. 현장 데이터가 많아질수록 중요한 것은 데이터를 더 수집하는 것이 아니라, 필요한 신호를 빠르게 읽어내는 구조를 만드는 것이다. 필터, 상태 구분, 우선순위 표시 같은 요소가 단순한 화면 장식이 아니라 운영 효율과 직접 연결된다는 점을 이 과정에서 더 분명하게 느꼈다.

## 앞으로 확장해 볼 수 있는 방향

- 지역별 위험 유형 분포를 따로 분류해 정책 제안 자료로 확장하기
- 관리자 화면에서 처리 이력과 후속 조치 결과를 함께 관리하기
- 데이터 누적 결과를 사용자 안내 우선순위와 연결하기

이런 구조가 갖춰지면 Walkmate는 단순 보조 앱을 넘어, 도시 보행 환경을 해석하는 서비스로도 발전할 수 있다.
