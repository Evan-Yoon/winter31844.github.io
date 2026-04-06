---
title: "[WorkWage] Flutter를 택한 건 언어 때문이 아니라 구조 때문이었다 — 기술 선택, AI 도구 활용, 급여 계산 로직"
slug: "workwage-part2-tech-prompting-paylogic"
date: 2026-03-05
author: "Evan Yoon"
category: "project"
subcategory: "personal-project"
description: "WorkWage 2편. Flutter/Provider/SharedPreferences를 선택한 이유, Stitch와 Jules를 구조화된 프롬프트로 활용한 경험, 그리고 자정을 넘기는 야간근무 계산이 왜 까다로운지를 정리했다."
thumbnail: "/images/posts/workwage-part2/cover.jpg"
tags:
  - workwage
  - flutter
  - dart
  - provider
  - salary-calculator
  - personal-project
  - ai-tools
  - pay-logic
readTime: 12
series: "WorkWage 프로젝트"
seriesOrder: 2
featured: false
draft: false
toc: true
---

## Flutter를 택한 건 언어 때문이 아니라 구조 때문이었다

처음에는 어떤 언어로 시작해야 할지 혼동이 있었다. Python으로 계산 로직을 먼저 짜야 하나, Java와는 어떻게 다른가. 선택지가 여러 개였다.

그런데 정리해보니 기준은 하나였다. **화면과 계산 로직을 같은 언어로 처리할 수 있는가.**

Python은 계산에 강하지만 모바일 화면을 만들기가 번거롭다. React Native는 JavaScript 생태계에 익숙해야 한다. Flutter는 Dart 하나로 UI와 로직을 전부 다룬다. 그리고 Dart는 Java와 문법적으로 닮은 부분이 많아서, 계산 클래스를 설계하는 데 심리적 장벽이 낮았다.

결론적으로 Flutter를 택한 이유는 두 가지다.

첫째, 이 앱은 화면보다 계산 로직이 중심이다. 급여 계산이 정확하게 작동하는 것이 핵심이고, 그 로직을 화면과 같은 언어로 같은 프로젝트 안에서 관리할 수 있는 것이 중요했다.

둘째, 입문 단계에서 새 언어를 하나만 배우는 것이 현실적이다. Flutter를 배우면서 동시에 별도 백엔드 언어를 익히는 것은 7일이라는 기간 안에 맞지 않았다.

---

## 폴더를 나눈 것이 나중에 가장 도움이 됐다

처음부터 단일 파일에 모든 것을 넣지 않았다. `lib` 폴더를 아래처럼 나눴다.

```
lib/
├── models/      — ShiftEntry 등 데이터 구조
├── providers/   — ChangeNotifier 기반 상태 관리
├── screens/     — 홈, 근무 입력, 설정 화면
├── utils/       — 급여 계산 함수, 유틸리티
└── widgets/     — 재사용 UI 컴포넌트
```

Flutter 입문 프로젝트에서 폴더 분리까지 신경 쓰는 게 과하다고 느낄 수 있다. 하지만 계산 로직이 복잡해질수록, 화면 코드 안에 계산 함수가 섞이면 디버깅이 어려워진다. `utils/` 안에 `salary_calculator.dart`를 별도로 분리해두면, 계산 로직만 독립적으로 테스트하고 수정할 수 있다.

상태 관리는 Provider/ChangeNotifier로 정리했다. Riverpod이나 Bloc도 선택지였지만, 입문 단계에서 복잡한 설정 없이 상태 변경을 화면에 반영하는 데는 Provider가 가장 직관적이었다. 로컬 저장은 SharedPreferences 중심으로 구성했다. 서버 없이 근무 기록을 영속 저장하고, 앱을 껐다 켜도 데이터가 유지되는 구조다.

---

## 프롬프트는 코드 요청이 아니라 기획서였다

UI는 Stitch로, 코드 구조와 로직은 Jules로 시작했다.

초반에는 막연하게 요청했다. "Flutter로 급여 계산기 만들어줘." 결과는 예상대로 껍데기 수준이었다. 입력 필드 몇 개, 버튼 하나, 계산 함수 없음.

바뀐 건 프롬프트의 밀도였다.

어떤 입력값을 받는지, 계산 규칙이 무엇인지, 상태는 어떻게 갱신되는지, 저장은 어디에 하는지까지 명시하자 산출물이 달라졌다. 프롬프트 안에 리스트 뷰 구조, 시간 선택 방식, 공휴일 토글, 시급 입력, Provider 상태 갱신 방식까지 적어주니, 생성된 코드가 실제 앱의 뼈대로 쓸 수 있는 수준이 됐다.

디자인 요청과 구현 요청을 분리한 것도 효과가 있었다. 화면을 먼저 Stitch로 잡고, 그 구조에 맞는 로직을 Jules에 요청하니 두 결과물이 서로 충돌하지 않았다. 화면과 로직을 한 번에 요청하면 AI가 둘 다 어중간하게 만들어낸다. 역할을 나눠서 요청하는 것이 결과 품질에 직접적인 차이를 만들었다.

