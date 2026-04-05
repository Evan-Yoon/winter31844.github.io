---
title: "사회적 가치 정립과 협업을 위한 Git 동기화"
slug: "walkmate-day15-social-value-and-git-sync"
date: 2026-02-23
author: "Evan Yoon"
category: "project"
subcategory: "team-project"
description: "[Walkmate] UN SDGs 연계를 통한 프로젝트 가치 정립, 센서 퓨전 기반 방향 보정 설계 및 Git 브랜치 동기화 기록."
thumbnail: "/images/posts/walkmate/cover15.png"
tags:
  - UNSDGs
  - Git
  - SensorFusion
  - CapacitorAssets
  - Collaboration
readTime: 11
series: "Walkmate AI"
seriesOrder: 8
featured: false
draft: false
toc: true
---

> **"기술은 사람을 향할 때 가치 있고, 협업은 소통을 통해 완성된다."**
>
> 프로젝트 15일 차. 오늘은 단순히 기능을 만드는 것을 넘어, Walkmate가 세상에 필요한 이유를 UN의 지속가능발전목표(SDGs)와 연결하여 정리해 보았다. 또한, 팀 협업 과정에서 발생한 Git 브랜치 불일치 문제를 해결하고 보행자 방향 인식의 정밀도를 높이기 위한 알고리즘 설계를 진행했다.

---

## 🚀 Today's Mission & Results

서비스의 정당성 확보와 기술적 한계 극복, 그리고 원활한 협업 체계 구축을 핵심 목표로 삼았다.

| 목표 항목 | 상태 | 비고 |
| :--- | :---: | :--- |
| 프로젝트의 사회적 가치 정립 (UN SDGs) | ✅ 완료 | SDG 11.2, 10.2 목표 매칭 및 분석 |
| 보행자 방향(Heading) 보정 로직 설계 | ✅ 완료 | 센서 퓨전 및 LPF(저역 통과 필터) 구상 |
| GitHub 브랜치 구조 분석 및 동기화 | ✅ 완료 | `main`과 `yoonjihyun` 브랜치 간 격차 해소 |
| 브랜드 정체성 적용 (Capacitor Assets) | ✅ 완료 | 플랫폼별 아이콘 및 스플래시 생성 프로세스 검토 |
| AI 모델 고도화 환경 점검 | ✅ 완료 | Letterbox 전처리 기반 인식률 향상 확인 |

---

## 💻 GitHub Commit History

사용자 인터페이스의 핵심 컴포넌트인 GuidingScreen과 VisionCamera의 고도화, 그리고 앱 브랜딩을 위한 기초 작업들이 진행되었다.

| Hash | Message | 주요 내용 |
| :--- | :--- | :--- |
| `871821e` | **docs: add portfolio AI prompts and failure cases** | AI 모델 성능 분석 및 실패 케이스 문서화 |
| `267dbf1` | **feat: Add user UI README and presentation prompt** | 사용자 UI 가이드라인 및 발표 자료 기초 구축 |
| `d8cde77` | **feat: Add metadata.json for app info** | 앱 이름, 설명 및 필수 권한 정의 파일 추가 |
| `830f292` | **feat: Add new GuidingScreen UI component** | 내비게이션 안내를 위한 핵심 UI 컴포넌트 추가 |
| `17010e2` | **feat: Add initial Android app icons and splash** | 플랫폼별 브랜드 자산(아이콘, 스플래시) 초기 적용 |
| `0334a5b` | **feat: Implement HazardModal component** | 위험 요소 상세 정보 및 이미지 확대 기능 구현 |
| `5fd7444` | **feat: Implement initial user interface** | 카메라, 안내, 확인 화면으로 이어지는 핵심 사용자 경험 구축 |
| `283e9c0` | **feat: add VisionCamera component** | 실시간 객체 인식을 위한 카메라 뷰 컴포넌트 통합 |

---

## 🛠 Tech Stack & Implementation

### 1. UN SDGs와 서비스의 연계
Walkmate는 단순히 길을 찾아주는 앱이 아니다. 우리는 이 서비스가 UN의 지속가능발전목표 중 두 가지 핵심 목표에 기여함을 명확히 했다.
- **목표 11.2 (지속 가능한 교통 체계)**: 취약계층, 특히 장애인과 고령자를 위한 안전한 보행 환경 조성.
- **목표 10.2 (사회적 불평등 완화)**: 기술적 혜택을 시각장애인에게도 제공하여 소외 없는 정보 접근성 실현.

### 2. 센서 퓨전 기반 방향 보정 설계
GPS 데이터만으로는 보행자의 미세한 시선 방향을 잡기 어렵다. 이를 해결하기 위해 디지털 나침반(자기계)과 자이로스코프 데이터를 결합하는 **센서 퓨전** 방식을 구상했다. 특히 자기계 데이터의 흔들림을 제어하기 위해 **저역 통과 필터(LPF)**를 적용하여 지도상의 화살표가 부드럽게 움직이도록 설계했다.

### 3. 브랜드 일관성 확보 (Capacitor Assets)
Android와 iOS 각 플랫폼의 다양한 해상도에 일일이 대응하는 대신, `Capacitor Assets` 도구를 활용하기로 했다. 단일 원본 소스에서 모든 크기의 아이콘과 스플래시 이미지를 자동 생성함으로써 개발 효율성을 높이고 브랜드 일관성을 확보했다.

---

## ⚠️ Issue Situation & Troubleshooting

### 이슈 1: 브랜치 동기화와 팀원 작업 비표시 문제
- **문제**: 현재 작업 중인 `yoonjihyun` 브랜치가 `main` 브랜치보다 19개의 커밋이 뒤처져 있어, 다른 동료들이 Push한 최신 코드(`leejiho`, `choihyunseok` 폴더 등)가 보이지 않는 현상이 발생했다.
- **해결**: 브랜치 간의 격차 원인을 분석하고, GitHub의 `Update branch` 기능을 사용하여 최신 `main` 코드를 병합(Merge)함으로써 협업 동기화 문제를 해결했다.

### 이슈 2: 저속 보행 시 방향 인식 불안정
- **문제**: GPS 기반의 헤딩은 속도가 일정 수준 이상일 때 정확하나, 보행자의 느린 발걸음에서는 방향 화살표가 튀는 현상이 잦았다.
- **해결**: 기기 내장 센서(나침반)를 주력으로 사용하되, 필터링 알고리즘을 통해 노이즈를 제거하는 로직을 다음 단계에서 구현하기로 결정했다.

---

## 💭 Reflections

### 기술 그 이상의 가치
코드를 짜는 기술만큼이나 중요한 것은 우리가 무엇을 위해 이 기술을 만드는가 하는 '목적의식'임을 느꼈다. UN SDGs 목표에 우리 서비스를 대입해 보며 느낀 보람은 개발의 원동력이 되었다.

### 협업의 도구, Git
깃허브의 브랜치 시스템을 제대로 이해하지 못하면 팀 프로젝트가 얼마나 고립될 수 있는지 실감했다. 소통의 시작은 메신저뿐만 아니라, 서로의 코드가 만나는 브랜치를 최신으로 유지하는 것에서부터 시작된다는 교훈을 얻었다.

---

**Next Plan:** 422 에러 해결을 위한 API 전송 로직 최종 배포 및 모든 팀원의 코드를 `main`으로 통합하는 전체 테스트를 진행할 예정이다. 🧭
