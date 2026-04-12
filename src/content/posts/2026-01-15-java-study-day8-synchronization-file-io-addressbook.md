---
title: "Java 학습 8일차: 스레드 동기화, 인터럽트, 스레드풀, 파일 입출력"
slug: java-study-day8-synchronization-file-io-addressbook
date: 2026-01-15
author: Evan Yoon
category: study
subcategory: bootcamp
description: "1월 15일 Java 학습 기록. synchronized 메서드와 동기화 블록, wait/notify, 스레드 종료와 interrupt, 데몬 스레드, ExecutorService, 바이트 스트림과 문자 스트림까지 실습 내용을 자세히 정리했다."
tags:
  - java
  - study
  - synchronization
  - concurrency
  - file-io
  - thread
  - executorservice
readTime: 16
series: Java Study
seriesOrder: 8
featured: false
draft: false
toc: true
---

## 오늘은 "동시에 실행되는 코드"와 "데이터를 파일로 남기는 코드"를 함께 다룬 날이었다 <!-- short: 오늘 배운 핵심 -->
`reference/javastudy/day0115/src/javaStudy`에는 `Calculator`, `User1Thread`, `User2Thread`, `WorkObject`, `ThreadA`, `ThreadB`, `PrintThread`, `AutoSaveThread`와 함께 `Study09.java`부터 `Study16.java`까지의 파일 입출력 예제가 들어 있다. 전날 day7이 예외 처리와 제네릭으로 "안전한 코드"를 만드는 감각을 익히는 날이었다면, day8은 그 코드가 동시에 실행될 때 생기는 문제와, 실행 결과를 파일에 저장하는 방법을 배우는 날이었다.

이날 학습 흐름을 크게 나누면 이렇다.

1. 여러 스레드가 하나의 객체를 공유할 때 생기는 문제를 본다.
2. `synchronized`로 임계 구역을 보호한다.
3. `wait()`와 `notify()`로 스레드 실행 순서를 조절한다.
4. 스레드를 안전하게 종료하는 방법을 익힌다.
5. 데몬 스레드와 스레드풀로 스레드 관리 방식을 넓힌다.
6. 바이트 스트림과 문자 스트림으로 파일을 쓰고 읽는다.

즉, day8은 "코드가 여러 흐름으로 동시에 움직일 때"와 "그 결과를 외부 저장소에 남길 때"를 처음 본격적으로 다루는 날이었다.

## 1. 공유 객체를 여러 스레드가 동시에 쓰면 왜 위험할까
`Study01.java`는 하나의 `Calculator` 객체를 두 스레드가 함께 사용하는 예제다.

```java
Calculator calculator = new Calculator();

User1Thread user1Thread = new User1Thread();
user1Thread.setCalculator(calculator);
user1Thread.start();

User2Thread user2Thread = new User2Thread();
user2Thread.setCalculator(calculator);
user2Thread.start();
```

`Calculator.java`에는 공유 상태가 있다.

```java
private int memory;
```

문제는 두 스레드가 같은 `memory`를 거의 동시에 바꾸려고 할 수 있다는 점이다. 이런 상황을 경쟁 상태, race condition이라고 이해하면 된다.

즉, 멀티스레드에서 중요한 건 "코드가 맞느냐"만이 아니라 "동시에 실행돼도 안전하냐"다.

## 2. `synchronized` 메서드: 메서드 전체를 한 번에 한 스레드만 실행하게 하기 <!-- short: 2. `synchronized` 메서드 -->
`Calculator.java`의 첫 번째 메서드는 `synchronized`가 붙어 있다.

```java
public synchronized void setMemory1(int memory) {
    this.memory = memory;

    try {
        Thread.sleep(2000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    System.out.println(Thread.currentThread().getName() + ": " + this.memory);
}
```

이 메서드는 객체 하나에 대해 한 순간에는 한 스레드만 들어올 수 있다. 즉, `User1Thread`가 실행 중이면 `User2Thread`는 잠시 기다려야 한다.

