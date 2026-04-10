---
title: "Java 학습 4일차: 클래스, 객체, 생성자, 메서드, 콘솔 미니 프로젝트"
slug: java-study-day4-classes-methods-and-console-projects
date: 2026-01-08
author: Evan Yoon
category: study
subcategory: self-study
description: "1월 8일 Java 학습 기록. 클래스와 객체, 생성자, 메서드, 오버로딩, static과 정적 초기화 블록, 주소록과 가계부 콘솔 프로젝트까지 실습 내용을 자세히 정리했다."
tags:
  - java
  - study
  - class
  - object
  - mini-project
  - static
  - method
readTime: 15
series: Java Study
seriesOrder: 4
featured: false
draft: false
toc: true
---

## 오늘부터 자바가 "대상을 만드는 언어"로 보이기 시작했다
`reference/javastudy/day0108/src/javaStudy`에는 `Study01.java`부터 `Study16.java`까지와 함께 `Student`, `Car`, `Korean`, `Calculator`, `CalculatorStatic`, `Television`, `AddressBook`, `PinMoney` 같은 클래스 파일이 따로 존재한다. 앞의 3일이 변수, 조건문, 문자열, 배열 같은 문법 요소를 익히는 흐름이었다면, 이날은 그 문법을 묶어서 "하나의 대상"으로 다루는 방법을 배우는 날이었다.

이날 학습 흐름을 크게 나누면 이렇다.

1. 배열과 반복문을 다시 써 보며 기초 감각을 정리한다.
2. 클래스와 객체를 만들고 `new`로 생성한다.
3. 필드와 생성자로 객체 상태를 초기화한다.
4. 메서드로 기능을 분리하고, 반환값과 매개변수를 다룬다.
5. 오버로딩과 `static`으로 클래스 설계 방식을 넓힌다.
6. 주소록과 가계부 같은 콘솔 미니 프로젝트로 객체 배열을 실제 프로그램처럼 사용한다.

즉, day4는 "문법을 아는 단계"에서 "코드를 구조로 묶는 단계"로 넘어가는 전환점이었다.

## 1. 시작은 의외로 배열과 반복문 복습이었다
`Study01.java`는 로또 번호 생성기다. 1부터 45 사이 숫자 6개를 뽑고, 중복을 제거하고, 마지막에 오름차순 정렬까지 한다.

```java
int[] lotto = new int[6];
for (int i = 0; i < lotto.length; i++) {
    lotto[i] = (int)(Math.random() * 45 + 1);
}
```

이 예제는 day3의 배열 지식과 day2의 반복문을 함께 다시 사용하게 만든다. 특히 중요한 부분은 두 군데다.

- 안쪽 반복문으로 이미 나온 숫자와 비교해 중복을 제거한다.
- 다시 한 번 중첩 반복문을 써서 버블 정렬 형태로 오름차순을 만든다.

즉, 객체지향으로 바로 점프하기 전에 "기존 문법을 조합해서 작은 기능을 만드는 감각"을 먼저 다졌다. 이 흐름이 중요하다. 클래스도 결국 이런 로직을 더 잘 담기 위해 등장하는 것이기 때문이다.

`Study02.java`는 향상된 for문도 복습한다.

```java
int[] scores = {95, 71, 88, 60, 55};
int sum = 0;
for (int score : scores) {
    sum += score;
}
```

여기서 배우는 핵심은 "배열 안의 값을 하나씩 꺼내 처리하는 더 간단한 문법"이다. 인덱스가 필요 없고 전체 순회만 하면 될 때는 일반 `for`보다 읽기 쉽다.

## 2. 명령행 인자: 입력은 콘솔 창 안에서만 오는 게 아니다
`Study03.java`는 `args` 배열을 사용한다.

```java
int x = Integer.parseInt(args[0]);
int y = Integer.parseInt(args[1]);
System.out.println("x + y = " + (x + y));
```

이 코드는 사용자가 실행할 때 함께 넘긴 문자열 값을 받아 숫자로 바꿔 계산한다. 여기서 중요한 포인트는 두 가지다.

- `main(String[] args)`의 `args`도 결국 문자열 배열이다.
- 사용자 입력은 어떤 방식으로 들어오든 계산 전에 적절한 타입 변환이 필요하다.

즉, day2에서 `Scanner`로 문자열을 받은 뒤 `parseInt()` 했던 흐름이 여기서도 그대로 반복된다. 입력 경로만 달라졌을 뿐 원리는 같다.

