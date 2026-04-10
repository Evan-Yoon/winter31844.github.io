package javaStudy;

public class Calculator {

    private int memory;

    public int getMemory(){
        return this.memory;
    }

    // ^동기화 메소드
    public synchronized void setMemory1(int memory) {
        this.memory = memory;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": " + this.memory);
    }

    public void setMemory2(int memory) {
         synchronized(this) { // ^동기화 블록
            this.memory = memory;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        System.out.println(Thread.currentThread().getName() + ": " + this.memory);
        }
    }
}
