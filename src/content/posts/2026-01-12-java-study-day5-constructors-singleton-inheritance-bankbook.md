---
title: "Java 학습 5일차: final, 캡슐화, 싱글턴, 상속, 다형성, 은행장부"
slug: java-study-day5-constructors-singleton-inheritance-bankbook
date: 2026-01-12
author: Evan Yoon
category: study
subcategory: self-study
description: "1월 12일 Java 학습 기록. final 필드와 상수, getter/setter와 캡슐화, 싱글턴 패턴, 상속과 오버라이딩, 자동 타입 변환과 강제 캐스팅, 은행장부 콘솔 프로그램까지 실습 내용을 자세히 정리했다."
tags:
  - java
  - study
  - final
  - encapsulation
  - inheritance
  - singleton
  - polymorphism
  - overriding
readTime: 16
series: Java Study
seriesOrder: 5
featured: false
draft: false
toc: true
---

## 오늘은 객체를 "만드는 것"에서 "관계를 맺게 하는 것"으로 넘어간 날이었다
`reference/javastudy/day0112/src/javaStudy`에는 `Singleton`, `Bank`, `BankTuple`, `Phone`, `SmartPhone`, `Calculator`, `Computer`, `Airplane`, `SuperSonicAirplane`, `Parent`, `Child`, `Tire`, `HankookTire`, `KumhoTire` 같은 클래스가 들어 있다. day4가 클래스와 객체, 생성자, 메서드를 처음 손에 익히는 날이었다면, day5는 그 객체들 사이에 관계를 만들고 규칙을 부여하는 날이었다.

이날 학습 흐름을 크게 나누면 이렇다.

1. `final` 필드와 상수로 변하지 않는 값을 다룬다.
2. `private` 필드와 getter/setter로 캡슐화를 익힌다.
3. 싱글턴 패턴으로 객체 생성을 하나로 제한한다.
4. 상속으로 기존 클래스를 재사용한다.
5. 오버라이딩으로 자식 클래스가 동작을 바꾼다.
6. 부모 타입과 자식 타입의 자동 변환, 강제 캐스팅, 다형성을 다룬다.
7. 은행장부 콘솔 프로그램으로 중앙 관리 구조를 실제 코드처럼 사용한다.

즉, day5는 객체지향이 "문법"이 아니라 "설계 방식"으로 보이기 시작하는 날이었다.

## 1. `final` 필드: 한 번 정해지면 바뀌지 않는 값
`Study01.java`는 `Korean.java`를 통해 `final` 필드를 다룬다.

```java
public class Korean {
    final String nation = "대한민국";
    final String ssn;
    String name;

    public Korean(String ssn, String name) {
        this.ssn = ssn;
        this.name = name;
    }
}
```

실행 코드는 이렇다.

```java
Korean k1 = new Korean("940608-1234567", "이재명");
System.out.println(k1.nation);
System.out.println(k1.ssn);
System.out.println(k1.name);
```

여기서 중요한 점은 `nation`과 `ssn`은 바꿀 수 없고, `name`은 바꿀 수 있다는 것이다.

- `nation`: 선언 시 바로 값이 정해진 `final` 필드
- `ssn`: 생성자에서 한 번만 값이 정해지는 `final` 필드
- `name`: 일반 필드라서 이후 수정 가능

### 왜 `final`이 필요할까
모든 값을 자유롭게 바꿀 수 있게 두면 편해 보이지만, 실제로는 바뀌면 안 되는 데이터도 많다. 주민등록번호, 회원 고유 ID, 생성일 같은 값은 한 번 정해지면 바뀌지 않는 편이 안전하다.

즉, `final`은 단순 제약이 아니라 "이 값은 불변이어야 한다"는 설계 의도를 코드에 명시하는 도구다.

## 2. 정적 상수: 프로그램 전체에서 공유하는 고정값
`Study02.java`는 `Earth.java`를 사용한다.

