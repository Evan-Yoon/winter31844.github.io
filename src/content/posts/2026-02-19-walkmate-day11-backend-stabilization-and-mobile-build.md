---
title: |
  [Walkmate] Day 11
  백엔드와 빌드 오류 잡기
slug: walkmate-day11-backend-stabilization-and-mobile-build
date: 2026-02-19
author: Evan Yoon
category: project
subcategory: team-project
description:
  "[Walkmate] FastAPI 리다이렉트 문제 해결, Capacitor 기반 파일 시스템 교체 및 YOLO 전처리 최적화
  기록."
thumbnail: /images/posts/walkmate/cover11.png
tags:
  - FastAPI
  - Capacitor
  - Vite
  - YOLO
  - ReactNativeWeb
  - MobileApp
  - team-project
  - troubleshooting
  - bugfix
readTime: 10
series: Walkmate
seriesOrder: 6
featured: false
draft: false
toc: true
---

> **"통신에서의 슬래시 하나, 전처리에서의 비율 하나가 시스템의 전체 신뢰도를 결정한다."**
>
> 프로젝트 11일 차. 초기 기능 구현의 파고가 지나가고, 이제는 시스템의 안정성과 빌드 환경의 완성도를 높이는 단계에 접어들었다. 오늘은 백엔드 통신 오류를 잡고, 모바일 앱 빌드 시 발생하는 의존성 충돌을 해결하며 객체 인식의 정확도를 높이기 위한 이미지 전처리 로직을 개선했다.

---

## 🚀 Today's Mission & Results

백엔드 API 안정화와 모바일 앱 빌드 환경의 고도화를 목표로 삼았으며, 대부분의 이슈를 해결하고 전처리 알고리즘을 개선했다.

| 목표 항목                                              |  상태   | 비고                                                |
| :----------------------------------------------------- | :-----: | :-------------------------------------------------- |
| 백엔드 API 연결 안정화 (Trailing Slash 이슈)           | ✅ 완료 | 307 리다이렉트 및 404 해결                          |
| 모바일 앱 빌드 오류 해결 (Capacitor 마이그레이션)      | ✅ 완료 | `react-native-fs`를 `capacitor/filesystem`으로 교체 |
| YOLO 이미지 전처리 최적화 (Letterbox 도입)             | ✅ 완료 | 이미지 왜곡 방지 및 인식률 향상                     |
| Vite 빌드 설정 최적화 및 네이티브 라이브러리 충돌 수정 | ✅ 완료 | `vite.config.ts` alias 및 exclude 설정              |
| AdminUI 타입 에러 수정                                 | ✅ 완료 | ReportCard 컴포넌트 key 속성 타입 에러 해결         |

---

## 💻 GitHub Commit History

빌드 환경 구축과 핵심 내비게이션 기능의 기초를 다지기 위해 주요 커밋들이 진행되었다.

| Hash      | Message                                                    | 주요 내용                                                |
| :-------- | :--------------------------------------------------------- | :------------------------------------------------------- |
| `a528c0a` | **build: add Capacitor Android build configuration**       | 핵심 및 커뮤니티 플러그인 Android 빌드 설정 추가         |
| `6db7e26` | **feat: Add YOLO parsing utility and reporting API**       | YOLO 결과 파싱 유틸리티 및 Vision Camera 연동 API 구축   |
| `28d247f` | **feat: Introduce hazard reporting system**                | 실시간 객체 탐지 기반 S3 이미지 업로드 및 신고 관리 기능 |
| `e1382e2` | **feat: Initialize Capacitor Android project with TFLite** | Web API 기반 TFLite 연동 및 앱 구조 초기화               |
| `52c27f6` | **feat: Add initial user and admin UI structures**         | API 연동 및 환경 설정, UI 기본 구조 구축                 |
| `0f3375b` | **feat: Integrate TMAP API for navigation**                | 경로 계산, 장소 검색 및 디버그용 맵(Leaflet) 통합        |
| `7346e82` | **feat: Implement real-time pedestrian navigation**        | GPS/나침반/TTS 통합 백엔드 경로 안내 화면 구현           |
| `6644a0f` | **chore: Add package-lock.json**                           | 의존성 잠금 및 환경 일관성 확보                          |

---

## 🛠 Tech Stack & Implementation

### 1. API 통신의 미묘한 차이: Trailing Slash 이슈 <!-- short: 1. API 통신의 미묘한 차이 -->

FastAPI 서버에서 특정 엔드포인트 호출 시 307 리다이렉트나 404 오류가 반복적으로 발생했다. 조사 결과, 백엔드 경로 정의와 클라이언트 요청 주소의 끝에 슬래시(/) 유무가 일치하지 않아 발생하는 문제였다. 모든 API 주소 끝에 슬래시를 추가하여 통신 안정성을 확보했다.

