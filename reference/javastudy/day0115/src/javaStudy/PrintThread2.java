package javaStudy;

public class PrintThread2 extends Thread {
    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("실행 중");
                Thread.sleep(1); // InterruptedException이 발생할 수 있게 하기 위함
            }
        } catch (Exception e) {
        }
        System.out.println("리소스 정리");
        System.out.println("실행 종료");
    }
}