```java
public class Earth {
    static final double EARTH_RADIUS = 6400;
    static final double EARTH_SURFACE_AREA;

    static {
        EARTH_SURFACE_AREA = 4 * Math.PI * EARTH_RADIUS * EARTH_RADIUS;
    }
}
```

사용할 때는 객체를 만들지 않는다.

```java
System.out.println("지구 반지름: " + Earth.EARTH_RADIUS + " km");
System.out.println("지구 표면적: " + Earth.EARTH_SURFACE_AREA + " km²");
```

이 예제는 세 가지 개념을 동시에 보여 준다.

- `static`: 객체와 무관하게 클래스 차원에서 공유
- `final`: 실행 중 바뀌지 않음
- 정적 초기화 블록: 계산이 필요한 상수를 한 번만 초기화

### 왜 상수 이름을 대문자로 쓸까
`EARTH_RADIUS`, `EARTH_SURFACE_AREA`처럼 모두 대문자로 쓰는 이유는 "이 값은 상수"라는 점을 한눈에 드러내기 위해서다. 자바에서 흔히 쓰는 관례이고, 코드 가독성을 크게 높여 준다.

## 3. 캡슐화: 필드를 직접 건드리지 않고 메서드로 제어하기
`Study03.java`와 `Car.java`는 캡슐화의 출발점을 보여 준다.

`Car.java`를 보면 속도와 정지 상태가 `private`로 숨겨져 있다.

```java
private int speed;
private boolean stop;
public Tire tire;
```

대신 접근 메서드가 있다.

```java
public int getSpeed() {
    return this.speed;
}

public void setSpeed(int speed) {
    if (speed < 0) {
        this.speed = 0;
    } else {
        this.speed = speed;
    }
}

public boolean isStop() {
    return this.stop;
}

public void setStop(boolean stop) {
    this.stop = stop;
    if (stop == true)
        this.speed = 0;
}
```

`Study03.java`는 이 메서드들을 사용한다.

```java
myCar.setSpeed(50);
System.out.println("현재 속도: " + myCar.getSpeed() + "km/h");
```

### 왜 직접 접근을 막을까
만약 `speed`가 공개돼 있다면 외부에서 음수 속도 같은 이상한 값도 그냥 넣을 수 있다. 하지만 `setSpeed()` 안에 규칙을 두면 잘못된 값은 자동으로 0으로 보정할 수 있다.

즉, 캡슐화는 "숨긴다"가 핵심이 아니라 "객체 내부 규칙을 보호한다"가 핵심이다.

## 4. `final` 메서드: 자식이 바꾸면 안 되는 동작 막기
`Car.java`에는 이런 메서드도 있다.

```java
public final void stop() {
    System.out.println("차를 멈춥니다.");
    speed = 0;
}
```

여기서 `final`은 필드가 아니라 메서드에 붙어 있다. 의미는 다르다.

- `final` 필드: 값을 바꾸지 못하게 함
- `final` 메서드: 자식 클래스가 오버라이딩하지 못하게 함

왜 이런 제한이 필요할까? 어떤 동작은 클래스 설계상 반드시 동일해야 하기 때문이다. 예를 들어 자동차 정지 로직은 자식 클래스가 임의로 바꾸면 위험할 수 있다. 그럴 때 `final` 메서드가 유용하다.

## 5. 싱글턴: 객체를 하나만 만들게 제한하는 패턴
`Study04.java`는 `Singleton.java`를 사용한다.

```java
public class Singleton {
    private static Singleton singleton = new Singleton();

    private Singleton(){}

    public static Singleton getInstance(){
        return singleton;
    }
}
```

핵심 구조는 매우 단순하다.

1. 자기 자신 타입의 정적 인스턴스를 하나 만든다.
2. 생성자를 `private`으로 막는다.
3. 외부에서는 `getInstance()`로만 접근하게 한다.

`Study04.java`에서는 아래처럼 확인한다.

```java
Singleton obj1 = Singleton.getInstance();
Singleton obj2 = Singleton.getInstance();

if (obj1 == obj2) {
    System.out.println("같은 Singleton 객체입니다.");
}
```