이 프로젝트에서 배운 것 중 하나는, 생성형 도구는 내가 정확히 알고 있을수록 더 잘 작동한다는 점이다. 도구를 잘 쓰는 능력은 결국 문제를 구조화하는 능력과 같았다. 코드를 생성하기 전에, 내가 먼저 무엇을 원하는지 명확히 정리하는 과정이 필요했다.

---

## 시급 × 시간이 전부가 아닌 이유

교대근무 급여 계산을 처음 접하는 사람에게 설명하면 이렇다.

밤 11시에 출근해서 다음 날 오전 7시에 퇴근하는 근무를 생각해보자. 8시간 근무, 시급이 10,320원이라면 단순 계산으로는 82,560원이다.

하지만 실제 급여는 다르다.

밤 10시부터 다음 날 오전 6시 사이의 근무 시간에는 야간 수당이 붙는다. 이 근무에서 야간 구간은 오전 6시까지이므로, 밤 11시부터 다음 날 오전 6시까지 7시간이 야간 구간에 해당한다. 이 7시간은 시급의 1.5배로 계산된다. 만약 그날이 공휴일이었다면 추가 수당이 또 붙는다. 중간에 1시간 휴게를 취했다면 실근로는 7시간이 된다.

WorkWage에서 사용자가 직접 입력하는 건 날짜, 출퇴근 시간, 휴게 시간, 공휴일 여부뿐이다. 나머지 계산은 앱이 처리한다.

---

## 이 앱에서 진짜 어려웠던 건 Flutter가 아니었다

화면을 그리는 것보다 어려웠던 건, 급여 계산 규칙을 코드가 이해할 수 있는 형태로 바꾸는 일이었다.

**가장 까다로운 케이스: 자정을 넘기는 근무**

`20:00 ~ 05:00` 근무를 단순 시간 차이로 계산하면 `05 - 20 = -15`가 나온다. DateTime 객체를 활용해 종료 시간이 시작 시간보다 작으면 다음 날로 처리하거나, 자정을 기준으로 날짜를 명시적으로 분리하는 로직이 필요하다.

```dart
// 자정 넘김 처리 예시
DateTime start = DateTime(date.year, date.month, date.day, startHour, startMin);
DateTime end = DateTime(date.year, date.month, date.day, endHour, endMin);

if (end.isBefore(start)) {
  end = end.add(const Duration(days: 1)); // 다음 날로 처리
}
```

**야간 수당 구간 겹침 계산**

야간 구간은 당일 `22:00`부터 다음 날 `06:00`까지다. 근무 시간이 이 구간과 얼마나 겹치는지를 분 단위로 계산해야 한다.

```dart
// 야간 구간 겹침 계산 (개념 코드)
DateTime nightStart = DateTime(date.year, date.month, date.day, 22, 0);
DateTime nightEnd = DateTime(date.year, date.month, date.day + 1, 6, 0);

DateTime overlapStart = start.isAfter(nightStart) ? start : nightStart;
DateTime overlapEnd = end.isBefore(nightEnd) ? end : nightEnd;

int nightMinutes = overlapEnd.isAfter(overlapStart)
    ? overlapEnd.difference(overlapStart).inMinutes
    : 0;
```

여기에 휴게 시간 차감, 공휴일 분기, 연장근로 여부 판단이 더해지면, 겉보기에는 단순한 계산 함수 하나가 상당한 경계값 처리를 담게 된다.

이 항목들을 처음부터 빠짐없이 정의할 수 있었던 것은 HR 경험 덕분이었다. 실제 현장에서 어떤 조건이 급여에 영향을 미치는지 알고 있었기 때문에, 계산 규칙 설계에서 놓치는 케이스가 적었다.

---

## 여기까지 정리

2편에서 다룬 내용을 요약하면 이렇다.

- Flutter를 선택한 이유는 화면과 로직을 한 언어로 처리할 수 있기 때문이었다.
- 폴더 분리는 과하지 않았다. 계산 로직이 복잡할수록 구조 분리의 효과가 컸다.
- 생성형 도구는 구조화된 요청에 반응한다. 막연한 요청과 구체적인 요청의 결과 차이가 명확했다.
- 급여 계산의 핵심 난관은 자정 넘김과 야간 구간 겹침이었다. Flutter 문법보다 계산 규칙 번역이 더 어려웠다.

다음 편에서는 상태 관리 구조와 로컬 저장 흐름, 그리고 기능 확장 방향을 정리한다. 이번 파트에서 가장 분명해진 점은 도구보다 먼저 요구사항을 구조화해야 한다는 것이었다. 급여 계산처럼 예외가 많은 문제에서는 그 차이가 결과물에 그대로 드러났다.
