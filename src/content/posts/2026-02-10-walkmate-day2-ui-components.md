---
title: |
  [Walkmate] Day 2
  UI 컴포넌트 개발
slug: walkmate-day2-ui-components
date: 2026-02-10
author: Evan Yoon
category: project
subcategory: team-project
description: |
  [Walkmate] Vite + React 환경 구축 및
  시각장애인 접근성을 고려한 StandbyScreen, ListeningScreen, Waveform 컴포넌트 개발 기록.
thumbnail: /images/posts/walkmate/cover2.png
tags:
  - Vite
  - React
  - TypeScript
  - TailwindCSS
  - A11y
  - FramerMotion
  - team-project
readTime: 10
series: Walkmate
seriesOrder: 2
featured: false
draft: false
toc: true
---

> **"시각장애인을 위한 UI는 화려함보다 단순함과 확실한 피드백이 핵심이다."**
>
> 프로젝트 2일 차. 오늘은 실제 서비스를 구동할 프론트엔드 환경을 구축하고, 시각장애인 사용자가 처음 마주하게 될 핵심 UI 컴포넌트들을 설계했다.

---

## 🚀 Today's Mission & Results

오늘은 개발 환경을 셋업하고, 접근성(Accessibility)을 최우선으로 고려한 화면들을 구현하는 데 집중했다.

| 목표 항목                             |  상태   | 비고                                          |
| :------------------------------------ | :-----: | :-------------------------------------------- |
| Node.js (LTS) 설치 및 프로젝트 초기화 | ✅ 완료 | v24.13.0 설치 및 Vite 프로젝트 생성           |
| Tailwind CSS 및 기본 라이브러리 설정  | ✅ 완료 | 스타일링 시스템 및 의존성 설치 완료           |
| 시각장애인용 핵심 UI 컴포넌트 구현    | ✅ 완료 | StandbyScreen, ListeningScreen, Waveform 개발 |
| Git 저장소 연동 및 초기 UI 커밋       | ✅ 완료 | 4건의 커밋을 통해 단계별 형상 관리 수행       |

---

## 💻 GitHub Commit History

오늘 커밋은 환경 세팅부터 핵심 UI 컴포넌트 추가까지 단계별로 진행됐다.

| Hash      | Message                                  | 주요 내용                                        |
| :-------- | :--------------------------------------- | :----------------------------------------------- |
| `c0c94f5` | **user UI 생성**                         | 프로젝트 초기 UI 구조 및 컴포넌트 기초 설계      |
| `2635d81` | **feat: add Waveform component...**      | 음성 인식 시각화를 위한 웨이브폼 애니메이션 추가 |
| `e89b00f` | **chore: Install project dependencies.** | React, Tailwind 등 프로젝트 필요 패키지 설치     |
| `806f12c` | **file migration**                       | 프로젝트 폴더 구조 최적화 및 파일 이동           |

---

## 🛠 Tech Stack & Implementation

### 1. 개발 환경 구축

- **Node.js v24.13.0**: 안정성을 위해 LTS 버전을 설치했다.
- **Vite + React + TypeScript**: 빠른 개발 속도와 타입 안정성을 위해 최신 스택을 채택했다.
- **Tailwind CSS**: 유틸리티 기반 클래스로 UI를 신속하게 정의했다.

### 2. 핵심 UI 컴포넌트 개발

- **`StandbyScreen.tsx`**: 시각장애인이 어디를 터치해도 반응할 수 있도록 전체 화면을 터치 버튼(`role="button"`)으로 구현했다. 키보드 접근성을 위한 `onKeyDown` 이벤트도 잊지 않았다.
- **`ListeningScreen.tsx`**: 현재 음성을 인식 중임을 직관적으로 알 수 있는 인터페이스를 구축했다.
- **`Waveform.tsx`**: 정적인 UI에 생동감을 주기 위해 CSS Animation과 `animation-delay`를 활용한 리얼한 음성 파형 애니메이션을 구현했다.

---

## ⚠️ Issue Situation & Troubleshooting

개발 과정에서 마주한 이슈들과 그 해결 과정을 기록했다.

### 이슈 1: 패키지 설치 경로 에러 (ENOENT)

- **문제**: `npm install` 실행 시 `package.json`을 찾을 수 없다는 에러가 발생했다.
- **원인**: 터미널의 현재 경로가 프로젝트 루트 폴더인 `cvProjectTeam3`가 아닌 상위 폴더로 잡혀 있었다.
- **해결**: `cd cvProjectTeam3` 명령어로 정확한 경로로 이동 후 재실행하여 해결했다.

### 이슈 2: Git Commit 메시지 입력 대기

- **문제**: 커밋 시 `COMMIT_EDITMSG` 파일이 열리며 프로세스가 멈춘 것처럼 보였다.
- **원인**: Git이 기본 에디터(VS Code)를 통해 메시지 입력을 대기하는 정상적인 절차였으나 이를 바로 인지하지 못했다.
- **해결**: 메시지 작성 후 파일을 닫고 커밋을 완료했다. 향후에는 `-m` 옵션을 활용하여 효율을 높이기로 했다.

---

## 💭 Reflections

초반에 환경 설정 오류로 잠깐 당황했지만, 하나씩 해결하면서 감이 잡혔다.

코드를 직접 짜보니 접근성 UI는 **'화려함'**보다 **'단순함'**과 **'확실한 피드백'**이 핵심이라는 게 피부로 느껴졌다. 사용자가 정보를 인지하는 데 방해가 되는 걸 걷어내는 작업이었다.

---

## **Next Plan:** 내일은 팀 프로젝트 기획서 발표가 있다. **WalkMate**의 기획 의도와 기술적 차별성을 명확히 전달할 수 있도록 준비할 예정이다.

## 접근성 UI를 만들며 가장 신경 쓴 부분

Walkmate의 화면은 일반적인 모바일 앱처럼 많은 정보를 동시에 보여주는 방향과는 거리가 멀었다. 보행 중 사용되는 인터페이스는 시선을 오래 붙잡아두면 오히려 위험하다. 그래서 Day 2에서 UI 컴포넌트를 만들 때는 예쁜 화면보다도 "한눈에 상태가 구분되는가", "음성과 함께 썼을 때 충돌이 없는가", "터치 대상이 충분히 큰가"를 더 중요하게 봤다. StandbyScreen과 ListeningScreen을 분리해 설계한 것도 이런 이유였다. 사용자가 지금 대기 중인지, 음성 명령을 듣고 있는지, 안내가 진행 중인지 상태가 명확해야 실제 현장에서 당황하지 않는다.

Waveform 같은 시각 요소도 단순 장식으로 넣은 것이 아니었다. 청각 안내를 보조하는 최소한의 시각 피드백으로서, 현재 시스템이 살아 있고 사용자의 입력을 받고 있다는 신호를 전달하는 역할을 하도록 의도했다. 특히 접근성 제품에서는 화려한 애니메이션보다 신뢰감을 주는 반응성이 더 중요하다는 점을 다시 확인했다.

## 구현 이후에 남은 과제

당시에는 주요 컴포넌트를 먼저 만드는 데 집중했지만, 실제 서비스 관점에서는 다음 단계가 더 중요했다. 첫째는 안내 실패 상황에서의 예외 처리다. 둘째는 밝은 야외 환경에서도 식별 가능한 대비 설계다. 셋째는 음성 명령과 터치 인터랙션이 동시에 발생할 때의 충돌 방지다. 이런 부분까지 정리해야 UI가 단순 시안이 아니라 실제 보행용 제품 경험으로 이어질 수 있다.