### 왜 싱글턴을 쓸까
프로그램 전체에서 하나만 있어야 하는 객체가 있다. 설정 관리자, 공용 로그 관리자, 전체 은행장부 관리자처럼 중앙에서 상태를 관리해야 하는 대상이 그렇다. 이런 경우 객체가 여러 개 생기면 오히려 상태가 흩어져 문제가 된다.

즉, 싱글턴은 "언제나 좋은 패턴"이 아니라 "정말 하나만 있어야 할 때 쓰는 패턴"이라고 이해하는 편이 좋다.

## 6. 은행장부 프로젝트: 싱글턴과 캡슐화가 실제 구조로 연결된다
`Study05.java`, `Bank.java`, `BankTuple.java`는 day5의 실전 예제다. 입금/출금 내역을 저장하고 조회하는 콘솔 프로그램인데, 단순히 메뉴만 있는 게 아니라 구조가 꽤 객체지향적으로 짜여 있다.

`BankTuple.java`는 한 건의 거래 내역을 표현한다.

```java
String date;
String content;
boolean isIncome;
int amount;
```

그리고 setter/getter로 값을 다룬다.

```java
public void setDate(String date) {
    this.date = date;
}

public boolean isIncome() {
    return isIncome;
}
```

`Bank.java`는 전체 장부를 관리한다.

```java
private static Bank bank = new Bank();
private BankTuple[] bankBook = new BankTuple[100];
private int cnt = 0;

private Bank(){}

public static Bank getBank() {
    return bank;
}
```

거래 저장 메서드는 이렇다.

```java
public void setBankTuple(BankTuple bt){
    if (cnt < bankBook.length) {
        bank.bankBook[cnt++] = bt;
    }
}
```

조회 메서드는 누적 잔액을 계산하며 출력한다.

```java
if (bank.bankBook[i].isIncome()) {
    sum += bank.bankBook[i].getAmount();
} else {
    sum -= bank.bankBook[i].getAmount();
}
```

### 이 구조가 왜 좋은가
이 예제는 day4의 가계부보다 한 단계 더 정리돼 있다.

- 거래 한 건은 `BankTuple`이 담당한다.
- 전체 목록과 저장 로직은 `Bank`가 담당한다.
- `Bank`는 싱글턴으로 하나만 존재한다.
- 외부에서는 `Bank.getBank().setBankTuple(...)`처럼 정해진 통로로만 접근한다.

즉, 데이터와 관리 책임이 분리돼 있다. 아직 DB도 파일 저장도 없지만, "모델 객체"와 "관리 객체"를 나누는 감각은 이미 들어 있다.

## 7. 상속: 기존 클래스를 확장해서 재사용하기
day5의 두 번째 큰 축은 상속이다. `Phone`과 `SmartPhone`이 가장 직관적이다.

부모 클래스:

```java
public class Phone {
    public String model;
    public String color;

    public void bell(){ ... }
    public void sendVoice(String message){ ... }
    public void receiveVoice(String message){ ... }
    public void hangUp(){ ... }
}
```

자식 클래스:

```java
public class SmartPhone extends Phone {
    public boolean wifi;

    public SmartPhone(String model, String color) {
        super();
        this.model = model;
        this.color = color;
    }
}
```

`Study06.java`에서는 스마트폰 객체를 만든 뒤, 부모에게서 물려받은 전화 기능과 자식 고유 기능을 함께 사용한다.

```java
myPhone.bell();
myPhone.sendVoice("여보세요");
myPhone.setWifi(true);
myPhone.internet();
```

여기서 핵심은 이것이다.

- `SmartPhone`은 `Phone`의 기능을 물려받는다.
- 추가로 `wifi`, `setWifi()`, `internet()` 같은 자기 기능도 가진다.

즉, 상속은 "복붙"이 아니라 "기존 클래스를 기반으로 더 구체적인 클래스를 만드는 방식"이다.

## 8. `super()`: 부모 생성자를 먼저 호출한다
`SmartPhone` 생성자 안에는 `super();`가 있다.

