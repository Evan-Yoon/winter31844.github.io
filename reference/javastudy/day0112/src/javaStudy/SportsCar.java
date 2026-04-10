package javaStudy;

public class SportsCar extends Car {
    @Override
    public void speedUp() {
        setSpeed(getSpeed() + 10); //parent의 private 멤버변수 speed에 직접 접근 불가
    }

    /*
    @Override
    public void stop() { // final 메서드는 오버라이딩 불가
        System.out.println("스포츠카 멈춤");
        setSpeed(0);
    } */
}
