---
title: "Java 학습 7일차: 예외 처리, 일반 API, 제네릭, 와일드카드"
slug: java-study-day7-exceptions-generics-and-threads
date: 2026-01-14
author: Evan Yoon
category: study
subcategory: self-study
description: "1월 14일 Java 학습 기록. try-catch-finally, 예외 떠넘기기와 사용자 정의 예외, 일반 API와 래퍼 클래스, 제네릭 클래스/메서드, 제한된 타입 매개변수와 와일드카드까지 실습 내용을 자세히 정리했다."
tags:
  - java
  - study
  - exception
  - generics
  - api
  - wrapper-class
  - wildcard
readTime: 16
series: Java Study
seriesOrder: 7
featured: false
draft: false
toc: true
---

## 오늘은 "코드가 실패할 때"와 "코드가 더 일반화될 때"를 함께 배운 날이었다
`reference/javastudy/day0114/src/javaStudy`에는 `Account`, `InsufficientExeption`, `Box`, `Product`, `Rentable`, `Applicant`, `Course` 같은 파일이 들어 있다. 앞의 학습들이 클래스, 상속, 인터페이스처럼 객체지향 구조를 만드는 데 집중했다면, 이날은 그 구조를 더 안전하고 재사용 가능하게 만드는 도구들을 배우는 날이었다.

이날 학습 흐름을 크게 나누면 이렇다.

1. 예외가 왜 발생하는지 직접 보고 `try-catch-finally`로 처리한다.
2. 체크 예외와 `throws`를 통해 예외를 호출한 쪽으로 넘긴다.
3. 사용자 정의 예외로 비즈니스 규칙 위반을 표현한다.
4. 래퍼 클래스와 기본 API로 값과 타입을 더 유연하게 다룬다.
5. 제네릭으로 타입 안전성을 높인다.
6. 제한된 타입 매개변수와 와일드카드로 더 정교한 타입 제약을 건다.

즉, day7은 "문법을 배운다"보다 "안전하고 재사용 가능한 자바 코드를 어떻게 만드는가"에 더 가까운 날이었다.

## 1. 예외 처리의 출발점: 프로그램은 언제든 실패할 수 있다
`Study01.java`는 아주 기본적인 `NullPointerException` 예제를 보여 준다.

```java
public static void printLength(String data) {
    try{
        int result = data.length();
        System.out.println("문자 수 :" + result);
    } catch (NullPointerException e) {
        System.out.println(e.getMessage());
    } finally {
        System.out.println("[마무리 실행]\n");
    }
}
```

`main()`에서는 정상 문자열과 `null`을 각각 넘긴다.

```java
printLength("I am Ironman");
printLength(null);
```

이 예제의 핵심은 세 가지다.

- `try`: 예외가 발생할 수 있는 코드를 감싼다.
- `catch`: 특정 예외가 발생했을 때 처리한다.
- `finally`: 예외 발생 여부와 상관없이 마지막에 실행된다.

### 왜 `finally`가 중요할까
입문 단계에서는 단순 로그처럼 보이지만, 실제로는 파일 닫기, 네트워크 연결 해제, 자원 반납 같은 정리 작업이 여기에 들어간다. 즉, `finally`는 "실패했든 성공했든 반드시 마무리해야 하는 일"을 담는 자리다.

## 2. 예외를 직접 처리할 수도 있고, 호출한 쪽으로 넘길 수도 있다
`Study02.java`와 `Study04.java`는 `Class.forName()`을 사용해 체크 예외를 다룬다.

`Study02.java`는 발생 지점에서 직접 처리한다.

```java
try {
    Class.forName("java.lang.String");
    System.out.println("java.lang.String 클래스가 존재합니다.");
} catch (ClassNotFoundException e) {
    e.printStackTrace();
}
```

존재하지 않는 클래스 이름을 넣으면 예외가 발생한다.

```java
Class.forName("java.lang.String2");
```

`Study04.java`는 방식을 바꾼다.

```java
public static void findClass() throws ClassNotFoundException {
    Class.forName("java.lang.String2");
}
```

그리고 호출한 쪽에서 받는다.

```java
try {
    findClass();
} catch (ClassNotFoundException e) {
    System.out.println("예외 처리: " + e.toString());
}
```

