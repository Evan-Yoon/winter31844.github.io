---
title: "TMAP API 전환 및 시각 장애인용 시계 방향 안내 시스템 구축"
slug: "walkmate-day4-tmap-navigation"
date: 2026-02-12
author: "Evan Yoon"
category: "project"
subcategory: "team-project"
description: "[Walkmate] TMAP API를 활용한 실시간 보행 내비게이션 환경 구축 및 시계 방향 안내 로직 구현 기록."
thumbnail: "/images/posts/walkmate/cover4.png"
tags:
  - TMAP
  - Navigation
  - Accessibility
  - Compass
  - Android
readTime: 10
series: "Walkmate AI"
seriesOrder: 4
featured: false
draft: false
toc: true
---

> **"사용자의 방향을 이해하는 것, 그것이 진정한 보행 안내의 시작이었다."**
>
> 프로젝트 4일 차. 오늘은 기술적인 구현만큼이나 '사용자가 실제로 어떻게 느끼는가'에 대한 고민이 깊었던 하루였다. Google Maps의 국내 도보 경로 미지원 문제를 TMAP으로 해결하고, 시각 장애인에게 가장 직관적인 '시계 방향' 안내 시스템을 도입했다.

---

## 🚀 Today's Mission & Results

보행 네비게이션의 정밀도를 높이고, 시각 장애인 사용자가 정면을 향할 수 있도록 돕는 방향 보정 시스템을 완성했다.

| 목표 항목 | 상태 | 비고 |
| :--- | :---: | :--- |
| TMAP API 연동 및 경로 데이터 수신 | ✅ 완료 | 한국 내 도보 안내 환경 구축 |
| 실시간 보행자 위치 추적 로직 구현 | ✅ 완료 | 안내 지점 자동 갱신 기능 |
| 나침반 연동 방향 보정 시스템 구축 | ✅ 완료 | "방향이 맞습니다" 멘트 성공 |
| 음성 안내(TTS) 큐잉 및 최적화 | ✅ 완료 | 안내 멘트 겹침 현상 해결 |

---

## 💻 GitHub Commit History

TMAP API 전환과 시계 방향 안내 로직 고도화를 위해 총 7건의 커밋이 이루어졌다.

| Hash | Message | 주요 내용 |
| :--- | :--- | :--- |
| `3a019b4` | **feat: implement NavigationScreen with TMAP** | TMAP API 기반 보행자 내비게이션 안내 화면 구현 |
| `5bfc248` | **feat: update TMAP integration** | TMAP 연동 관련 코드 및 통신 로직 반영 |
| `ba58658` | **chore: unify development env to Java 17** | 개발 환경 Java 17 버전 통합 |
| `e10746f` | **feat: initialize Android app with TFLite** | TFLite 및 카메라 지원 안드로이드 앱 초기화 |
| `d71d2ac` | **feat: add TFLite plugin and UI components** | NPU/GPU 가속 플러그인 및 안내/재시도 UI 추가 |
| `c6e0872` | **refactor: remove VisionScreen** | 불필요해진 이전 버전 스크린 코드 제거 |
| `41f3379` | **chore: update project configurations** | 프로젝트 설정 및 환경 최적화 업데이트 |

---

## 🛠 Tech Stack & Implementation

### 1. TMAP API 전환 및 환경 구축
Google Maps API가 국내 도보 경로 미지원을 확인한 후, 즉시 TMAP API로 전환을 결정했다. `CapacitorHttp`를 활용해 안드로이드 스튜디오 환경에서 안정적인 API 통신을 구현했으며, 실시간으로 경로 포인트를 수신하는 로직을 구축했다.

### 2. 시각 장애인 맞춤형 '시계 방향' 안내
단순한 "좌회전", "우회전" 안내는 사용자가 현재 어느 방향을 보고 있는지 모를 때 무의미할 수 있다. 이를 해결하기 위해:
- **웹 표준 API**: 기기의 나침반 센서(DeviceOrientation) 데이터를 활용했다.
- **방위각 계산**: 사용자의 현재 방향과 목적지 방위각을 비교하여 "2시 방향으로 도세요"와 같이 시계 방향으로 안내를 구체화했다.
- **방향 보정**: 사용자가 정확한 방향을 잡았을 때 "방향이 맞습니다"라는 피드백을 주어 심리적 안정감을 제공했다.

### 3. 음성 안내(TTS) 최적화
안내 멘트가 겹치거나 잘리는 현상을 방지하기 위해 **큐잉(Queuing) 로직**을 도입했다. 여러 안내가 동시에 발생할 경우 순차적으로 출력되도록 제어하여 정보 전달의 명확성을 높였다.

---

## ⚠️ Issue Situation & Troubleshooting

### 이슈 1: TMAP API 403 Forbidden 에러
- **문제**: API 키가 유효함에도 `INVALID_API_KEY` 에러가 발생하며 경로 정보를 불러오지 못했다.
- **해결**: TMAP 대시보드의 '상품 관리' 탭에서 '보행자 경로 안내' 서비스를 별도로 구독해야 함을 발견했다. 외부 API 사용 시 단순 키 발급 외에 서비스 활성화 상태 확인의 중요성을 깨달았다.

### 이슈 2: 센서 데이터의 민감도와 안내 간격
- **문제**: 나침반 센서의 미세한 떨림으로 인해 안내가 너무 자주 바뀌고, 2초 간격의 안내는 사용자에게 조급함을 주었다.
- **해결**: 저역 통과 필터(Low-pass filter)와 유사한 보정 로직을 적용해 떨림을 잡았고, 안내 간격을 3초로 상향 조정하여 사용자가 반응할 여유를 주었다.

---

## 💭 Reflections

### 감정적 변화
초기 Google API 미지원 소식에 좌절하기도 했지만, TMAP으로 전환 후 첫 음성 안내가 성공했을 때의 성취감은 대단했다. 특히 수차례의 로직 수정을 거쳐 "방향이 맞습니다"라는 멘트가 흘러나왔을 때 프로젝트의 실질적인 가치를 체감할 수 있었다.

### 인식적 변화
기술의 화려함보다 중요한 것은 '사용자의 호흡'이라는 것을 배웠다. 안내 간격을 단 1초 늘리는 결정이 시각 장애인 사용자에게는 큰 심리적 안정감을 준다는 점을 깨달았다. 개발자의 계산보다 사용자의 속도가 우선되어야 한다는 결론을 얻었다.

---

**Next Plan:** 내일은 실제 야외 환경에서 GPS 오차 보정 로직을 정밀화하고 필드 테스트를 진행할 예정이다. 또한, 관리자용 대시보드 설계를 시작하여 시스템의 관리 측면도 보충할 계획이다. 🚧