## 3. 클래스는 설계도고 객체는 실제 인스턴스다
`Study04.java`와 `Student.java`는 클래스와 객체의 가장 기본적인 관계를 보여 준다.

```java
Student s1 = new Student();
Student s2 = new Student();
```

`Student` 클래스 자체는 비어 있다.

```java
public class Student {
}
```

그런데도 `new Student()`를 두 번 호출하면 서로 다른 객체가 만들어진다. 여기서 가장 먼저 익혀야 하는 감각은 이것이다.

- 클래스는 틀이다.
- 객체는 그 틀로부터 만들어진 실제 대상이다.
- 같은 클래스라도 객체는 여러 개 만들 수 있다.

초반에는 클래스 파일이 비어 있어서 허무해 보일 수 있다. 하지만 오히려 이 단순한 예제가 "객체 생성" 자체를 분리해서 보게 해 준다.

## 4. 필드: 객체가 기억해야 할 상태를 담는 곳
`Car.java`는 day4에서 가장 중요한 예제 클래스 중 하나다.

```java
String company = "현대자동차";
String model;
String color;
boolean start;
int maxSpeed;
int speed;
int gas;
```

이런 필드들은 자동차 객체가 가져야 할 상태를 표현한다. 즉, "자동차란 무엇을 기억해야 하는가?"를 코드로 적어 둔 것이다.

`Study06.java`에서는 객체를 만든 뒤 필드를 출력해 본다.

```java
Car myCar = new Car();
System.out.println("제작회사 : " + myCar.company);
System.out.println("현재속도 : " + myCar.speed);

myCar.speed = 60;
System.out.println("현재속도 : " + myCar.speed);
```

여기서 중요한 점은 객체 안의 값이 점처럼 이어서 접근된다는 것이다.

- `myCar.company`
- `myCar.speed`

이 표기법은 "어떤 객체의 어떤 속성인가"를 명확하게 보여 준다.

### 왜 필드가 중요한가
이전까지는 `x`, `y`, `score`, `sum` 같은 독립 변수로 값을 관리했다. 그런데 객체를 쓰면 관련된 값들이 하나의 대상 안에 묶인다. 자동차의 회사명, 색상, 속도, 연료량을 따로 흩어 놓지 않고 `Car` 안에 모아 둘 수 있는 것이다.

즉, 필드는 단순한 멤버 변수가 아니라 "관련된 데이터를 한 단위로 묶는 수단"이다.

## 5. 생성자: 객체가 처음 만들어질 때 어떤 값을 받을지 정한다
day4에서 정말 본격적으로 중요해지는 부분은 생성자다. `Car.java`에는 여러 생성자가 있다.

```java
public Car() {
}

public Car(String model) {
    this(model, "은색", 250);
}

public Car(String model, String color) {
    this(model, color, 250);
}

public Car(String model, String color, int maxSpeed) {
    this.model = model;
    this.color = color;
    this.maxSpeed = maxSpeed;
}
```

`Study08.java`는 이 생성자들을 각각 호출해 본다.

```java
Car car2 = new Car("아반떼");
Car car3 = new Car("아이오닉", "파란색");
Car car4 = new Car("캐스퍼", "노란색", 200);
```

이 예제는 세 가지 개념을 동시에 보여 준다.

- 객체를 만들 때 필요한 값을 바로 넣을 수 있다.
- 생성자를 여러 형태로 만들 수 있다.
- `this(...)`를 사용해 생성자끼리 연결할 수 있다.

### `this`는 왜 필요할까
`this.model = model;` 같은 코드는 왼쪽의 필드와 오른쪽의 매개변수를 구분해 준다. 즉, "이 객체 자신의 필드에 전달받은 값을 넣는다"는 뜻이다.

생성자는 단순 초기화 문법이 아니다. "이 객체는 생성 시점에 어떤 정보가 꼭 필요한가?"를 설계하는 자리다.

## 6. 생성자로 필수값을 강제하는 법
`Korean.java`는 기본 생성자 없이 필수값을 받는 생성자만 둔다.

```java
String nation = "대한민국";
String name;
String ssn;

public Korean(String n, String s) {
    this.name = n;
    ssn = s;
}
```

`Study07.java`에서는 아래처럼 객체를 만든다.

```java
Korean k1 = new Korean("이재명", "630505-1234567");
Korean k2 = new Korean("윤석열", "600115-1345678");
```

