package javaStudy;

public class Study03 {
    public static void main(String[] args) {
    // Phone phone = new Phone("이재명"); //추상 클래스는 객체를 스스로 생성 불가

    Smartphone smartphone = new Smartphone("윤석열");

    smartphone.turnOn();
    smartphone.internetSearch();
    smartphone.turnoff();
    }
}
