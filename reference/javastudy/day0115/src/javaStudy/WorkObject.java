package javaStudy;

public class WorkObject {
    public synchronized void methodA() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + " : methodA() 작업 실행");
        notify(); //다른 스레드를 실행 대기로 만든다.

        try {
            wait(); //자신을 일시정지 상태로 만든다.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public synchronized void methodB() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + " : methodB() 작업 실행");
        notify(); //다른 스레드를 실행 대기로 만든다.

        try {
            wait(); //자신을 일시정지 상태로 만든다.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