이 구조의 장점은 분명하다. 이름과 주민등록번호 없이 어정쩡한 `Korean` 객체를 만들 수 없게 된다. 즉, 생성자가 객체의 최소 완성 조건을 보장해 준다.

이 개념은 나중에 회원 객체, 게시글 객체, 주문 객체를 만들 때도 계속 중요하다. "필수 정보 없는 객체"를 막는 가장 기본적인 방법이기 때문이다.

## 7. 메서드: 객체가 할 수 있는 동작을 분리한다
필드가 상태라면, 메서드는 행동이다. `Calculator.java`는 메서드 개념을 익히기에 아주 좋은 클래스다.

```java
void powerOn() {
    System.out.println("전원을 켭니다.");
}

int plus(int ... values) {
    int sum = 0;
    for (int i = 0; i < values.length; i++) {
        sum += values[i];
    }
    return sum;
}

double divide(int x, int y) {
    double result = (double) x / (double) y;
    return result;
}
```

`Study09.java`에서는 이 메서드들을 실제로 호출한다.

```java
Calculator myCal = new Calculator();
myCal.powerOn();
int result1 = myCal.plus(5, 6);
double result2 = myCal.divide(x, y);
myCal.powerOff();
```

여기서 배울 핵심은 다음과 같다.

- 메서드는 객체가 제공하는 기능이다.
- 매개변수는 메서드 실행에 필요한 입력값이다.
- `return`은 실행 결과를 돌려준다.
- `void`는 반환값이 없다는 뜻이다.

즉, 메서드는 단순히 코드를 줄이는 도구가 아니라 "객체가 무슨 일을 할 수 있는지"를 정의하는 인터페이스다.

## 8. 가변 인자와 오버로딩: 같은 기능을 더 유연하게 만든다
`Calculator.java`의 `plus(int ... values)`는 가변 인자 메서드다. 즉, 정수 개수를 고정하지 않고 여러 개 받을 수 있다.

`Study10.java`에서는 이렇게 호출한다.

```java
System.err.println(cal.plus(8, 2));
System.err.println(cal.plus(8, 2, 5));
System.err.println(cal.plus(8, 2, 5, 3));
```

이 코드가 보여 주는 건 단순 합계가 아니다. "하나의 메서드가 길이가 다른 입력도 처리할 수 있다"는 설계 감각이다.

또 `Study12.java`에서는 `areaRectangle()`을 두 가지 방식으로 호출한다.

```java
double result1 = myCal.areaRectangle(10);
double result2 = myCal.areaRectangle(10, 20);
```

이는 같은 메서드 이름이더라도 매개변수 개수나 타입이 다르면 서로 다른 메서드로 취급된다는 뜻이다. 이것이 오버로딩이다.

### 왜 오버로딩이 유용한가
정사각형 넓이와 직사각형 넓이는 계산 대상이 비슷하다. 이름을 완전히 다르게 짓는 대신 같은 `areaRectangle`이라는 이름 아래 입력 형태만 다르게 두면 의미가 더 자연스럽다.

즉, 오버로딩은 "관련된 동작을 같은 이름 아래 정리하는 방법"이다.

## 9. 객체는 상태와 동작을 함께 가진다
`Car.java`는 필드와 메서드를 함께 보여 주는 좋은 예제다.

```java
void setGas(int gas) {
    this.gas = gas;
}

boolean isLeftGas() {
    if (gas == 0) {
        System.out.println("gas가 없습니다.");
        return false;
    }
    System.out.println("gas가 있습니다.");
    return true;
}

void run() {
    while (true) {
        if (gas > 0) {
            System.out.println("달립니다.(gas잔량 : " + gas + ");");
            gas -= 1;
        } else {
            System.out.println("멈춥니다.(gas잔량 : " + gas + ");");
            return;
        }
    }
}
```

`Study11.java`에서는 이 동작을 실행한다.

```java
myCar.setGas(10);

if (myCar.isLeftGas()) {
    System.out.println("출발합니다.");
    myCar.run();
}
```

이 부분이 중요한 이유는 객체지향의 핵심 감각이 여기 들어 있기 때문이다.

- `gas`는 자동차의 상태다.
- `setGas()`, `isLeftGas()`, `run()`은 그 상태를 다루는 동작이다.
- 상태와 동작이 같은 클래스 안에 함께 있다.

이전까지는 변수와 로직이 흩어져 있었다면, 이제는 하나의 대상 안에 모인다. 이것이 객체지향이 주는 첫 번째 큰 이점이다.

