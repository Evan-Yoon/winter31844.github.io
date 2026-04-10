---
title: "Java 학습 6일차: 추상 클래스, 다형성, 인터페이스 설계"
slug: java-study-day6-polymorphism-and-interfaces
date: 2026-01-13
author: Evan Yoon
category: study
subcategory: self-study
description: "1월 13일 Java 학습 기록. 추상 클래스와 추상 메서드, 다형성 복습, instanceof 패턴 매칭, 인터페이스 구현과 다중 구현, default 메서드와 static 메서드, 인터페이스 상속까지 실습 내용을 자세히 정리했다."
tags:
  - java
  - study
  - abstract-class
  - interface
  - polymorphism
  - oop
  - default-method
readTime: 16
series: Java Study
seriesOrder: 6
featured: false
draft: false
toc: true
---

## 오늘은 "무엇인가"보다 "무엇을 할 수 있는가"로 설계가 이동한 날이었다
`reference/javastudy/day0113/src/javaStudy`에는 `Driver`, `Vehicle`, `Bus`, `Taxi`, `Phone`, `Smartphone`, `Animal`, `Dog`, `Cat`, `RemoteControl`, `Searchable`, `Service`, `InterfaceA`, `InterfaceB`, `InterfaceC` 같은 파일이 들어 있다. day5가 상속과 오버라이딩, 다형성의 출발점을 배우는 날이었다면, day6는 그 위에 "추상화"와 "역할 중심 설계"를 얹는 날이었다.

이날 학습 흐름을 크게 나누면 이렇다.

1. 부모 타입으로 자식 객체를 다루는 다형성을 다시 확인한다.
2. `instanceof`와 패턴 매칭으로 안전한 형변환을 익힌다.
3. 추상 클래스로 공통 동작과 미완성 동작을 함께 표현한다.
4. 인터페이스로 역할을 정의하고 구현 클래스를 분리한다.
5. `default` 메서드와 `static` 메서드로 인터페이스 기능을 넓힌다.
6. 인터페이스 상속과 다중 구현으로 더 유연한 구조를 본다.

즉, day6는 "객체를 상속으로 연결하는 단계"에서 "역할과 계약으로 연결하는 단계"로 넘어가는 날이었다.

## 1. 다형성 복습: 호출부는 단순하게, 실제 동작은 다양하게
`Study01.java`는 아주 전형적인 다형성 예제다.

```java
Driver driver = new Driver();

Bus bus = new Bus();
driver.drive(bus);

Taxi taxi = new Taxi();
driver.drive(taxi);
```

`Driver.java`는 이렇게 되어 있다.

```java
public void drive(Vehicle vehicle){
    vehicle.run();
}
```

그리고 `Bus`, `Taxi`는 모두 `Vehicle`을 상속하면서 `run()`을 각각 다르게 오버라이딩한다.

```java
public class Bus extends Vehicle {
    @Override
    public void run() {
        System.out.println("버스가 달립니다.");
    }
}
```

```java
public class Taxi extends Vehicle {
    @Override
    public void run() {
        System.out.println("택시가 달립니다.");
    }
}
```

여기서 가장 중요한 점은 `Driver`가 버스인지 택시인지 구분하지 않는다는 것이다. 그저 `Vehicle`만 받으면 `run()`을 호출한다. 실제 어떤 객체냐에 따라 실행 결과만 달라진다.

이것이 다형성의 핵심이다. 호출하는 쪽은 단순하고, 동작은 유연해진다.

## 2. 부모 타입으로 받고 필요할 때만 자식으로 확인하기
`Study02.java`는 `Person`과 `Student`를 통해 형변환을 더 현실적으로 보여 준다.

```java
Person p1 = new Person("이재명");
personInfo(p1);

Person p2 = new Student("윤석열", 10);
personInfo(p2);
```

핵심 메서드는 이 부분이다.

```java
public static void personInfo(Person person) {
    System.out.println("이름: " + person.name);
    person.walk();

    if(person instanceof Student student) {
        System.out.println("studentNo: " + student.studentNo);
        student.study();
    }
}
```

