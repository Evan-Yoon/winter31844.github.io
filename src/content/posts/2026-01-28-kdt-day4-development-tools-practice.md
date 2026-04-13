---
title: "[KDT] 4일차: 개발 도구 실습 - AI와 Git으로 실전 준비하기"
slug: "kdt-day4-development-tools-practice"
date: 2026-01-28
author: "Evan Yoon"
category: "study"
subcategory: "bootcamp"
description: "Refactoring, VCS, Git, GitHub를 배우며 실제 개발 워크플로우를 경험한 하루."
thumbnail: ""
tags:
  - kdt
  - git
  - github
  - refactoring
  - development-tools
  - study
  - bootcamp
readTime: 7
series: "KDT 부트캠프 기록"
seriesOrder: 4
featured: false
draft: false
toc: true
---

지금까지 3일간 배운 것들은 모두 "논리와 이론"이었다. 오늘부터는 그 논리를 **"실제 코드"로 구현하고, 그 코드를 "협업하고 관리하는 방법"**을 배웠다. 머리로만 아는 것과 손으로 직접 하는 것의 차이를 느꼈다.

## 오늘 다룬 범위

- 코드 리팩토링(Refactoring): Magic Number, Rename, Guard Clause, Extract Method
- 버전 관리 시스템(VCS): RCS, CVS, SVN, Git
- Git의 기본 개념과 GitHub 실습
- 개발 도구: Antigravity, Google AI Studio, Jules

## 핵심 개념 정리

### 리팩토링 - 코드를 깔끔하게 정리하기

처음엔 이해가 안 갔다. "코드가 작동하는데 왜 바꿔야 해?"라고 생각했다.

하지만 강사님의 설명을 듣고 깨달았다. **리팩토링은 "기능은 같지만 더 읽기 쉽고 유지보수하기 좋은 코드로 만드는 것"**이다.

#### 1. Magic Number 제거

```python
# 나쁜 예
if age >= 18:  # 18이 왜? 무엇을 의미하나?
    grant_permission()

# 좋은 예
LEGAL_AGE = 18
if age >= LEGAL_AGE:
    grant_permission()
```

숫자 자체보다 **그 숫자가 의미하는 바**를 명확하게 하는 게 중요하다.

#### 2. 변수명 개선(Rename)

```python
# 나쁜 예
a = [1, 2, 3, 4, 5]
for i in a:
    print(i)

# 좋은 예
student_ids = [1, 2, 3, 4, 5]
for student_id in student_ids:
    print(student_id)
```

**코드는 컴퓨터가 아닌 사람이 읽는다.** 변수명이 명확하면 코드의 의도가 드러난다.

#### 3. Guard Clause (조기 반환)

```python
# 나쁜 예
def check_student(age):
    if age >= 18:
        if gpa > 3.0:
            if no_debts:
                return True
    return False

# 좋은 예
def check_student(age):
    if age < 18:
        return False
    if gpa <= 3.0:
        return False
    if debts:
        return False
    return True
```

**"예외 케이스를 먼저 처리하고 나가기"**로 코드의 복잡도를 줄인다.

#### 4. Extract Method (메서드 추출)

```python
# 나쁜 예: 한 함수에 너무 많은 일
def register_user(name, email, age):
    # 이메일 유효성 검증... (5줄)
    # 나이 유효성 검증... (3줄)
    # DB에 저장... (4줄)
    # 이메일 전송... (5줄)

# 좋은 예: 책임을 분리
def is_valid_email(email):
    # 이메일 검증 로직

def is_valid_age(age):
    # 나이 검증 로직

def register_user(name, email, age):
    if not is_valid_email(email):
        return False
    if not is_valid_age(age):
        return False
    save_to_db(name, email, age)
    send_email(email)
```

**"한 함수는 한 가지만 잘 하자"**는 원칙이 핵심이다.

### VCS와 Git - 코드를 함께 관리하기

처음엔 정말 와닿지 않았다. "왜 이런 걸 배우지? 코드 짤 때 필요한 게 아닌데?"

하지만 두 번째 수업부터 깨달았다. **프로그래밍은 절대 혼자 하는 일이 아니다.**

#### VCS의 진화

1. **RCS/CVS**: "한 명이 한 번에 한 파일만 수정" → 너무 느리다
2. **SVN**: "중앙 서버가 모든 버전 관리" → 중앙 서버가 망하면 끝
3. **Git**: "모든 개발자가 전체 히스토리를 가짐" → 분산형, 안전함