## 10. `static`: 객체마다 필요한 값이 아니라 클래스 전체에 필요한 값
day4에서는 인스턴스 멤버뿐 아니라 정적 멤버도 등장한다. `CalculatorStatic.java`를 보면 `pi`와 메서드들이 모두 `static`이다.

```java
static double pi = 3.14159;

static int plus(int ... values) {
    int sum = 0;
    for (int i = 0; i < values.length; i++) {
        sum += values[i];
    }
    return sum;
}
```

`Study13.java`는 객체 생성 없이 바로 사용한다.

```java
double x = CalculatorStatic.pi;
System.out.println(CalculatorStatic.plus(1, 2, 3, 4, 5));
```

여기서 얻어야 하는 핵심은 이 문장이다.

"모든 객체가 공유해야 하거나, 애초에 객체 상태와 무관한 기능은 `static`으로 둘 수 있다."

예를 들어 원주율은 계산기 객체마다 다를 이유가 없다. 합계 계산도 특정 계산기 인스턴스의 상태를 쓰지 않는다. 그럴 때 `static`이 자연스럽다.

## 11. 정적 초기화 블록: 클래스가 준비될 때 한 번 실행되는 코드
`Television.java`는 정적 초기화 블록을 보여 준다.

```java
static String company = "My Company";
static String model = "LED";
static String info;

static {
    info = company + " - " + model;
}
```

`Study14.java`에서는 `Television.info`를 출력한다.

```java
System.err.println(Television.info);
```

이 구조는 클래스가 로딩될 때 한 번 실행되는 초기화 코드를 보여 준다. 지금은 문자열 두 개를 합치는 단순한 예제지만, 나중에는 복잡한 정적 설정값을 조합할 때도 같은 개념이 쓰인다.

즉, 생성자는 객체 초기화이고, 정적 초기화 블록은 클래스 자체 초기화다. 둘은 비슷해 보이지만 대상이 다르다.

## 12. 콘솔 미니 프로젝트 1: 주소록
`Study15.java`는 day4에서 가장 실제 프로그램에 가까운 예제다. 메뉴를 출력하고, 사용자 입력을 받아서 주소록 객체 배열에 저장하고, 다시 조회한다.

```java
AddressBook[] abs = new AddressBook[20];
Scanner scan = new Scanner(System.in, "EUC-KR");
```

주소록 한 건을 표현하는 클래스는 `AddressBook.java`다.

```java
static int cnt = 0;

String name;
String phone;
int age;

void showData() {
    System.out.println(this.name + "\t" + this.phone + "\t" + this.age);
}
```

프로그램의 핵심 흐름은 이렇다.

1. 주소록 객체를 담을 배열을 만든다.
2. 메뉴를 반복 출력한다.
3. 사용자가 `1`을 누르면 저장된 객체들을 순회하며 출력한다.
4. 사용자가 `2`를 누르면 새 `AddressBook` 객체를 만들고 입력값을 채운다.
5. `AddressBook.cnt`를 늘려 현재 저장 개수를 관리한다.

### 왜 이 예제가 중요한가
이 프로그램에는 이미 아주 많은 기초가 묶여 있다.

- 배열
- 반복문
- `Scanner`
- `switch`
- 클래스와 객체
- 메서드 호출
- `static` 카운터

아직 파일 저장도 없고 수정/삭제 기능도 없다. 하지만 메모리 안에서 동작하는 작은 CRUD 프로그램의 출발점은 이미 갖춘 셈이다. 입문 단계에서 이 구조를 한 번 직접 만들어 보는 것은 의미가 크다.

## 13. 콘솔 미니 프로젝트 2: 가계부
`Study16.java`와 `PinMoney.java`는 주소록보다 한 단계 더 흥미롭다. 단순 데이터 저장을 넘어서 전체 잔액이라는 "공유 상태"까지 관리하기 때문이다.

`PinMoney.java`의 핵심 필드는 아래와 같다.

```java
static int cnt = 0;
static int totalMoney = 0;

String dateStr;
String content;
int amount;
boolean isIncome;
```

생성자에서는 수입인지 지출인지에 따라 총액을 바로 갱신한다.

```java
public PinMoney(String date, String content, int amount, boolean income) {
    this.dateStr = date;
    this.content = content;
    this.amount = amount;
    this.isIncome = income;
    cnt++;
    if (income) {
        totalMoney += amount;
    } else {
        totalMoney -= amount;
    }
}
```