### 왜 이런 보호가 필요할까
`this.memory = memory;`를 막 바꾼 직후 잠시 대기하고, 그 다음 출력하는 구조이기 때문에 중간에 다른 스레드가 끼어들면 출력값이 꼬일 수 있다. 동기화는 이런 "중간 상태 노출"을 막아 준다.

즉, `synchronized`는 공유 자원을 다루는 코드 구간을 원자적으로 보이게 만드는 도구다.

## 3. 동기화 블록: 메서드 전체가 아니라 필요한 부분만 잠그기 <!-- short: 3. 동기화 블록 -->
`Calculator.java`의 두 번째 메서드는 조금 다르게 작성돼 있다.

```java
public void setMemory2(int memory) {
    synchronized(this) {
        this.memory = memory;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": " + this.memory);
    }
}
```

이건 메서드 전체가 아니라 `synchronized(this)` 블록 안만 잠근다.

### 메서드 동기화와 블록 동기화의 차이
- `synchronized` 메서드: 메서드 시작부터 끝까지 잠금
- `synchronized` 블록: 필요한 구간만 선택적으로 잠금

실제 프로그램에서는 불필요하게 너무 넓게 잠그면 성능이 떨어질 수 있다. 그래서 정말 공유 자원을 다루는 핵심 구간만 잠그는 방식이 자주 쓰인다.

## 4. `wait()`와 `notify()`: 스레드가 번갈아 일하게 만들기 <!-- short: 4. `wait()`와 `notify()` -->
`Study02.java`와 `WorkObject.java`는 day8의 핵심 예제다.

```java
WorkObject workObject = new WorkObject();

ThreadA threadA = new ThreadA(workObject);
ThreadB threadB = new ThreadB(workObject);

threadA.start();
threadB.start();
```

`WorkObject.java`는 이렇게 되어 있다.

```java
public synchronized void methodA() {
    Thread thread = Thread.currentThread();
    System.out.println(thread.getName() + " : methodA() 작업 실행");
    notify();

    try {
        wait();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
```

`methodB()`도 비슷하다.

여기서 중요한 흐름은 이렇다.

1. 현재 스레드가 작업을 한다.
2. `notify()`로 다른 스레드를 깨운다.
3. 자신은 `wait()`로 대기 상태가 된다.

이렇게 하면 `ThreadA`와 `ThreadB`가 서로 번갈아 실행될 수 있다.

### 왜 `wait()`와 `notify()`가 필요할까
동기화만으로는 "동시에 못 들어오게"만 할 수 있다. 하지만 어떤 경우에는 두 스레드가 정해진 순서대로 번갈아 일해야 한다. 생산자-소비자 문제처럼 협업이 필요한 상황이 그렇다. 그런 제어에 `wait()`와 `notify()`가 쓰인다.

## 5. 스레드 종료 1: 플래그 변수로 스스로 멈추게 하기 <!-- short: 5. 스레드 종료 1 -->
`Study03.java`는 `PrintThread`를 사용한다.

```java
PrintThread printThread = new PrintThread();
printThread.start();

Thread.sleep(3000);
printThread.setStop(true);
```

`PrintThread.java`는 이렇게 반복한다.

```java
while (!stop) {
    System.out.println("실행 중");
}
System.out.println("리소스 정리");
System.out.println("실행 종료");
```

이 방식은 가장 직관적이다. 스레드 스스로 `stop` 값을 확인하다가 조건이 바뀌면 자연스럽게 빠져나온다.

### 장점과 한계
- 장점: 흐름이 단순하고 이해하기 쉽다.
- 한계: 스레드가 대기나 블로킹 상태라면 플래그를 바로 확인하지 못할 수 있다.

그래서 day8에서는 인터럽트 방식도 이어서 다룬다.

## 6. 스레드 종료 2: `interrupt()`로 깨우기 <!-- short: 6. 스레드 종료 2 -->
`Study04.java`는 `PrintThread2`를 시작한 뒤 `interrupt()`를 호출한다.

```java
Thread thread = new PrintThread2();
thread.start();
Thread.sleep(1000);
thread.interrupt();
```

