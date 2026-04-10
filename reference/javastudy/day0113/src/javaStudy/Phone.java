package javaStudy;

public abstract class Phone { //abstract를 넣으면 추상 클래스가 된다.

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
