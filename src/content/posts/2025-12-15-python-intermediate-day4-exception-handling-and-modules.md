---
title: "Python 중급 4일차: 예외처리, 모듈"
slug: python-intermediate-day4-exception-handling-and-modules
date: 2025-12-15
author: Evan Yoon
category: study
subcategory: self-study
description: "Python 중급 4일차 기록. try-except로 에러를 처리하고, calc.py 같은 모듈 분리 실습을 통해 코드를 조금 더 실용적으로 정리했다."
tags:
  - python
  - study
  - self-study
  - exception
  - module
readTime: 8
series: Python Self-Study
seriesOrder: 9
featured: false
draft: false
toc: true
---

## 오늘은 오류를 피하는 것이 아니라 다루는 법을 배웠다

기초와 중급 초반까지는 코드가 정상적으로 돌아가는 경우를 중심으로 공부했다. 그런데 오늘은 나누기에서 0이 들어오거나, 자료형이 맞지 않는 상황처럼 실제로 충분히 발생할 수 있는 오류를 어떻게 처리할지 배웠다.

처음엔 에러가 나면 그냥 틀린 코드라고만 생각했는데, 예외처리는 "틀릴 수 있는 상황을 미리 예상하고 흐름을 관리하는 것"이라는 점에서 훨씬 실전적인 내용이었다.

## try-except는 프로그램이 멈추지 않게 해준다

실습 코드에서는 아래처럼 예외를 처리했다.

```python
def new_div(a, b):
    try:
        result = a / b
        print("Result :", result)
    except ZeroDivisionError:
        print("Error : Division by zero")
    except TypeError:
        print("Error : Type Error")
```

이 코드를 보면 `try` 안에서는 정상 흐름을 실행하고, 문제가 생기면 `except`에서 해당 상황을 따로 처리한다. 예전 같으면 그냥 프로그램이 멈췄을 상황을, 이제는 예상 가능한 문제로 받아들이게 된다.

`ZeroDivisionError`, `TypeError`처럼 에러 종류가 다르다는 점도 중요했다. 같은 오류처럼 보여도 원인이 다르면 대응 방식도 달라진다.

## 안전한 계산기 클래스로 다시 보니 더 이해가 됐다

상속 예제와도 연결되는 코드가 있었다.

```python
class SafeFourCal(FourCal):
    def div(self):
        if self.second == 0:
            return 0
        else:
            return self.first / self.second
```

이 코드는 예외를 직접 잡는 방식은 아니지만, 위험한 입력을 미리 걸러서 안전하게 처리한다는 점에서 같은 맥락으로 이해할 수 있었다. 결국 중요한 건 "문제가 생길 수 있는 지점"을 코드에 반영하는 것이다.

## 모듈 분리는 코드를 나누는 첫 연습이었다

오늘 함께 본 파일 중에는 `calc.py`도 있었다.

```python
def plus(a, b):
    return a + b

def minus(a, b):
    return a - b

def mul(a, b):
    return a * b

def div(a, b):
    return a / b
```

이 파일을 보면서 모듈은 "기능별로 분리해둔 Python 파일"이라는 감각이 조금 생겼다. 아직 큰 프로젝트를 하는 단계는 아니지만, 연산 기능을 따로 모아두면 메인 코드가 훨씬 읽기 쉬워진다.

기초에서는 함수 하나하나를 배우는 느낌이었다면, 오늘은 그런 함수들을 파일 단위로 정리하는 감각까지 조금씩 연결됐다.

## 오늘 헷갈렸던 점

예외처리는 처음 보면 흐름이 일반 조건문과 달라서 조금 헷갈린다. `if`는 조건을 검사하는 것이고, `try-except`는 실제 실행 중 생긴 문제를 처리하는 구조라는 점을 구분해야 한다.

또 모듈 분리는 단순히 파일을 쪼개는 게 아니라, 어떤 기능을 어느 파일에 둘지 정리하는 습관과 연결된다는 점도 새로웠다.

## 오늘의 실수 포인트

- 모든 오류를 한 가지 방식으로 처리하려고 하면 흐름이 거칠어진다.
- `if`와 `try-except`는 역할이 다르다.
- 모듈은 코드 분량을 나누는 것이 아니라 역할을 나누는 것이다.

## 복습용으로 남기는 질문

1. `try-except`는 언제 필요한가?
2. `ZeroDivisionError`와 `TypeError`는 어떤 차이가 있는가?
3. `calc.py`처럼 파일을 분리하는 이유는 무엇인가?

## 한 줄 정리

오늘은 Python 코드가 실패할 수도 있다는 전제를 받아들이고, 그 실패를 관리하는 방법과 코드를 조금 더 구조적으로 나누는 방식을 함께 배운 날이었다.