`PrintThread2.java`는 `sleep()` 중 인터럽트를 받으면 예외가 발생하는 구조다.

```java
try {
    while (true) {
        System.out.println("실행 중");
        Thread.sleep(1);
    }
} catch (Exception e) {
}
System.out.println("리소스 정리");
System.out.println("실행 종료");
```

여기서 핵심은 인터럽트가 단순히 "강제 종료"가 아니라, 대기 중인 스레드에게 "이제 멈출 준비를 해라"라고 신호를 보내는 방식이라는 점이다.

## 7. 스레드 종료 3: 인터럽트 상태 직접 확인하기
`Study05.java`는 `PrintThread3`를 사용한다.

```java
while (true) {
    System.out.println("실행 중");
    if (Thread.interrupted()) {
        break;
    }
}
System.out.println("리소스 정리");
System.out.println("실행 종료");
```

이 방식은 `sleep()` 예외에 의존하지 않고, 루프 안에서 직접 인터럽트 상태를 검사한다. 즉, 인터럽트는 "예외를 던지게 하는 기술"만이 아니라 "종료 요청 상태를 알려 주는 플래그"로도 볼 수 있다.

이 세 가지 종료 방식 비교는 실제로 매우 중요하다.

- 플래그 변수 방식
- `sleep()`/`wait()` 중 인터럽트 받기
- 직접 인터럽트 상태 확인하기

## 8. 데몬 스레드: 보조 역할 스레드는 메인 작업이 끝나면 같이 끝나게 하기 <!-- short: 8. 데몬 스레드 -->
`Study06.java`는 `AutoSaveThread`를 데몬 스레드로 실행한다.

```java
AutoSaveThread autoSaveThread = new AutoSaveThread();
autoSaveThread.setDaemon(true);
autoSaveThread.start();
```

`AutoSaveThread.java`는 1초마다 저장을 시도한다.

```java
while (true) {
    Thread.sleep(1000);
    save();
}
```

그리고 메인 스레드가 종료되면 프로그램도 끝난다.

### 데몬 스레드는 언제 쓰나
자동 저장, 로그 감시, 임시 정리 작업처럼 주 작업을 돕는 보조 성격의 스레드에 적합하다. 반대로 반드시 끝까지 완료해야 하는 핵심 작업은 사용자 스레드로 두는 것이 맞다.

즉, 데몬 스레드는 "주인공이 아니라 배경 지원 역할"이라고 이해하면 쉽다.

## 9. `ExecutorService`: 스레드를 직접 만들기보다 풀로 관리하기 <!-- short: 9. `ExecutorService` -->
`Study07.java`와 `Study08.java`는 `ExecutorService`를 다룬다.

기본 생성:

```java
ExecutorService executorService = Executors.newFixedThreadPool(5);
```

`Study07.java`는 생성 후 곧바로 종료하는 아주 기본 구조를 보여 준다.

```java
executorService.shutdownNow();
```

`Study08.java`는 더 실제적인 예제다. 메일 1000건을 5개 스레드 풀에 나눠 처리한다.

```java
for(int i=0;i<1000;i++) {
    final int idx = i;
    executorService.execute(new Runnable() {
        @Override
        public void run() {
            Thread thread = Thread.currentThread();
            String from = mails[idx][0];
            String to = mails[idx][1];
            String contents = mails[idx][2];
            System.out.println("[" + thread.getName() + "]" + from + "==>" + to + " : " + contents);
        }
    });
}
executorService.shutdown();
```

### 왜 스레드풀이 필요할까
작업마다 스레드를 새로 만들면 비용이 크고 관리도 어렵다. 스레드풀은 미리 준비된 스레드를 재사용해 다수 작업을 효율적으로 처리한다.

즉, `ExecutorService`는 "스레드 자체"보다 "작업 실행 관리"에 더 집중한 도구다.

## 10. 파일 입출력 1: 바이트 쓰기
`Study09.java`, `Study10.java`, `Study11.java`는 `FileOutputStream`을 사용해 바이트를 파일에 쓴다.