### `throws`는 무슨 뜻일까
메서드 안에서 해결하지 않고, "이 메서드는 이런 예외를 발생시킬 수 있으니 호출한 쪽이 처리해 달라"라고 알리는 것이다. 즉, 예외를 숨기지 않고 책임을 위로 넘기는 방식이다.

이 개념은 파일 입출력, DB 연결, 네트워크 코드처럼 체크 예외가 많은 영역에서 매우 자주 등장한다.

## 3. 하나의 코드에서 여러 종류의 예외를 다루기
`Study03.java`는 day7에서 꽤 중요한 예제다.

```java
String[] array = {"100", "1oo", null, "200"};

for(int i = 0; i <= array.length; i++) {
    try {
        int value = Integer.parseInt(array[i]);
        System.out.println("Integer value: " + value);
    } catch (ArrayIndexOutOfBoundsException e) {
        System.out.println("배열 인덱스가 초과됨: " + e.getMessage());
    } catch (NullPointerException | NumberFormatException e){
        System.out.println("데이터에 문제가 있음: " + e.getMessage());
    }
}
```

이 예제 안에는 일부러 여러 문제가 섞여 있다.

- `"1oo"`: 숫자 변환 실패
- `null`: 널 참조 문제
- `i <= array.length`: 마지막 순회에서 배열 범위 초과

### 여기서 배워야 할 핵심
- 예외는 하나만 발생하는 게 아니다.
- 어떤 예외는 따로 처리하고, 어떤 예외는 함께 묶어 처리할 수 있다.
- 멀티 캐치(`A | B`)를 사용하면 비슷한 처리 로직을 합칠 수 있다.

즉, 예외 처리는 "문제가 생기면 잡는다"가 아니라 "어떤 종류의 실패가 가능한지 미리 분류하고 대응한다"는 사고방식이다.

## 4. 사용자 정의 예외: 비즈니스 규칙 위반도 예외로 표현할 수 있다
`Study05.java`와 `Account.java`는 가장 실용적인 예제다. 계좌 출금 시 잔액이 부족하면 커스텀 예외를 던진다.

`Account.java`:

```java
public void withdraw(int money) throws InsufficientExeption {
    if(balance < money) {
        throw new InsufficientException("잔고 부족: " + (money - balance) + " 부족");
    }
    balance -= money;
}
```

사용 코드:

```java
account.deposit(10000);
System.out.println("예금액 : " + account.getBalance());

try {
    account.withdraw(30000);
} catch (InsufficientExeption e) {
    String message = e.getMessage();
    System.out.println(message);
}
```

이 예제는 코드에 오타가 조금 섞여 있지만, 학습 포인트는 분명하다.

- 시스템 오류만 예외인 것은 아니다.
- "잔액보다 많이 출금하면 안 된다" 같은 업무 규칙도 예외로 표현할 수 있다.

### 왜 커스텀 예외가 유용할까
단순히 `Exception`으로 처리하면 어떤 문제인지 이름만 보고 알기 어렵다. 하지만 `InsufficientException` 같은 이름을 쓰면 코드만 읽어도 "잔고 부족 상황"이라는 의미가 바로 드러난다.

즉, 커스텀 예외는 오류 정보를 더 도메인 친화적으로 만드는 도구다.

## 5. 일반 API와 래퍼 클래스: 값과 타입을 객체처럼 다루기
day7에서는 제네릭으로 넘어가기 전에 기본 API 감각도 같이 익힌다. `Study11.java`는 `Integer`를 사용한다.

```java
Integer obj = 100;
System.out.println("value: " + obj.intValue());

int value = obj;
System.out.println("value: " + value);

int result = obj + 100;
System.out.println("result: " + result);
```

여기서 중요한 개념은 세 가지다.

- 래퍼 클래스: 기본형을 객체 형태로 감싼 클래스
- 오토박싱: `int -> Integer`
- 오토언박싱: `Integer -> int`

### 왜 래퍼 클래스가 필요할까
자바의 제네릭은 기본형을 직접 담을 수 없다. 예를 들어 `Box<int>`는 안 되고 `Box<Integer>`는 된다. 따라서 컬렉션이나 제네릭을 쓰기 시작하면 래퍼 클래스 이해가 반드시 필요하다.

