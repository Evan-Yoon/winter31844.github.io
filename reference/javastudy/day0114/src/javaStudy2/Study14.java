package javaStudy2;

import java.awt.Toolkit;

public class Study14 {
    public static void main(String[] args) {

        // 1. 작업 스레드 생성 (비프음 소리 담당)
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                for(int i=0; i<5; i++) {
                    toolkit.beep(); // 소리 재생
                    try {
                        Thread.sleep(1000); // 1초 대기
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }); // 스레드 정의 끝

        // 2. ⭐ 중요: 스레드 시작! (이 코드가 없으면 위의 run()은 실행되지 않음)
        thread.start();

        // 3. 메인 스레드 작업 (글자 출력 담당)
        // (중간에 있던 중복된 소리 재생 코드는 삭제했습니다. 그래야 소리와 글자가 동시에 나옵니다.)
        for(int i=0; i<5; i++) {
            System.out.println("띵");
            try {
                Thread.sleep(1000); // 1초 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}