### 2. Capacitor 기반 파일 시스템 교체

기존 `react-native-fs`를 사용하던 중 호환성 문제로 인해 빌드 오류가 지속되었다. 이를 해결하기 위해 Capacitor 환경에 최적화된 `@capacitor/filesystem`으로 플러그인을 교체했다. 네이티브 파일 접근 방식이 더 깔끔해졌으며 빌드 안정성도 높아졌다.

### 3. Vite 빌드 환경 및 Ngrok 우회

- **네이티브 라이브러리 충돌**: Vite 빌드 시 특정 라이브러리가 브라우저 환경과 충돌하는 문제를 방지하기 위해 `vite.config.ts`에서 `alias`와 `exclude` 설정을 세밀하게 조정했다.
- **Ngrok 경고 페이지**: 무료 버전의 ngrok은 첫 접속 시 안내 페이지를 띄우는데, 이로 인해 API 요청이 차단되는 현상이 있었다. 모든 요청 헤더에 `ngrok-skip-browser-warning`을 추가하여 이를 우회했다.

---

## ⚠️ Issue Situation & Troubleshooting <!-- short: Troubleshooting -->

### 이슈 1: 이미지 왜곡으로 인한 오인식 (Stretching vs Letterbox) <!-- short: 이슈 1 -->

- **문제**: YOLO 모델에 입력되는 이미지 리사이징 과정에서 종횡비를 무시하고 늘리는(Stretching) 방식이 사용되었다. 이로 인해 노란색 점자블록이 벤치처럼 길쭉하게 왜곡되어 오인인식되는 문제가 발생했다.
- **해결**: 이미지의 원본 비율을 유지하면서 부족한 부분을 검은색 등으로 채우는 **레터박스(Letterbox)** 방식을 도입했다. 이를 통해 객체의 기하학적 형태가 보존되어 인식 정확도가 대폭 향상되었다.

### 이슈 2: react-native 내부 Flow 문법으로 인한 Vite 빌드 실패 <!-- short: 이슈 2 -->

- **문제**: 일부 라이브러리 내부에서 Flow 타입 문법이 사용되어 Vite의 기본 빌드 프로세스에서 에러가 발생했다.
- **해결**: `react-native-web`을 설치하고, 빌드 시 해당 모듈로 대체(alias)하도록 설정하여 웹 환경에서도 원활하게 빌드되도록 조치했다.

---

## 💭 Reflections

FastAPI에서 슬래시 하나가 통신 성공 여부를 가르는 걸 보고, 프레임워크별 관례를 꼼꼼히 확인하는 게 얼마나 중요한지 다시 느꼈다. AI 모델 성능을 높이는 데 전처리가 모델 아키텍처 못지않게 결정적이라는 것도 이번에 실감했다.

내일부터는 레터박스 방식을 카메라 프레임 전처리에 전면 적용하고 YOLO26n 모델 성능 검증에 집중할 계획이다.

---

## **Next Plan:** 카메라 프레임 전처리 알고리즘 고도화 및 YOLO26n 모델 현장 테스트 진행 예정. 🧭 <!-- short: Next Plan -->

## 안정화 단계에서 드러난 실제 병목

기능을 빠르게 붙이는 시기에는 새 화면이나 새 API가 눈에 잘 띈다. 하지만 프로젝트가 중반을 넘어서면 눈에 띄지 않는 안정화 작업이 훨씬 중요해진다. Day 11에서 의미 있었던 부분도 바로 여기였다. FastAPI 쪽 응답 구조, 모바일 빌드 환경, 의존성 충돌처럼 겉보기에는 사소해 보이는 요소들이 실제로는 전체 개발 속도를 가장 크게 좌우했다.

특히 모바일 빌드 문제는 팀 프로젝트에서 자주 과소평가된다. 기능 구현이 끝났다고 생각했는데 빌드가 깨지면 검증 자체가 멈춘다. 백엔드도 마찬가지다. API 스펙이 조금만 흔들려도 프론트엔드 디버깅 시간이 급격히 늘어난다. 그래서 이 시점의 작업은 단순 유지보수가 아니라, 이후 기능 추가를 가능하게 만드는 기반 공사에 가까웠다.

## 이 단계에서 배운 점

안정화는 "새로운 것을 안 만드는 시간"이 아니라 "이후에 더 빨리 만들기 위한 시간"이었다. 실제로 이 과정을 거친 뒤에는 버그 원인 추적 범위가 줄어들고, 팀원 간 책임 구분도 선명해졌다. 데모 일정이 있는 프로젝트일수록 이런 기반 정리가 뒤로 밀리기 쉬운데, 오히려 중간에 한 번 강하게 잡아두는 편이 전체 일정에는 더 유리하다는 것을 확인했다.