가장 기본 예제:

```java
OutputStream os = new FileOutputStream("Test1.db");

byte a = 10;
byte b = 20;
byte c = 30;

os.write(a);
os.write(b);
os.write(c);
```

배열로 쓰기도 한다.

```java
byte[] array = {10, 20, 30};
os.write(array);
```

배열 일부만 쓸 수도 있다.

```java
byte[] array = {10, 20, 30, 40, 50};
os.write(array, 1, 3);
```

여기서 익혀야 할 핵심은 파일 저장의 가장 밑바닥 단위가 결국 바이트라는 점이다.

## 11. 파일 입출력 2: 바이트 읽기
`Study12.java`와 `Study13.java`는 `FileInputStream`으로 읽는다.

한 바이트씩 읽기:

```java
int data = is.read();
if(data == -1) break;
System.out.println(data);
```

배열 단위로 읽기:

```java
byte[] data = new byte[100];
int num = is.read(data);
if(num == -1) break;
```

### 왜 읽기 결과가 `int`일까
`read()`는 실제 데이터 값뿐 아니라 파일 끝을 뜻하는 `-1`도 반환해야 한다. 그래서 `byte`가 아니라 `int`를 사용한다. 이런 작은 디테일을 이해하면 IO 코드가 덜 낯설어진다.

## 12. 파일 복사: 텍스트가 아닌 바이너리도 결국 스트림이다 <!-- short: 12. 파일 복사 -->
`Study14.java`는 이미지 파일을 복사한다.

```java
String originalFileName = "src\\Image\\Cat.jpg";
String targetFileName = "src\\Image\\Cat2.jpg";

InputStream is = new FileInputStream(originalFileName);
OutputStream os = new FileOutputStream(targetFileName);

byte[] data = new byte[1024];
while (true) {
    int num = is.read(data);
    if(num == -1) break;
    os.write(data, 0, num);
}
```

이 예제가 중요한 이유는 텍스트가 아닌 이미지도 같은 스트림 개념으로 처리된다는 점을 보여 주기 때문이다. 즉, 파일 종류가 달라도 결국 "바이트 흐름을 읽고 쓴다"는 본질은 같다.

## 13. 문자 스트림: 글자를 직접 다루기
`Study15.java`와 `Study16.java`는 문자 스트림을 다룬다.

쓰기:

```java
Writer writer = new FileWriter("Try1.txt");
writer.write('A');
writer.write('B');
writer.write(new char[] {'C', 'D', 'E'});
writer.write("FGH");
```

읽기:

```java
Reader reader = new FileReader("try1.txt");
int data = reader.read();
if(data == -1) break;
System.out.print((char)data);
```

### 바이트 스트림과 문자 스트림의 차이
- 바이트 스트림: 이미지, 실행 파일, 일반 바이너리 포함 모든 데이터를 바이트 단위로 처리
- 문자 스트림: 텍스트를 문자 단위로 처리

입문 단계에서는 "텍스트면 `Reader/Writer`, 그 외 일반 파일이면 `InputStream/OutputStream`" 정도로 구분하면 이해하기 쉽다.

## 14. day8의 실제 중심은 주소록 CRUD보다 스레드와 IO였다
현재 포스트 파일명에는 `addressbook`이 들어 있지만, 실제 `day0115` 소스 기준으로 이날 중심은 주소록 CRUD가 아니라 스레드 동기화와 파일 스트림이다. 따라서 이 날을 복습할 때는 "동시성 제어와 파일 입출력 입문"으로 이해하는 편이 정확하다.

이런 정리는 중요하다. 파일명만 보고 실제 학습 내용을 잘못 기억하면 이후 day9 이후 흐름까지 혼동될 수 있기 때문이다.

## 15. 오늘 실습을 통해 얻은 핵심 감각
1월 15일 실습에서 꼭 남겨야 할 핵심은 아래와 같다.

