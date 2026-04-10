package javaStudy;

public class Computer extends Calculator {

    @Override //생략하면 컴파일러가 자동으로 붙여줌
    public double areaCircle(double r) {
        System.out.println("Computer 객체의 areaCircle() 실행");
        return Math.PI * r * r;
    }
}