여기서는 두 가지를 배운다.

- 매개변수는 넓게 `Person` 타입으로 받아 공통 처리한다.
- 정말 `Student`일 때만 학생 번호와 `study()`를 사용한다.

### `instanceof` 패턴 매칭이 왜 좋은가
이전에는 `instanceof` 검사 후 다시 강제 캐스팅을 따로 써야 했다.

```java
if (person instanceof Student) {
    Student student = (Student)person;
}
```

지금 예제처럼 쓰면 검사와 캐스팅이 한 번에 된다.

```java
if (person instanceof Student student) {
    ...
}
```

즉, 더 짧고 안전하게 형변환할 수 있다.

## 3. 추상 클래스: 공통 부분은 주고, 핵심 동작은 자식에게 맡긴다
`Study03.java`는 `Phone`과 `Smartphone`으로 추상 클래스를 다룬다.

부모 클래스는 이렇게 선언되어 있다.

```java
public abstract class Phone {
    String owner;

    Phone(String owner) {
        this.owner = owner;
    }

    void turnOn() {
        System.out.println("폰 전원을 켭니다.");
    }

    void turnoff() {
        System.out.println("폰 전원을 끕니다.");
    }
}
```

이 클래스는 `abstract`이기 때문에 스스로 객체를 만들 수 없다.

```java
// Phone phone = new Phone("이재명");
```

대신 자식 클래스가 구체화한다.

```java
public class Smartphone extends Phone {
    Smartphone(String owner) {
        super(owner);
    }

    void internetSearch() {
        System.out.println("인터넷 검색을 합니다.");
    }
}
```

`Study03.java`는 이 구조를 사용한다.

```java
Smartphone smartphone = new Smartphone("윤석열");
smartphone.turnOn();
smartphone.internetSearch();
smartphone.turnoff();
```

### 추상 클래스는 왜 필요할까
`Phone`은 완전히 비어 있는 틀이 아니다. 전원 켜기/끄기처럼 공통 기능은 이미 제공하고 있다. 하지만 "전화라는 개념 자체는 추상적"이라 직접 객체로 만들지는 않게 하고 싶다. 이런 경우 추상 클래스가 적절하다.

즉, 추상 클래스는 "공통 코드 재사용"과 "직접 생성 방지"를 동시에 제공한다.

## 4. 추상 메서드: 자식이 반드시 구현해야 하는 약속
`Study04.java`는 `Animal`, `Dog`, `Cat` 구조를 보여 준다.

부모 추상 클래스:

```java
public abstract class Animal {
    public void breath() {
        System.out.println("숨을 쉽니다.");
    }

    public abstract void sound();
}
```

자식 클래스:

```java
public class Dog extends Animal {
    @Override
    public void sound() {
        System.out.println("멍멍");
    }
}
```

```java
public class Cat extends Animal {
    @Override
    public void sound() {
        System.out.println("야옹");
    }
}
```

`Study04.java`는 이렇게 공통 메서드 하나로 처리한다.

```java
public static void animalSound(Animal animal) {
    animal.sound();
}
```

이 구조는 매우 중요하다. 모든 동물이 숨 쉬는 건 같으니 `breath()`는 부모가 제공하면 된다. 하지만 우는 소리는 동물마다 다르니 `sound()`는 자식이 반드시 구현하도록 비워 둔다.

즉, 추상 메서드는 "이 기능은 꼭 필요하지만, 구현은 자식마다 다르다"는 선언이다.

## 5. 오버라이딩은 다형성과 함께 읽어야 한다
`Study05.java`는 `Person`, `Employee`, `Manager`, `Director` 구조를 사용한다.

```java
Person p = new Person();
Employee e = new Employee();
Manager m = new Manager();
Director d = new Director();

p.work();
e.work();
m.work();
d.work();
```

각 클래스는 `work()`를 다르게 구현한다.

- `Person`: "하는 일이 결정되지 않았습니다."
- `Employee`: "제품을 생산합니다."
- `Manager`: "생산 관리를 합니다."
- `Director`: "제품을 기획합니다."