```java
public SmartPhone(String model, String color) {
    super();
    this.model = model;
    this.color = color;
    System.out.println("SmartPhone(String model, String color) 생성자 호출");
}
```

그리고 `Phone`에는 기본 생성자가 있다.

```java
public Phone() {
    System.out.println("Phone 생성자 호출");
}
```

`Study06.java`, `Study07.java`를 실행하면 부모 생성자가 먼저, 자식 생성자가 나중에 호출되는 흐름을 확인할 수 있다.

### 왜 부모 생성자를 먼저 부를까
자식 객체는 부모의 속성과 기능도 함께 가지기 때문이다. 즉, 자식이 완성되기 전에 부모 부분이 먼저 준비되어야 한다. 이 순서를 이해하면 나중에 복잡한 상속 구조도 훨씬 잘 읽힌다.

## 9. 오버라이딩: 물려받은 메서드를 자식에게 맞게 바꾸기
`Study08.java`는 `Calculator`와 `Computer`를 비교한다.

부모 클래스:

```java
public double areaCircle(double r) {
    System.out.println("Calculator 객체의 areaCircle() 실행");
    return 3.14159 * r * r;
}
```

자식 클래스:

```java
@Override
public double areaCircle(double r) {
    System.out.println("Computer 객체의 areaCircle() 실행");
    return Math.PI * r * r;
}
```

같은 `areaCircle()` 메서드지만 자식 클래스가 더 정확한 방식으로 다시 정의했다. 이것이 오버라이딩이다.

### 오버로딩과 헷갈리지 말 것
day4에서 배운 오버로딩은 "같은 클래스 안에서 이름은 같고 매개변수만 다른 메서드 여러 개"였다.

day5의 오버라이딩은 전혀 다르다.

- 오버로딩: 같은 이름, 다른 매개변수
- 오버라이딩: 상속 관계에서 부모 메서드를 자식이 다시 정의

둘을 구분해서 이해해야 이후 객체지향 개념이 덜 헷갈린다.

## 10. `super`로 부모 동작을 다시 사용할 수 있다
`SuperSonicAirplane.java`는 오버라이딩과 함께 `super` 사용도 보여 준다.

```java
public class SuperSonicAirplane extends Airplane {
    public static final int NORMAL = 1;
    public static final int SUPERSONIC = 2;
    public int flyMode = NORMAL;

    @Override
    public void fly() {
        if (flyMode == SUPERSONIC) {
            System.out.println("초음속 비행합니다.");
            return;
        } else {
            super.fly();
        }
    }
}
```

`Study09.java`에서는 비행 모드를 바꾸며 같은 `fly()` 호출이 다른 결과를 내는 것을 본다.

```java
ssa.fly();
ssa.flyMode = SuperSonicAirplane.SUPERSONIC;
ssa.fly();
ssa.flyMode = SuperSonicAirplane.NORMAL;
ssa.fly();
```

이 예제의 핵심은 자식이 부모 기능을 완전히 버리는 게 아니라 필요할 때는 `super.fly()`로 다시 사용할 수 있다는 점이다.

즉, 오버라이딩은 "부모 기능을 없애는 것"이 아니라 "상황에 따라 바꾸거나 확장하는 것"이다.

## 11. 자동 타입 변환: 부모 타입으로 자식 객체를 담을 수 있다
`Study10.java`는 상속 계층에서 자동 타입 변환을 보여 준다.

```java
A a1 = b;
A a2 = c;
A a3 = d;
A a4 = e;

B b1 = d;
C c1 = e;
```

이게 가능한 이유는 간단하다. `D`는 `B`의 자식이고, `B`는 `A`의 자식이므로 `D` 객체는 동시에 `B`이면서 `A`이기도 하다.

즉, 더 큰 범주의 타입으로 담는 것은 자연스럽다. 이를 upcasting, 자동 타입 변환이라고 이해하면 된다.