또 `Study12.java`의 `getClass().getSimpleName()`처럼 객체 메서드를 사용할 수 있다는 점도 중요하다. 기본형은 이런 메서드를 직접 가질 수 없기 때문이다.

## 6. 제네릭 클래스: 타입을 나중에 정하는 설계
`Study06.java`는 제네릭의 가장 기본적인 형태를 보여 준다.

```java
Box<String> box1 = new Box<>();
box1.content = "안녕하세요.";
String str = box1.content;

Box<Integer> box2 = new Box<>();
box2.content = 100;
int value = box2.content;
```

`Box.java`는 이렇게 정의되어 있다.

```java
public class Box<T> {
    public T content;
    private T t;
}
```

여기서 `T`는 실제 타입이 아니다. 나중에 사용하는 쪽에서 정하는 타입 자리다.

### 왜 제네릭이 필요한가
제네릭이 없으면 `Object`로 받아야 하고, 꺼낼 때마다 캐스팅이 필요하다. 그러면 코드가 번거롭고 타입 실수도 런타임까지 가서야 드러난다.

하지만 `Box<String>`, `Box<Integer>`처럼 타입을 미리 정하면:

- 잘못된 타입 대입을 컴파일 단계에서 막을 수 있고
- 꺼낼 때 캐스팅이 필요 없고
- 코드 의미가 더 분명해진다

즉, 제네릭은 재사용성과 타입 안전성을 동시에 얻는 도구다.

## 7. 타입 매개변수가 여러 개인 제네릭
`Study07.java`는 `Product<K, M>`를 사용한다.

```java
Product<TV, String> product1 = new Product<>();
product1.setKind(new TV());
product1.setModel("Smart TV");

Product<Car, String> product2 = new Product<>();
product2.setKind(new Car());
product2.setModel("SUV");
```

`Product.java`는 이렇게 생겼다.

```java
public class Product<K, M>{
    public K kind;
    public M model;
}
```

이 구조는 제품 종류와 모델명을 각각 다른 타입으로 관리할 수 있게 해 준다. 즉, 제네릭은 타입 하나만 일반화하는 데 그치지 않는다. 여러 정보를 각기 다른 타입 파라미터로 분리해 설계할 수도 있다.

## 8. 제네릭 인터페이스: 반환 타입도 일반화할 수 있다
`Study08.java`는 `Rentable<P>` 인터페이스를 사용한다.

```java
public interface Rentable<P> {
    P rent();
}
```

구현 클래스는 각각 다르다.

```java
public class HomeAgency implements Rentable<Home> {
    @Override
    public Home rent() {
        return new Home();
    }
}
```

```java
public class CarAgency implements Rentable<Car> {
    @Override
    public Car rent() {
        return new Car();
    }
}
```

사용 코드는 이렇다.

```java
HomeAgency homeAgency = new HomeAgency();
Home home = homeAgency.rent();
home.turnOnLight();

CarAgency carAgency = new CarAgency();
Car car = carAgency.rent();
car.run();
```

이 예제의 핵심은 "대여한다"는 행위는 같지만, 빌려주는 대상 타입은 다를 수 있다는 점을 타입 수준에서 표현했다는 것이다.

즉, 제네릭은 단순 자료 보관뿐 아니라 역할 인터페이스에도 자연스럽게 들어갈 수 있다.

## 9. 같은 타입끼리만 비교되게 만드는 제네릭
`Study09.java`는 `Box.compare()`를 이용한다.

```java
Box box1 = new Box();
box1.content = "100";

Box box2 = new Box();
box2.content = "100";

Box box3 = new Box();
box3.content = 100;
```

`Box.java` 안의 비교 메서드는 이렇게 정의돼 있다.

```java
public boolean compare(Box<T> other) {
    boolean result = this.content.equals(other.content);
    return result;
}
```

의도 자체는 중요하다. 같은 타입의 박스끼리만 의미 있게 비교하겠다는 것이다. 실제 사용 코드에서는 raw type을 써서 제네릭 장점이 조금 줄어들지만, 학습 포인트는 "타입 파라미터를 이용해 비교 대상도 같은 타입으로 묶을 수 있다"는 점이다.

## 10. 제네릭 메서드: 클래스 전체가 아니라 메서드만 일반화하기
`Study10.java`는 제네릭 메서드를 보여 준다.

