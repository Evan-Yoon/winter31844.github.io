---
title: "Python 중급 3일차: 상속, 클래스 확장"
slug: python-intermediate-day3-inheritance-and-extension
date: 2025-12-12
author: Evan Yoon
category: study
subcategory: self-study
description: "Python 중급 3일차 기록. FourCal 예제를 통해 상속이 왜 필요한지, 기존 클래스 기능을 어떻게 확장하는지 정리했다."
tags:
  - python
  - study
  - self-study
  - inheritance
  - oop
readTime: 7
series: Python Self-Study
seriesOrder: 8
featured: false
draft: false
toc: true
---

## 중급 3일차에는 상속 개념이 나왔다

오늘은 객체지향에서 자주 나오는 상속을 실습으로 정리했다. 처음엔 상속이 단순히 "클래스를 복사해서 쓰는 것"처럼 느껴졌는데, 예제를 다시 보니 실제 의미는 기존 클래스를 기반으로 기능을 덧붙이는 데 더 가깝다.

수업 시간에 본 예제는 계산기 클래스였다.

## 기본 클래스부터 다시 읽어봤다

```python
class FourCal:
    def __init__(self, fir, sec):
        self.first = fir
        self.second = sec

    def add(self):
        return self.first + self.second

    def sub(self):
        return self.first - self.second

    def mul(self):
        return self.first * self.second

    def div(self):
        return self.first / self.second
```

이 클래스는 사칙연산을 처리하는 기본 계산기다. 그리고 상속은 이 구조를 그대로 가져오면서 새 기능을 추가할 수 있게 한다.

## 상속을 쓰면 기존 기능을 재사용할 수 있다

```python
class MoreFourCal(FourCal):
    def pow(self):
        return self.first ** self.second
```

이 코드는 `FourCal`을 상속받아 거듭제곱 기능을 추가한 형태다. 중요한 점은 `add()`, `sub()`, `mul()`, `div()`를 다시 만들지 않아도 된다는 것이다.

```python
mymore1 = MoreFourCal(4, 3)
print(mymore1.add())
print(mymore1.mul())
print(mymore1.pow())
```

처음에는 "새 클래스를 만들었는데 왜 옛 기능이 그대로 있지?" 싶었는데, 이게 상속의 핵심이었다. 기존 클래스의 기능을 물려받으면서 필요한 부분만 확장하는 방식이다.

## 상속은 중복을 줄이는 방식이다

오늘 공부하면서 가장 크게 느낀 점은, 상속이 단지 객체지향 문법이 아니라 중복 제거 도구라는 점이다. 비슷한 기능을 가진 클래스를 여러 개 만들 때, 공통 부분을 부모 클래스에 두고 차이만 자식 클래스에 적으면 구조가 훨씬 깔끔해진다.

기초 단계에서 함수를 만들어 반복 코드를 줄였다면, 중급 단계에서는 상속으로 클래스 단위의 반복을 줄인다고 이해할 수 있었다.

## 오늘 헷갈렸던 점

`class MoreFourCal(FourCal):`처럼 괄호 안에 부모 클래스를 적는 문법이 아직은 낯설었다. 하지만 "어디에서 기능을 물려받는지 표시한다"는 의미로 읽으니 조금 이해가 됐다.

또 자식 클래스가 부모의 속성과 메서드를 그대로 사용할 수 있다는 점도 초반에는 신기하게 느껴졌다. 결국 부모 클래스의 구조가 잘 잡혀 있어야 상속도 유용하게 작동한다는 뜻이기도 하다.

## 오늘의 실수 포인트

- 상속은 복붙이 아니라 재사용과 확장이다.
- 자식 클래스는 부모 클래스의 메서드를 그대로 사용할 수 있다.
- 공통 기능은 부모에, 추가 기능은 자식에 두는 식으로 생각하면 이해가 쉽다.

## 복습용으로 남기는 질문

1. 상속을 사용하는 이유는 무엇인가?
2. 부모 클래스와 자식 클래스의 관계는 어떻게 이해하면 좋은가?
3. `MoreFourCal`은 `FourCal`과 비교해 무엇이 추가되었는가?

## 한 줄 정리

오늘은 상속을 통해 "기존 코드를 버리지 않고 확장하는 방법"을 처음 제대로 이해해본 날이었다.