이 예제는 단순히 오버라이딩만 보여 주는 게 아니다. 같은 `work()`라는 메시지를 보내더라도 객체 종류에 따라 다르게 반응하는 것이 다형성이라는 점을 다시 확인하게 해 준다.

또 `Person`이 `sealed`, `Employee`가 `final`, `Manager`가 `non-sealed`로 선언되어 있어서, 상속 허용 범위를 제어하는 최신 문법 감각도 맛볼 수 있다.

## 6. 인터페이스: 역할만 정의하고 구현은 각 클래스에 맡긴다
day6의 핵심은 인터페이스다. `Study06.java`는 `RemoteControl`을 사용한다.

```java
RemoteControl rc;
rc = new Television();
rc.turnOn();

rc = new Audio();
rc.turnOn();
```

인터페이스는 이렇게 선언되어 있다.

```java
public interface RemoteControl {
    int MAX_VOLUME = 10;
    int MIN_VOLUME = 0;

    public void turnOn();
    public void turnOff();
    public void setVolume(int setVolume);
}
```

여기서 중요한 건 인터페이스가 "무엇을 할 수 있어야 하는가"만 정의한다는 점이다.

- 켤 수 있어야 한다.
- 끌 수 있어야 한다.
- 볼륨을 조절할 수 있어야 한다.

하지만 실제로 TV를 켤지, 오디오를 켤지는 구현 클래스가 정한다.

즉, 추상 클래스가 "공통 코드 + 일부 미완성"이라면, 인터페이스는 더 순수하게 "역할 계약"에 가깝다.

## 7. 인터페이스 상수와 구현 클래스
`Study07.java`는 인터페이스 상수를 출력한다.

```java
System.out.println("리모콘 최대 볼륨 : " + RemoteControl.MAX_VOLUME);
System.out.println("리모콘 최저 볼륨 : " + RemoteControl.MIN_VOLUME);
```

인터페이스 안의 필드는 자동으로 `public static final` 성격을 가진다. 즉, 상수로 취급한다.

`Television.java`와 `Audio.java`는 그 상수를 활용해 볼륨 범위를 제한한다.

```java
if(volume > RemoteControl.MAX_VOLUME) {
    this.volume = RemoteControl.MAX_VOLUME;
} else if(volume < RemoteControl.MIN_VOLUME) {
    this.volume = RemoteControl.MIN_VOLUME;
}
```

이 구조는 "공통 규칙은 인터페이스에 두고, 실제 저장 방식은 구현체가 가진다"는 감각을 보여 준다.

## 8. `default` 메서드: 인터페이스도 기본 동작을 가질 수 있다
`RemoteControl`은 추상 메서드만 있는 것이 아니다. `default` 메서드도 있다.

```java
default void setMute(boolean mute){
    if(mute){
        System.out.println("무음 처리합니다.");
        setVolume(MIN_VOLUME);
    } else {
        System.out.println("무음을 해제합니다.");
    }
}
```

`Study08.java`에서는 이를 직접 호출한다.

```java
rc.setVolume(5);
rc.setMute(true);
rc.setMute(false);
```

여기서 핵심은 구현 클래스가 `setMute()`를 따로 만들지 않아도 인터페이스가 기본 동작을 제공할 수 있다는 점이다.

### 왜 `default` 메서드가 필요할까
인터페이스에 메서드를 하나 추가하면, 원래 그 인터페이스를 구현하던 모든 클래스가 다 수정되어야 한다. 그런데 기본 동작을 인터페이스에서 제공하면 기존 구현체를 덜 깨뜨리고 기능을 확장할 수 있다.

즉, `default` 메서드는 인터페이스의 유연성을 높여 준다.

## 9. 인터페이스 메서드도 오버라이드해서 더 구체적으로 바꿀 수 있다
`Audio.java`는 `setMute()`를 직접 재정의한다.

```java
private int memoryVolume;

@Override
public void setMute(boolean mute) {
    if(mute){
        this.memoryVolume = this.volume;
        System.out.println("무음 처리합니다.");
        setVolume(MIN_VOLUME);
    } else {
        System.out.println("무음을 해제합니다.");
        setVolume(this.memoryVolume);
    }
}
```

