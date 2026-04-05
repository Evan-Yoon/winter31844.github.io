---
title: "시각장애인을 위한 AI 보행 보조 앱 개발"
slug: "walkmate-day1-ai-setup"
date: 2026-02-09
author: "Evan Yoon"
category: "project"
subcategory: "team-project"
description: "[Walkmate] YOLOv11n과 TFLite를 활용한 온디바이스 AI 보행 보조 서비스의 시작. 데이터셋 구축 및 환경 설정 기록."
thumbnail: "/images/posts/walkmate/cover.png"
tags:
  - YOLOv11
  - TFLite
  - Flutter
  - Walkmate
  - Accessibility
readTime: 8
series: "Walkmate AI"
seriesOrder: 1
featured: true
draft: false
toc: true
---

> **"누군가에게는 당연한 보행이, 누군가에게는 도전이 되지 않도록."**
>
> 2026년 2월 9일, 시각장애인을 위한 AI 보행 보조 및 자동 민원 신고 서비스 **Walkmate** 프로젝트를 본격적으로 시작했다. 단순한 기술 구현을 넘어 사회적 가치를 담은 서비스를 지향한다.

---

## 🚀 Today's Mission & Results

오늘의 핵심 목표는 프로젝트의 아이디어를 구체화하고, 실제 구동 가능한 AI 모델의 환경을 구축하는 것이었다.

| 목표 항목 | 상태 | 비고 |
| :--- | :---: | :--- |
| 프로젝트 핵심 아이디어 구체화 및 차별화 포인트 설정 | ✅ 완료 | 유료 서비스(PathPal) 벤치마킹 및 차별성 확보 |
| 기술 스택 확정 (YOLOv11n, TFLite, Flutter) | ✅ 완료 | 모바일 최적화를 위해 경량 모델 채택 |
| 프로젝트 제안서 초안 작성 (1.1~1.4) | ✅ 완료 | 2025 통계 기반 문제 분석 반영 |
| 협업 그라운드 룰 및 브랜치 전략 수립 | ✅ 완료 | Git Flow 기반 협업 규칙 정립 |

---

## 💻 GitHub Commit History

오늘 진행된 커밋은 AI 모델 도입을 위한 기초 환경 설정에 집중되었다.

> [!NOTE]
> **Commit Hash:** `b87cb99`
> **Message:** `feat: Add new2.py script, road.jpg image, and dataset/train directory.`

- **`new2.py`**: YOLOv11n 모델 추론 및 테스트를 위한 기초 스크립트 작성.
- **`road.jpg`**: 객체 탐지 성능 검증을 위한 샘플 도로 이미지 추가.
- **`dataset/train`**: AI Hub 및 공개 데이터셋 활용을 위한 학습 데이터 디렉토리 구조 설계.

---

## 🛠 Tech Stack Optimization

프로젝트 기획 단계에서 발생한 가장 큰 이슈는 **실시간성**과 **모델 용량**이었다.

### ⚠️ Issue Situation
기존의 서버 통신 방식(API)은 지연 시간(Latency) 문제로 인해 실시간 보행 보조에 한계가 있었다. 보행 중 1~2초의 지연은 치명적인 사고로 이어질 수 있기 때문이다.

### 💡 Solution: On-device AI
1. **Model Selection**: 최신 **YOLOv11 Nano** 모델을 선택하여 연산량(FLOPs)을 최소화했다.
2. **Quantization**: 고도의 양자화 기술을 적용할 경우 모델 용량을 **6MB 이하**로 줄일 수 있음을 확인했다.
3. **Framework**: Flutter와 Vision 라이브러리를 결합하여 안정적인 크로스 플랫폼 환경을 구축하기로 했다.

---

## 📝 Daily Report Summary

- **실제 소요 시간**: 8시간 (계획 대비 2시간 초과 - 기술 스택 선정을 위한 심층 분석 진행)
- **핵심 결과물**: [Stitch UI 시안], [기술 검토 보고서], [GitHub 기초 리포지토리]
- **협업 시너지**: 팀원 최현석, 이지호 님과 함께 '자동 민원 신고'라는 차별적 가치를 도출했다.

---

## 💭 Reflections

### 감정적 변화
처음에는 기존 기술들이 너무 완벽해 보여서 "우리가 과연 새로운 걸 만들 수 있을까?" 하는 의문이 들었다. 하지만 '자동 신고'와 같은 사회적 가치를 덧붙이며 독창성을 찾아갔을 때 큰 성취감을 느꼈다.

### 깨달은 점
최신 기술(v11)이 항상 정답은 아니지만, 모바일 환경에서는 **파라미터 수**와 **연산량**을 가장 먼저 체크해야 한다는 실무적인 원칙을 다시 한번 확인했다.

---

## 📂 Project Resources

프로젝트의 상세한 기획 내용이 담긴 제안서 파일을 아래 링크에서 확인할 수 있다.

> [!IMPORTANT]
> **[Walkmate 프로젝트 기획서 다운로드 (PDF/MD)](/docs/Walkmate_Proposal.md)**

---

**Next Plan:** 내일은 실제 맞춤형 데이터셋(점자블록, 킥보드 등)을 구축하고 YOLOv11n 모델의 첫 전이 학습(Transfer Learning)을 실시할 예정이다. 🚄