### 왜 이 변환이 중요할까
이 개념이 있어야 부모 타입 하나로 여러 자식 객체를 공통 처리할 수 있다. 그리고 바로 그 지점이 다형성으로 이어진다.

## 12. 부모 타입으로 보면 보이는 것과 안 보이는 것이 달라진다
`Study11.java`와 `Study12.java`는 매우 중요하다.

```java
Child child = new Child();
Parent parent = child;

parent.method1();
parent.method2();
```

여기서 `parent`는 실제로는 `Child` 객체를 가리키지만, 타입은 `Parent`다. 그래서 다음 규칙이 적용된다.

- 부모에 있는 멤버는 접근 가능
- 자식에만 있는 멤버는 부모 타입으로는 접근 불가

예를 들어:

```java
// parent.method3(); // 불가
```

하지만 `method2()`는 다르다. 부모에 선언된 메서드이면서 자식이 오버라이딩했기 때문에, 실제 실행은 자식 버전이 호출된다.

즉, "참조 변수 타입이 접근 가능 범위를 결정하고, 실제 객체 타입이 실행 메서드를 결정한다"는 매우 중요한 규칙이 여기 숨어 있다.

## 13. 강제 캐스팅: 실제 자식 객체일 때만 다시 내려갈 수 있다
`Study12.java`는 다운캐스팅을 보여 준다.

```java
Parent parent = new Child();
Child child = (Child)parent;

child.field2 = "data2";
child.method3();
```

왜 강제 캐스팅이 필요할까? 부모 타입으로 한 번 올려 담았기 때문에, 다시 자식 고유 기능을 쓰려면 "이 객체는 정말 Child가 맞다"는 걸 개발자가 명시해야 하기 때문이다.

하지만 여기서 중요한 전제가 있다. 실제 객체가 진짜 `Child`여야만 한다. 만약 그냥 `new Parent()`였는데 억지로 `(Child)`로 바꾸면 런타임 오류가 난다.

즉, 다운캐스팅은 가능한 경우에만 조심해서 써야 한다.

## 14. 다형성의 출발점: 같은 부모 타입, 다른 실제 동작
`Study13.java`는 day5의 핵심 예제다.

```java
Car myCar = new Car();

myCar.tire = new Tire();
myCar.run();

myCar.tire = new HankookTire();
myCar.run();

myCar.tire = new KumhoTire();
myCar.run();
```

`Car`는 `Tire` 타입 필드를 가진다.

```java
public Tire tire;

public void run(){
    this.tire.roll();
}
```

그런데 실제로 넣는 객체에 따라 `roll()` 결과가 달라진다.

- `Tire` -> "회전합니다."
- `HankookTire` -> "한국타이어가 회전합니다."
- `KumhoTire` -> "금호타이어가 회전합니다."

이것이 다형성이다. 같은 `Tire` 타입으로 다루지만, 실제 객체가 무엇인지에 따라 실행 결과가 달라진다.

### 왜 다형성이 중요한가
차 입장에서는 "타이어는 굴러야 한다"는 공통 규칙만 알면 된다. 한국타이어인지 금호타이어인지에 맞춰 `if`문을 잔뜩 쓰지 않아도 된다. 객체 스스로 자기 동작을 제공하기 때문이다.

이 감각이 생기면 이후 인터페이스, 스프링 빈 주입, 전략 패턴 같은 개념도 훨씬 자연스럽게 이해된다.

## 15. 오늘 실습을 통해 얻은 핵심 감각
1월 12일 학습에서 꼭 남겨야 할 핵심은 아래와 같다.

- `final`은 바뀌면 안 되는 값과 동작을 보호한다.
- 상수는 `static final`로 두고 클래스 이름으로 접근한다.
- 캡슐화는 데이터를 숨기는 것이 아니라 규칙을 지키게 만드는 것이다.
- 싱글턴은 객체 생성을 하나로 제한해 중앙 관리 구조를 만든다.
- 상속은 기존 클래스를 재사용하고 확장하는 방법이다.
- 오버라이딩은 자식이 부모 동작을 상황에 맞게 바꾸는 방법이다.
- 부모 타입으로 자식 객체를 다루면 다형성이 가능해진다.
- 강제 캐스팅은 실제 객체 타입을 정확히 알고 있을 때만 안전하다.

