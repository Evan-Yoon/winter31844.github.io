---
title: "Java 학습 3일차: 문자열 처리와 배열 다루기"
slug: java-study-day3-strings-and-arrays
date: 2026-01-07
author: Evan Yoon
category: study
subcategory: bootcamp
description: "1월 7일 Java 학습 기록. 문자열 비교와 메서드, 배열 선언과 순회, 2차원 배열, 배열 복사와 문자열 분해까지 실습 내용을 자세히 정리했다."
tags:
  - java
  - study
  - string
  - array
  - control-flow
readTime: 14
series: Java Study
seriesOrder: 3
featured: false
draft: false
toc: true
---

## 오늘 학습의 중심 주제
`reference/javastudy/day0107/src/javaStudy`에는 `Study01.java`부터 `Study24.java`까지 들어 있다. 전날이 입력, 연산자, 조건문, 반복문을 통해 프로그램 흐름을 제어하는 법을 익힌 날이었다면, 이날은 그 흐름 안에서 "데이터를 더 구조적으로 다루는 법"을 배운 날이었다.

핵심은 두 가지였다.

1. 문자열을 읽고, 비교하고, 자르고, 바꾸는 법
2. 여러 값을 한 번에 저장하고 순회하는 배열 사용법

이 둘은 이후 JavaFX, 파일 입출력, 주소록, 데이터 라벨링 같은 실습으로 갈 때 계속 반복해서 등장한다. 즉, day3는 "실제 데이터를 다루는 기초"에 해당한다.

## 1. 반복문 복습에서 출발했지만 목적은 더 멀리 있었다
`Study01.java`는 `float`를 0.1씩 증가시키는 반복문과, 정수로 계산 후 10으로 나누는 방식을 비교한다.

```java
for (float x = 0; x <= 1.0f; x += 0.1f) {
    System.out.println(x);
}
```

이 코드는 day2의 부동소수점 오차 개념을 다시 확인하게 해 준다. `0.1f`를 계속 더하면 우리가 머릿속으로 예상한 `0.0, 0.1, 0.2, ...`가 정확히 나오지 않을 수 있다.

그래서 아래처럼 정수 기반 반복이 더 안정적일 수 있다.

```java
for (int i = 0; i <= 10; i++) {
    System.out.println(i / 10.0);
}
```

이 예제는 단순 반복문 복습 같지만, 사실은 "컴퓨터가 수를 표현하는 한계"를 한 번 더 체감하게 해 주는 중요한 코드다.

## 2. while, do-while, break, continue 복습
`Study02.java`부터 `Study05.java`는 반복문 흐름을 조금 더 자연스럽게 만드는 실습이다.

- `Study02.java`: `while`
- `Study03.java`: `do-while`로 종료 문자 `q` 처리
- `Study04.java`: `break`
- `Study05.java`: `continue`

특히 `Study03.java`는 단순 숫자 반복이 아니라, 사용자가 메시지를 입력하고 `q`를 입력하면 종료하는 구조다.

```java
do {
    System.out.print(">");
    inputStr = scan.nextLine();
    System.out.println(inputStr);
} while (!inputStr.equals("q"));
```

여기서 중요한 건 두 가지다.

- 사용자 입력은 문자열로 다뤄야 한다.
- 문자열 비교는 `==`가 아니라 `equals()`를 써야 한다.

이 포인트가 바로 이어지는 문자열 파트의 핵심과 연결된다.

## 3. 참조 타입과 `null`: 문자열을 배우기 전에 꼭 알아야 하는 것
`Study07.java`는 일부러 `null` 참조를 사용해 에러가 날 수 있는 상황을 보여 준다.

```java
String str = null;
System.out.println(str.length());
```

이 코드는 `NullPointerException`을 발생시킨다. 즉, 문자열 변수 자체는 존재하지만 실제 문자열 객체를 가리키고 있지 않기 때문에 메서드를 호출할 수 없다.

### 왜 이게 중요한가
문자열을 다룰 때 가장 흔한 실수 중 하나가 `null`과 빈 문자열 `""`을 같은 것으로 착각하는 것이다.

- `null`: 아무 객체도 가리키지 않음
- `""`: 길이가 0인 실제 문자열 객체