```java
public static <T> Box<T> boxing(T t) {
    Box<T> box = new Box<>();
    box.set(t);
    return box;
}
```

호출은 이렇게 한다.

```java
Box<Integer> box1 = boxing(100);
Box<String> box2 = boxing("이재명");
```

여기서 중요한 건 `Box` 클래스 자체가 제네릭이라는 점과 별개로, `boxing()` 메서드도 독립적으로 제네릭이라는 것이다.

즉, 제네릭은 클래스 단위로만 쓰는 것이 아니라 "이 메서드는 어떤 타입이 오든 같은 패턴으로 처리할 수 있다"는 경우에도 쓸 수 있다.

## 11. 제한된 타입 매개변수: 아무 타입이나 받지 않게 만들기
`Study12.java`는 제네릭을 더 정교하게 제한한다.

```java
public static <T extends Number> boolean compare(T t1, T t2) {
    double v1 = t1.doubleValue();
    double v2 = t2.doubleValue();
    return (v1 == v2);
}
```

여기서 `T extends Number`는 매우 중요하다. 아무 타입이나 받는 게 아니라 `Number`의 자손만 받겠다는 뜻이다.

이렇게 제한하면 메서드 안에서 안심하고 `doubleValue()`를 호출할 수 있다. 왜냐하면 `Number` 계열이면 그 메서드를 가진다는 걸 컴파일러도 알기 때문이다.

### 왜 이런 제한이 필요할까
제네릭을 무제한으로 열어 두면 메서드 안에서 할 수 있는 일이 오히려 적어진다. 타입이 너무 다양해서 공통 메서드를 보장할 수 없기 때문이다. 그래서 "필요한 범위까지만 허용"하는 제약이 중요하다.

## 12. 와일드카드: 허용 범위를 더 유연하게 표현하기
`Study13.java`와 `Course.java`는 와일드카드의 대표 예제다.

`Applicant<T>`는 지원자 타입을 담고 있다.

```java
public class Applicant<T> {
    public T kind;

    public Applicant(T kind){
        this.kind = kind;
    }
}
```

`Course.java`는 세 종류의 등록 메서드를 가진다.

```java
public static void registerCourse1(Applicant<?> applicant)
public static void registerCourse2(Applicant<? extends Student> applicant)
public static void registerCourse3(Applicant<? super Worker> applicant)
```

각각 의미가 다르다.

- `<?>`: 아무 타입이나 가능
- `<? extends Student>`: `Student`와 그 하위 타입만 가능
- `<? super Worker>`: `Worker`와 그 상위 타입만 가능

`Study13.java`는 이 차이를 실제 등록 예제로 보여 준다.

```java
Course.registerCourse2(new Applicant<Student>(new Student()));
Course.registerCourse2(new Applicant<HighStudent>(new HighStudent()));
```

반면 아래 코드는 허용되지 않는다.

```java
// Course.registerCourse2(new Applicant<Person>(new Person()));
```

### 왜 와일드카드가 중요할까
이건 단순 문법이 아니라 "이 메서드는 어떤 범위의 타입까지 받아야 하는가"를 정교하게 설계하는 도구다. 학생 전용 과정, 일반인 과정, 직장인 과정처럼 비즈니스 규칙을 타입 시스템으로 표현할 수 있다.

즉, 와일드카드는 제네릭을 훨씬 실용적으로 만들어 준다.

## 13. day7의 실제 중심은 스레드보다 예외와 제네릭이었다
파일명에는 `threads`가 포함되어 있지만, 실제 `day0114` 실습 소스 기준으로 이날 중심은 예외 처리, 일반 API, 제네릭, 와일드카드였다. 그래서 이 날을 이해할 때는 "프로그램의 실패를 안전하게 처리하고, 타입을 일반화하는 법을 배운 날"로 보는 편이 정확하다.

이런 보정은 중요하다. 나중에 복습할 때 파일명만 보고 내용을 오해하면 학습 흐름이 헷갈릴 수 있기 때문이다.

## 14. 오늘 실습을 통해 얻은 핵심 감각
1월 14일 실습에서 꼭 남겨야 할 핵심은 아래와 같다.

