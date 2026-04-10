package javaStudy;

public class Study06 {
    public static void main(String[] args) {
        AutoSaveThread autoSaveThread = new AutoSaveThread();
        autoSaveThread.setDaemon(true); // true로 설정하면 데몬 스레드가 된다.
        autoSaveThread.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
        System.out.println("메인 스레드 종료");
    }
}