이 구분을 모르면 나중에 입력값 검사, 파일 읽기, API 응답 처리에서 오류를 자주 만나게 된다.

## 4. 문자열 비교: `==`와 `equals()`는 전혀 다르다
`Study08.java`는 day3에서 가장 중요한 파일 중 하나다. 문자열 비교 방식을 직접 보여 주기 때문이다.

```java
String strVar1 = "이재명";
String strVar2 = "이재명";
String strVar3 = new String("이재명");
```

이때 비교 결과는 상황에 따라 달라진다.

- `==`: 두 변수가 같은 객체를 가리키는지 비교
- `equals()`: 문자열 내용이 같은지 비교

### 왜 헷갈릴까
문자열 리터럴은 같은 값을 재사용할 수 있기 때문에, 어떤 경우에는 `==`도 `true`가 나올 수 있다. 그래서 초보자는 "어? 되는 것 같은데?" 하고 착각하기 쉽다.

하지만 `new String("이재명")`처럼 새 객체를 만들면 이야기가 달라진다. 내용은 같아도 객체는 다를 수 있기 때문이다.

그래서 문자열 값 비교는 항상 아래처럼 하는 습관이 중요하다.

```java
strVar1.equals(strVar3)
```

이 원칙은 Java 공부 전체에서 매우 중요하다.

## 5. 빈 문자열 검사
`Study09.java`는 빈 문자열을 다룬다.

```java
String hobby = "";
if (hobby.equals("")) {
    System.out.println("빈 문자열");
}
```

이 코드는 단순하지만 실무에서도 자주 쓰는 패턴이다. 사용자가 아무것도 입력하지 않았는지 검사할 때 필요하기 때문이다.

다만 조금 더 익숙해지면 아래 방식도 자주 쓴다.

- `hobby.length() == 0`
- `hobby.isEmpty()`

입문 단계에서는 `equals("")`가 가장 직관적이다.

## 6. 문자열 메서드: 필요한 정보만 뽑아내는 도구들
`Study10.java`부터 `Study15.java`까지는 문자열 메서드 집중 실습이다. 이 구간은 외워야 한다기보다, "문자열을 조작하는 사고방식"을 익히는 것이 중요하다.

### `charAt()`
`Study10.java`는 주민번호 문자열에서 특정 위치 문자를 꺼내 성별을 판별한다.

```java
String ssn = "9406081234567";
char gender = ssn.charAt(6);
```

문자열도 결국 문자들의 순서 있는 묶음이라는 사실을 보여 준다. 인덱스는 0부터 시작한다는 점도 여기서 다시 확인한다.

### `length()`
`Study11.java`는 문자열 길이를 구한다.

```java
int length = ssn.length();
```

입력값이 정상 길이인지 확인하는 데 매우 자주 쓰인다.

### `replace()`
`Study12.java`는 문자열 일부를 다른 문자열로 바꾼다.

```java
String oldStr = "대한민국의 대통령은 윤석열입니다.";
String newStr = oldStr.replace("윤석열", "이재명");
```

여기서 중요한 건 원본 문자열이 직접 바뀌는 것이 아니라, 바뀐 결과를 담은 새 문자열이 만들어진다는 점이다. Java의 `String`은 불변 객체이기 때문이다.

### `substring()`
`Study13.java`는 문자열의 일부를 잘라 낸다.

```java
String ssn = "940608-1234567";
String firstNum = ssn.substring(0, 6);
```

시작 인덱스는 포함하고, 끝 인덱스는 포함하지 않는다는 규칙을 반드시 익혀야 한다.

### `indexOf()`
`Study14.java`는 특정 문자열의 위치를 찾는다.

```java
int location = subject.indexOf("프로그래밍");
```

원하는 단어가 어디서 시작하는지 알아야 그 위치를 기준으로 다시 자르거나 검사할 수 있다.

### `split()`
`Study15.java`는 문자열을 구분자로 나눠 배열로 바꾼다.

```java
String board = "1, 어린왕자, 철없는 왕자 이야기, 생텍쥐";
String[] strs = board.split(", ");
```

이 메서드는 이후 CSV 형태 데이터, 저장 파일, 사용자 입력 파싱에서 계속 쓰인다. day3에서 이걸 미리 익힌 건 꽤 좋았다.