- 예외는 프로그램 실패를 코드 안에서 관리하는 도구다.
- `try-catch-finally`는 처리, 분기, 마무리를 역할별로 나눈다.
- `throws`는 예외 처리 책임을 호출한 쪽으로 넘긴다.
- 사용자 정의 예외는 도메인 규칙 위반을 더 명확하게 표현한다.
- 래퍼 클래스는 기본형을 객체처럼 다루게 해 준다.
- 제네릭은 타입 안전성과 재사용성을 높인다.
- 제네릭 메서드는 메서드 단위 일반화를 가능하게 한다.
- `T extends Number` 같은 제한은 필요한 범위만 허용하게 해 준다.
- 와일드카드는 허용 타입 범위를 더 세밀하게 설계하게 해 준다.

이 감각은 이후 컬렉션, 파일 입출력, 스레드, 네트워크, 프레임워크를 배울 때 계속 중요하게 작동한다.

## 오늘의 복습 질문
이 글만 읽고 아래 질문에 답할 수 있으면 day7 내용은 잘 정리된 것이다.

<details>
<summary>1. <code>try</code>, <code>catch</code>, <code>finally</code>는 각각 어떤 역할을 할까?</summary>

`try`는 예외가 발생할 수 있는 코드를 실행하고, `catch`는 발생한 예외를 처리하며, `finally`는 예외 여부와 관계없이 마무리 작업을 수행한다.

</details>

<details>
<summary>2. <code>throws ClassNotFoundException</code>은 무엇을 의미할까?</summary>

이 메서드가 해당 예외를 직접 처리하지 않고, 호출한 쪽으로 처리 책임을 넘긴다는 뜻이다.

</details>

<details>
<summary>3. 멀티 캐치는 어떤 상황에서 유용할까?</summary>

서로 다른 예외를 같은 방식으로 처리할 때 유용하다. 중복 `catch` 블록을 줄여 코드를 간결하게 만들 수 있다.

</details>

<details>
<summary>4. 사용자 정의 예외는 왜 <code>Exception</code> 하나로 통일하지 않고 따로 만들까?</summary>

도메인 규칙 위반 원인을 더 명확히 표현하기 위해서다. 예외 이름만 봐도 어떤 문제가 발생했는지 바로 이해하기 쉬워진다.

</details>

<details>
<summary>5. 왜 제네릭을 쓰면 캐스팅이 줄고 타입 실수가 빨리 드러날까?</summary>

컴파일러가 타입을 미리 검사해 주기 때문이다. 잘못된 타입 사용이 실행 전부터 드러나고, 꺼낼 때도 명시적 캐스팅이 줄어든다.

</details>

<details>
<summary>6. 제네릭 클래스와 제네릭 메서드는 어떻게 다를까?</summary>

제네릭 클래스는 클래스 전체에서 타입 매개변수를 쓰고, 제네릭 메서드는 특정 메서드 하나 안에서만 타입 매개변수를 사용한다.

</details>

<details>
<summary>7. <code>T extends Number</code>를 붙이면 메서드 안에서 어떤 이점이 생길까?</summary>

숫자 타입만 받도록 제한할 수 있어 `doubleValue()` 같은 `Number` 계열 기능을 안전하게 사용할 수 있다.

</details>

<details>
<summary>8. <code>&lt;? extends Student&gt;</code>와 <code>&lt;? super Worker&gt;</code>는 각각 어떤 범위를 허용할까?</summary>

`<? extends Student>`는 `Student`와 그 하위 타입까지, `<? super Worker>`는 `Worker`와 그 상위 타입까지 허용한다.

</details>

## 마무리
1월 14일은 자바 코드가 한층 더 현실적으로 보이기 시작한 날이었다. 단순히 객체를 만들고 상속하는 데서 멈추지 않고, 실패 상황을 제어하고, 타입을 일반화하고, 잘못된 사용을 컴파일 단계에서 막는 방법까지 배우기 시작했기 때문이다.

즉, day7은 "문법을 더 배운 날"이라기보다 "코드를 더 안전하고 재사용 가능하게 만드는 법을 배운 날"이었다. 이후 컬렉션, 스레드, IO, 네트워크를 공부할수록 이날의 예외 처리와 제네릭 감각은 더 자주, 더 중요하게 반복된다.