- 여러 스레드가 하나의 객체를 공유하면 동기화가 필요할 수 있다.
- `synchronized`는 한 번에 한 스레드만 임계 구역에 들어오게 만든다.
- `wait()`와 `notify()`는 협업하는 스레드의 순서를 제어한다.
- 스레드는 플래그 변수나 인터럽트로 안전하게 종료해야 한다.
- 데몬 스레드는 보조 작업용 스레드다.
- `ExecutorService`는 많은 작업을 스레드풀로 효율적으로 처리하게 해 준다.
- 파일은 결국 바이트 또는 문자 흐름으로 읽고 쓴다.
- IO에서는 `flush()`, `close()`, `-1` 같은 작은 규칙이 매우 중요하다.

이 감각은 이후 네트워크, DB, GUI 백그라운드 작업, 서버 프로그래밍으로 갈수록 더 중요해진다.

## 오늘의 복습 질문
이 글만 읽고 아래 질문에 답할 수 있으면 day8 내용은 잘 정리된 것이다.

<details>
<summary>1. 공유 객체를 여러 스레드가 함께 사용할 때 어떤 문제가 생길 수 있을까?</summary>

실행 순서가 섞이면서 값이 꼬이거나 예상과 다른 결과가 나오는 경쟁 상태가 생길 수 있다.

</details>

<details>
<summary>2. <code>synchronized</code> 메서드와 <code>synchronized</code> 블록은 어떻게 다를까?</summary>

메서드 전체를 잠글 수도 있고, 블록으로 필요한 부분만 잠글 수도 있다. 블록 방식이 더 세밀하게 임계 구역을 제어할 수 있다.

</details>

<details>
<summary>3. <code>wait()</code>와 <code>notify()</code>는 각각 무엇을 할까?</summary>

`wait()`는 현재 스레드를 대기 상태로 만들고, `notify()`는 대기 중인 스레드 하나를 다시 깨워 실행 기회를 준다.

</details>

<details>
<summary>4. 스레드를 종료하는 방법으로 플래그 방식과 인터럽트 방식은 어떻게 다를까?</summary>

플래그 방식은 공유 변수 값을 보고 스스로 종료하게 만들고, 인터럽트 방식은 실행 중인 스레드에 중단 신호를 보내 종료 흐름을 유도한다.

</details>

<details>
<summary>5. 데몬 스레드는 어떤 작업에 적합할까?</summary>

주 작업을 돕는 보조 작업에 적합하다. 예를 들어 자동 저장, 상태 감시 같은 백그라운드 처리에 많이 쓴다.

</details>

<details>
<summary>6. <code>ExecutorService</code>를 쓰면 직접 스레드를 계속 생성하는 것보다 어떤 장점이 있을까?</summary>

스레드 재사용과 작업 관리가 쉬워지고, 많은 작업을 더 효율적으로 처리할 수 있다. 스레드 생성 비용도 줄일 수 있다.

</details>

<details>
<summary>7. 바이트 스트림과 문자 스트림은 언제 구분해서 써야 할까?</summary>

텍스트를 다루면 문자 스트림을, 이미지나 일반 바이너리처럼 바이트 단위 데이터는 바이트 스트림을 쓰는 편이 맞다.

</details>

<details>
<summary>8. <code>read()</code>가 <code>-1</code>을 반환한다는 것은 무엇을 의미할까?</summary>

더 이상 읽을 데이터가 없는 파일 끝(EOF)에 도달했다는 뜻이다.

</details>

## 마무리
1월 15일은 자바가 단순히 순차적으로 실행되는 언어가 아니라는 점을 강하게 체감한 날이었다. 여러 스레드가 같은 데이터를 건드릴 때 어떤 문제가 생기는지, 그걸 어떻게 동기화할지, 또 실행 중 만들어진 데이터를 파일로 어떻게 남길지를 함께 배웠기 때문이다.

즉, day8은 "스레드 문법"이나 "IO 문법"을 따로 익힌 날이 아니라, 프로그램이 더 현실적인 환경에서 움직이기 시작하는 첫날이었다. 이후의 네트워크, GUI, 파일 저장, 백그라운드 처리 학습은 대부분 이날 익힌 감각 위에서 이어진다.