## 7. 배열이 필요한 이유
문자열을 하나의 값으로 다루는 법을 익혔다면, 이제는 여러 값을 묶어서 다루는 배열로 넘어간다.

`Study16.java`는 가장 기본적인 1차원 배열 예제다.

```java
String[] season = {"spring", "summer", "fall", "winter"};
System.out.println(season[0]);
```

배열은 같은 타입의 값을 여러 개 저장하는 구조다. 가장 중요한 특징은 다음과 같다.

- 각 값에는 인덱스가 있다.
- 인덱스는 0부터 시작한다.
- 길이는 고정돼 있다.

### 왜 배열이 필요한가
지금은 계절 네 개 정도라 변수 네 개를 따로 만들어도 될 것처럼 보인다. 하지만 학생 점수 100개, 전화번호 500개, 문장 데이터 1만 개를 각각 따로 변수로 저장하는 것은 불가능하다. 그래서 "같은 종류의 값 여러 개"를 다룰 때 배열이 필요하다.

## 8. 배열 순회: 합계와 평균 구하기
`Study17.java`와 `Study18.java`는 배열을 순회하며 합계와 평균을 계산한다.

```java
int[] scores = {88, 95, 77};
int sum = 0;
for (int i = 0; i < scores.length; i++) {
    sum += scores[i];
}
```

이 패턴은 매우 중요하다.

1. 누적 변수 준비
2. 반복문으로 전체 순회
3. 각 원소를 누적
4. 필요하면 길이로 나눠 평균 계산

### `scores.length`를 써야 하는 이유
배열 길이를 직접 숫자로 적지 않고 `scores.length`를 쓰는 습관이 중요하다. 배열 크기가 바뀌어도 코드 수정이 줄고, 실수도 줄어든다.

## 9. 2차원 배열: 표 형태 데이터 다루기
`Study19.java`와 `Study20.java`는 2차원 배열을 다룬다.

```java
int[][] scores = {
    {80, 90, 96},
    {76, 88}
};
```

이 구조는 행과 열 개념을 가진다. 즉, 하나의 배열 안에 또 다른 배열이 들어 있는 형태다.

### 중요한 포인트
2차원 배열은 항상 직사각형일 필요가 없다. 즉, 각 행의 길이가 다를 수 있다.

- `scores.length`: 행 개수
- `scores[i].length`: 각 행의 열 개수

이 개념은 표 데이터, 좌표, 게임 보드, 다중 카테고리 데이터 같은 곳에서 자주 등장한다.

## 10. 배열도 참조 타입이다
`Study21.java`는 문자열 배열 비교를 통해 배열 역시 참조 타입이라는 점을 보여 준다.

```java
String[] strArray = new String[3];
strArray[0] = "Java";
strArray[1] = "Java";
strArray[2] = new String("Java");
```

문자열 비교에서 배웠던 것처럼, 배열 안에 들어 있는 객체 역시 참조를 통해 다뤄진다. 즉, 값이 같아 보여도 같은 객체인지 다른 객체인지는 따로 봐야 한다.

이 구간은 단순 배열 문법보다 "Java는 객체를 참조로 다룬다"는 감각을 다시 확인하는 데 의미가 있다.

## 11. 배열 복사: 직접 복사와 `System.arraycopy()`
`Study22.java`와 `Study23.java`는 배열 복사를 보여 준다.

### 직접 복사
```java
for (int i = 0; i < oldIntArray.length; i++) {
    newIntArray[i] = oldIntArray[i];
}
```

### `System.arraycopy()`
```java
System.arraycopy(oldStrArray, 0, newStrArray, 0, oldStrArray.length);
```

여기서 중요한 개념은 "배열 변수 하나를 다른 변수에 대입하는 것"과 "배열 내용을 실제로 복사하는 것"이 다르다는 점이다.

```java
newArray = oldArray;
```

이건 복사가 아니라 같은 배열을 함께 가리키게 되는 것이다. 반면 위 예제들은 원소를 새로운 배열에 옮겨 담는다.

이 차이는 이후 리스트, 객체 복사, 상태 관리에서도 계속 중요하다.