이 구현은 TV의 기본 무음 해제보다 더 똑똑하다. 이전 볼륨을 기억했다가 무음 해제 시 복구한다.

이 예제가 중요한 이유는 `default` 메서드가 "최종 동작"이 아니라 "기본 제공 동작"이라는 걸 보여 주기 때문이다. 필요하면 구현 클래스가 더 좋은 방식으로 바꿔도 된다.

## 10. 인터페이스의 `static` 메서드
`Study08.java` 마지막에는 이런 코드도 있다.

```java
RemoteControl.changeBattery();
```

`RemoteControl` 안에는 정적 메서드가 정의돼 있다.

```java
static void changeBattery(){
    System.out.println("리모컨의 배터리를 교환합니다.");
}
```

이 메서드는 객체가 아니라 인터페이스 이름으로 호출한다. 즉, 특정 구현체 상태와 무관한 공통 기능일 때 적합하다.

인터페이스는 단순 계약만 있는 줄 알기 쉬운데, day6에서는 `default`와 `static`을 통해 꽤 다양한 기능을 가질 수 있다는 점까지 배운 셈이다.

## 11. 인터페이스 여러 개를 한 클래스가 함께 구현할 수 있다
`Study10.java`는 `SmartTelevision`을 사용한다.

```java
RemoteControl rc = new SmartTelevision();
rc.turnOn();
rc.turnOff();

Searchable searchable = new SmartTelevision();
searchable.searchable("www.naver.com");
```

`Searchable`은 이렇게 생겼다.

```java
public interface Searchable {
    void searchable(String url);
}
```

이 구조가 중요한 이유는 하나의 클래스가 여러 역할을 동시에 가질 수 있음을 보여 주기 때문이다.

- 리모컨으로 제어 가능한 기기
- 검색 가능한 기기

클래스는 단일 상속만 가능하지만, 인터페이스는 여러 개 구현할 수 있다. 이 차이가 설계 유연성을 크게 만든다.

실습 소스의 `SmartTelevision` 구현은 일부 메서드가 미완성 상태지만, 학습 포인트 자체는 분명하다. "한 객체가 여러 계약을 동시에 만족할 수 있다"는 것이다.

## 12. 인터페이스도 상속할 수 있다
`Study11.java`는 `InterfaceA`, `InterfaceB`, `InterfaceC` 구조를 보여 준다.

```java
public interface InterfaceC extends InterfaceA, InterfaceB {
    void methodC();
}
```

즉, 인터페이스도 다른 인터페이스를 상속할 수 있고, 여러 인터페이스를 한 번에 확장할 수 있다.

구현 클래스는 이렇게 된다.

```java
public class InterfaceCImpl implements InterfaceC {
    @Override
    public void methodA() { ... }

    @Override
    public void methodB() { ... }

    @Override
    public void methodC() { ... }
}
```

`Study11.java`는 같은 객체를 어떤 인터페이스 타입으로 보느냐에 따라 호출 가능한 메서드가 달라짐을 보여 준다.

```java
InterfaceA ia = impl;
ia.methodA();

InterfaceB ib = impl;
ib.methodB();

InterfaceC ic = impl;
ic.methodA();
ic.methodB();
ic.methodC();
```

즉, 참조 변수 타입이 "어떤 기능까지 보이게 할지"를 결정한다는 점이 다시 한 번 강조된다.

## 13. 클래스 상속 구조와 인터페이스 설계를 함께 보는 감각
`Study12.java`는 `A`, `B`, `C`, `D`, `E` 계층을 통해 클래스 상속 관계에서의 대입을 다시 보여 준다.

```java
A a;
a = b;
a = c;
a = d;
a = e;
```

이 예제 자체는 단순하지만 day6 맥락에서는 의미가 있다. 상속 계층에서는 "부모 타입으로 자식 객체를 담는다"는 원리가 유지되고, 인터페이스에서는 "인터페이스 타입으로 구현 객체를 담는다"는 원리가 이어진다.