`Study16.java`는 메뉴 기반으로 다음 기능을 제공한다.

- `1`: 내역 조회
- `2`: 수입 등록
- `3`: 지출 등록
- `q`: 종료

이 예제의 좋은 점은 "객체가 만들어질 때 클래스 전체 상태도 함께 갱신된다"는 점을 보여 준다는 것이다. 개별 항목은 각각 하나의 `PinMoney` 객체지만, 전체 잔액은 `PinMoney.totalMoney`로 공유된다.

즉, 인스턴스 데이터와 정적 데이터가 함께 쓰이는 구조를 입문 단계에서 직접 본 셈이다.

## 14. 오늘 실습을 통해 얻은 핵심 감각
1월 8일 실습에서 꼭 남겨야 할 핵심은 아래와 같다.

- 클래스는 설계도이고 객체는 그 결과물이다.
- 필드는 객체의 상태를, 메서드는 객체의 동작을 표현한다.
- 생성자는 객체가 처음 어떤 상태로 시작할지 정한다.
- `this`는 현재 객체 자신을 가리킨다.
- 오버로딩은 비슷한 기능을 같은 이름 아래 정리하게 해 준다.
- `static`은 객체마다 따로 둘 필요 없는 값과 기능을 표현한다.
- 객체 배열을 사용하면 콘솔 프로그램도 구조적으로 만들 수 있다.

이 감각이 생기면 이후의 상속, 캡슐화, 다형성도 훨씬 자연스럽게 연결된다. 왜냐하면 그 개념들도 결국 "객체를 어떻게 더 잘 설계할 것인가"에 대한 이야기이기 때문이다.

## 오늘의 복습 질문
이 글만 읽고 아래 질문에 답할 수 있으면 day4 내용은 꽤 잘 정리된 것이다.

<details>
<summary>1. 클래스와 객체는 어떻게 다를까?</summary>

클래스는 설계도이고, 객체는 그 설계도로 실제 만들어진 인스턴스다. 클래스가 틀이라면 객체는 실제 데이터와 상태를 가진 결과물이다.

</details>

<details>
<summary>2. 생성자가 필요한 이유는 무엇일까?</summary>

객체가 만들어질 때 필요한 초기 상태를 바로 넣기 위해서다. 생성자가 있으면 객체가 의미 있는 상태로 시작할 수 있다.

</details>

<details>
<summary>3. <code>this.model = model;</code>에서 왼쪽과 오른쪽은 각각 무엇을 가리킬까?</summary>

왼쪽 `this.model`은 현재 객체의 필드이고, 오른쪽 `model`은 생성자나 메서드에 전달된 매개변수다.

</details>

<details>
<summary>4. <code>static</code> 필드와 일반 필드는 어떤 차이가 있을까?</summary>

`static` 필드는 객체 전체가 아니라 클래스 차원에서 하나만 공유되고, 일반 필드는 객체마다 따로 존재한다.

</details>

<details>
<summary>5. 주소록 프로그램에서 <code>AddressBook.cnt</code> 같은 정적 변수를 둔 이유는 무엇일까?</summary>

전체 주소록 항목 수처럼 모든 객체가 함께 봐야 하는 공통 상태를 관리하기 위해서다. 이런 값은 객체별로 따로 두는 것보다 클래스 하나에 두는 편이 자연스럽다.

</details>

<details>
<summary>6. 가계부 프로그램에서 <code>PinMoney.totalMoney</code>는 왜 객체마다 따로 있지 않고 클래스에 붙어 있을까?</summary>

전체 잔액은 개별 지출/수입 객체 하나의 상태가 아니라 모든 내역이 함께 만든 공유 결과이기 때문이다.

</details>

## 마무리
1월 8일은 자바 학습에서 꽤 중요한 전환점이다. 앞선 3일 동안 배운 변수, 조건문, 반복문, 문자열, 배열이 이날부터 클래스와 객체 안으로 묶이기 시작했기 때문이다. 단순히 문법 항목 하나를 더 배운 것이 아니라, 프로그램을 구성하는 방식 자체가 바뀌기 시작한 날이라고 보는 편이 맞다.

특히 주소록과 가계부처럼 작은 콘솔 프로젝트를 직접 만드는 흐름은 이후 파일 저장, 컬렉션, GUI, 데이터베이스 학습으로 자연스럽게 이어진다. day4는 "객체지향 문법 소개"가 아니라 "실제 프로그램을 객체 단위로 나누어 생각하는 첫날"이었다.