## 12. 긴 문자열 데이터를 구조적으로 읽기
`Study24.java`는 여러 줄의 긴 문자열 데이터를 다룬다. 게시글 번호, 제목, 설명, 저자 같은 텍스트를 하나의 큰 문자열로 두고 줄 단위와 구분자 단위로 나눌 수 있는 형태를 보여 준다.

이 예제는 이후 파일 입출력, 게시글 데이터 처리, CSV 파싱의 아주 초기 형태처럼 볼 수 있다. 즉, day3에서 배운 문자열과 배열 개념이 실제 데이터 처리 형태로 연결되기 시작한 것이다.

## 13. 오늘의 핵심 감각
1월 7일 실습을 통해 익혀야 하는 가장 중요한 감각은 아래와 같다.

- 문자열은 단순한 글자 덩어리가 아니라 메서드로 조작 가능한 객체다.
- 문자열 비교는 `==`가 아니라 `equals()`다.
- `null`과 빈 문자열은 다르다.
- 배열은 같은 타입 값 여러 개를 한 번에 다룰 수 있게 해 준다.
- 반복문과 배열은 거의 항상 같이 간다.
- 2차원 배열은 표 구조 데이터의 출발점이다.
- 복사는 대입과 다르다.

이 내용은 이후 모든 프로젝트 실습에서 계속 반복된다. 주소록도, 메모장도, JavaFX 입력도, 데이터 라벨링도 결국 문자열을 읽고 배열이나 리스트 형태로 순회하면서 처리한다.

## 오늘의 복습 질문
이 글만 읽고 아래 질문에 답할 수 있으면 day3 내용은 잘 정리된 것이다.

<details>
<summary>1. 문자열 비교에서 <code>==</code>를 쓰면 왜 위험할까?</summary>

`==`는 문자열 내용이 아니라 참조 위치를 비교하기 때문이다. 문자열 내용 비교는 `equals()`를 써야 안전하다.

</details>

<details>
<summary>2. <code>null</code>과 <code>""</code>는 어떻게 다를까?</summary>

`null`은 문자열 객체가 아예 없는 상태이고, `""`는 길이가 0인 빈 문자열 객체다. 둘은 의미가 다르다.

</details>

<details>
<summary>3. <code>substring(0, 6)</code>에서 왜 6번째 인덱스 문자는 포함되지 않을까?</summary>

`substring(begin, end)`는 시작 인덱스는 포함하고 끝 인덱스는 포함하지 않는 규칙을 따르기 때문이다.

</details>

<details>
<summary>4. 배열 순회에서 <code>scores.length</code>를 쓰는 이유는 무엇일까?</summary>

배열 길이를 직접 숫자로 적지 않고 실제 크기에 맞춰 반복하기 위해서다. 배열 크기가 바뀌어도 코드를 수정할 필요가 줄어든다.

</details>

<details>
<summary>5. 2차원 배열에서 <code>scores.length</code>와 <code>scores[i].length</code>는 각각 무엇을 뜻할까?</summary>

`scores.length`는 행 개수, `scores[i].length`는 특정 행 안의 열 개수를 뜻한다. 즉, 바깥 배열 크기와 안쪽 배열 크기를 따로 보는 것이다.

</details>

<details>
<summary>6. 배열 대입과 배열 복사는 왜 다른가?</summary>

배열 대입은 같은 배열을 함께 가리키게 하고, 배열 복사는 내용을 새 배열에 따로 옮긴다. 그래서 한쪽 수정이 다른 쪽에 영향을 주는지가 달라진다.

</details>

## 마무리
1월 7일은 Java가 단순 계산용 언어에서 "텍스트와 여러 값을 다루는 언어"로 보이기 시작한 날이었다. 문자열 메서드를 통해 필요한 정보를 꺼내고, 배열을 통해 여러 데이터를 묶어서 반복문으로 순회하는 흐름이 만들어졌기 때문이다.

이 기반이 있어야 다음 단계인 클래스와 객체, 그리고 실제 콘솔 미니 프로젝트가 훨씬 자연스럽게 이해된다. day3는 겉보기엔 잔기술이 많은 날 같지만, 실제로는 데이터 처리 감각을 만드는 매우 중요한 날이었다.