즉, 다형성의 본질은 같다.

- 상속 기반 다형성
- 인터페이스 기반 다형성

둘 다 공통 타입으로 여러 구현을 다룰 수 있게 해 준다.

## 14. 추상 클래스와 인터페이스는 어떻게 다를까
day6에서 가장 헷갈리기 쉬운 부분이 이 비교다.

### 추상 클래스
- 공통 필드와 일반 메서드를 가질 수 있다.
- 일부 메서드는 완성하고 일부는 추상 메서드로 남길 수 있다.
- 상속은 하나만 가능하다.

### 인터페이스
- 역할과 규칙을 정의하는 데 더 적합하다.
- 여러 개를 동시에 구현할 수 있다.
- 상수, 추상 메서드, `default` 메서드, `static` 메서드를 가질 수 있다.

입문 단계에서는 이렇게 이해하면 편하다.

"비슷한 종류를 묶고 공통 코드를 주고 싶으면 추상 클래스, 서로 다른 클래스들이 공통 역할을 하게 만들고 싶으면 인터페이스."

## 15. 오늘 실습을 통해 얻은 핵심 감각
1월 13일 실습에서 꼭 남겨야 할 핵심은 아래와 같다.

- 다형성은 호출부를 단순하게 만들고 확장성을 높인다.
- `instanceof`는 안전한 형변환을 도와준다.
- 추상 클래스는 공통 코드와 미완성 설계를 함께 담을 수 있다.
- 추상 메서드는 자식이 반드시 구현해야 하는 계약이다.
- 인터페이스는 객체의 역할을 정의한다.
- `default` 메서드는 인터페이스에 기본 동작을 제공한다.
- `static` 메서드는 인터페이스 차원의 공통 기능을 제공한다.
- 인터페이스는 여러 개 구현할 수 있어 설계를 더 유연하게 만든다.
- 참조 변수 타입은 접근 가능한 기능의 범위를 결정한다.

이날부터 객체지향은 단순한 상속 구조를 넘어 "느슨하게 연결된 설계"라는 방향으로 확장되기 시작한다.

## 오늘의 복습 질문
이 글만 읽고 아래 질문에 답할 수 있으면 day6 내용은 꽤 잘 정리된 것이다.

1. `Driver.drive(Vehicle vehicle)`가 버스와 택시를 모두 처리할 수 있는 이유는 무엇일까?
2. `instanceof` 패턴 매칭은 기존 캐스팅 방식보다 어떤 점이 편할까?
3. 추상 클래스는 왜 직접 객체를 만들 수 없을까?
4. 추상 메서드와 일반 메서드는 어떻게 다를까?
5. 인터페이스와 추상 클래스는 각각 어떤 상황에서 더 자연스러울까?
6. `default` 메서드는 왜 도입됐을까?
7. 같은 `RemoteControl` 인터페이스를 구현해도 `Television`과 `Audio`의 무음 처리 방식이 달라질 수 있는 이유는 무엇일까?
8. 한 클래스가 `RemoteControl`과 `Searchable`을 함께 구현하는 구조는 어떤 장점이 있을까?
9. `InterfaceA`, `InterfaceB`, `InterfaceC` 예제에서 참조 타입이 바뀌면 보이는 메서드가 왜 달라질까?

## 마무리
1월 13일은 자바 객체지향이 한 단계 더 추상적으로 보이기 시작한 날이었다. 상속만으로 구조를 짜는 데서 멈추지 않고, 공통 동작은 추상 클래스로 정리하고, 역할은 인터페이스로 분리하며, 공통 타입 하나로 여러 구현을 다루는 방법을 본격적으로 익히기 시작했기 때문이다.

이 감각은 이후 제네릭, 컬렉션, 예외 처리, 스레드, GUI, 프레임워크 학습까지 계속 이어진다. day6는 단순히 인터페이스 문법을 익힌 날이 아니라 "구현보다 계약을 먼저 보는 사고방식"을 처음 제대로 연습한 날이었다.