이날부터 자바의 객체지향은 "클래스를 여러 개 만든다" 수준을 넘어서, "관계를 설계하고 책임을 나눈다"는 쪽으로 깊어진다.

## 오늘의 복습 질문
이 글만 읽고 아래 질문에 답할 수 있으면 day5 내용은 잘 정리된 것이다.

<details>
<summary>1. <code>final</code> 필드와 일반 필드는 어떤 차이가 있을까?</summary>

`final` 필드는 한 번만 할당할 수 있고 이후 변경할 수 없다. 일반 필드는 필요에 따라 값을 바꿀 수 있다.

</details>

<details>
<summary>2. 왜 <code>EARTH_RADIUS</code> 같은 값은 <code>static final</code>로 두는 것이 자연스러울까?</summary>

모든 객체가 동일하게 공유해야 하고, 바뀌면 안 되는 상수이기 때문이다. 그래서 클래스 차원에서 하나만 두는 것이 맞다.

</details>

<details>
<summary>3. <code>private</code> 필드를 직접 공개하지 않고 setter/getter를 쓰는 이유는 무엇일까?</summary>

값 변경과 조회 규칙을 통제하기 위해서다. 캡슐화를 통해 잘못된 값 입력을 막고, 클래스가 자신의 상태를 책임지게 할 수 있다.

</details>

<details>
<summary>4. 싱글턴 패턴은 어떤 상황에서 유용할까?</summary>

프로그램 전체에서 객체를 하나만 두고 공용으로 관리해야 할 때 유용하다. 설정 관리자나 중앙 서비스 객체 같은 경우가 대표적이다.

</details>

<details>
<summary>5. 오버로딩과 오버라이딩은 어떻게 다를까?</summary>

오버로딩은 같은 이름의 메서드를 매개변수 다르게 여러 개 만드는 것이고, 오버라이딩은 부모 메서드를 자식 클래스에서 다시 구현하는 것이다.

</details>

<details>
<summary>6. 부모 타입 변수로 자식 객체를 참조할 때, 접근 가능한 멤버와 실제 실행되는 메서드는 어떻게 결정될까?</summary>

접근 가능한 멤버 범위는 참조 변수 타입을 따르고, 실제 실행되는 오버라이딩 메서드는 실제 객체 타입을 따른다.

</details>

<details>
<summary>7. 다운캐스팅은 왜 조심해서 써야 할까?</summary>

실제 객체 타입이 맞지 않으면 `ClassCastException`이 발생할 수 있기 때문이다. 그래서 보통 `instanceof` 등으로 확인한 뒤 사용해야 한다.

</details>

<details>
<summary>8. <code>Tire</code>, <code>HankookTire</code>, <code>KumhoTire</code> 예제에서 다형성은 어떻게 드러날까?</summary>

같은 `Tire` 타입으로 다뤄도 실제 객체가 무엇인지에 따라 다른 `roll()` 동작이 실행되는 점에서 다형성이 드러난다.

</details>

## 마무리
1월 12일은 객체지향 자바가 본격적으로 재미있어지는 구간이다. 객체를 단순히 만드는 수준을 넘어서, 어떤 값은 고정하고, 어떤 값은 숨기고, 어떤 객체는 하나만 두고, 어떤 클래스는 부모를 물려받아 확장하고, 어떤 경우에는 부모 타입으로 여러 자식을 공통 처리하는 구조를 직접 보게 되기 때문이다.

특히 `Bank`와 `BankTuple` 실습은 이후 파일 저장, 컬렉션, 데이터베이스, UI 연결로 확장될 수 있는 좋은 토대였다. day5는 "상속 문법 소개"가 아니라 "객체 사이의 관계를 설계하는 첫날"이라고 보는 편이 맞다.
