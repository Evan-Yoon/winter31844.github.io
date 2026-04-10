package javaStudy;

public class Study09 {
    public static void main(String[] args) {
        Calculator myCal = new Calculator();

        myCal.powerOn();

        int result1 = myCal.plus(5, 6);
        System.out.println("result1 : " + result1);

        int x = 10;
        int y = 4;
        double result2 = myCal.divide(x, y);
        System.out.println("result2 : " + result2);

        myCal.powerOff();
    }
}