#### Git의 핵심 개념

```
Working Directory → Staging Area → Repository
  (내 컴퓨터)       (커밋할 파일들)   (히스토리 저장소)

git add .     → Staging
git commit    → Repository에 저장
git push      → 원격 서버에 전송
git pull      → 원격 서버에서 받기
```

**모든 변경 사항이 기록되므로, 언제든 과거로 돌아갈 수 있다.**

### GitHub - 협업의 플랫폼

Git을 혼자 쓰면 로컬 저장소지만, GitHub에 올리면 전 세계와 협업할 수 있다.

오늘 처음 GitHub에 코드를 푸시했을 때의 쾌감은 정말 특별했다. "내 코드가 인터넷에 올라갔다"는 느낌.

## 헷갈렸던 점 / 실수 포인트

### "리팩토링은 선택사항이 아니라 필수다"

처음엔 "리팩토링은 고급 기술 아닌가?"라고 생각했다. 하지만 강사님은 명확히 했다.

> "코드를 처음 짤 때부터 리팩토링을 염두에 두고 짠다. 그래야 나중에 유지보수 비용이 줄어든다."

**리팩토링은 나중의 선택이 아니라, 처음부터의 습관이다.**

### "Git은 어렵지 않다, 하지만 실수는 치명적이다"

Git 명령어 자체는 간단하다. 하지만 `git push --force` 같은 명령을 잘못 쓰면 팀 전체의 코드가 날아간다.

그래서 오늘 배운 중요한 원칙:
1. 혼자가 아닌 경우엔 절대 `--force` 사용 금지
2. `commit` 전에 항상 `git status`로 무엇을 커밋하는지 확인
3. 큰 변경을 할 때는 새로운 브랜치(branch)에서 작업

**도구는 강력할수록 조심해서 다뤄야 한다.**

### "AI도구의 존재를 몰랐다"

Google AI Studio, Antigravity 같은 도구들이 존재한다는 걸 오늘 처음 알았다.

- **Antigravity**: 자동으로 플로우차트를 만든다
- **Google AI Studio**: 간단하게 AI 모델을 테스트해본다

"앞으로의 개발은 이런 도구들을 어떻게 활용하느냐가 관건이겠구나"라는 생각이 들었다.

## 복습 Q&A

<details>
<summary>Q. 리팩토링하다가 실수로 기능을 깨뜨리면 어떻게 하나?</summary>

A. 그래서 Git이 필요하다!

리팩토링을 하기 전에:
1. `git add .`로 현재 상태를 커밋하고
2. 새 브랜치를 만들어서 리팩토링을 한다
3. 테스트를 돌려서 기능이 잘 작동하는지 확인
4. 문제가 있으면 `git revert`로 돌아간다

**따라서 "리팩토링 전 커밋"은 매우 중요한 습관이다.**

</details>

<details>
<summary>Q. Git과 GitHub의 차이?</summary>

A. 
- **Git**: 버전 관리 시스템 (로컬에서 작동)
- **GitHub**: Git 저장소를 호스팅하는 서비스 (클라우드)

비유하자면:
- **Git** = 일기장
- **GitHub** = 인터넷에서 일기를 공유하는 서비스

Git으로 로컬에서 관리하고, GitHub에 백업하고 공유한다.

</details>

<details>
<summary>Q. 매번 커밋을 하면 히스토리가 너무 길어지지 않나?</summary>

A. 오히려 좋다. 왜냐하면:

1. **돌아갈 포인트가 많다**: 언제든 과거의 상태로 돌아갈 수 있다
2. **변경 추적이 쉽다**: 언제 누가 뭘 바꿨는지 알 수 있다
3. **팀 협업에 유리하다**: 누가 어떤 부분을 담당했는지 명확해진다

다만, 커밋 메시지는 명확하게 작성해야 한다. "fix", "update" 같은 애매한 메시지는 나중에 혼란을 준다.

</details>

## 한 줄 정리

**깔끔한 코드와 효과적인 버전 관리는 좋은 개발자의 기본기다. 기능하는 코드도 중요하지만, 유지보수할 수 있는 코드가 더 중요하다.**

내일부터는 데이터베이스를 배운다. 지금까지는 변수와 함수 안에서 데이터를 다뤘다면, 내일부터는 **"대량의 데이터를 어떻게 안전하게 저장하고 관리할 것인가"**를 배우게 될 것 같다.
