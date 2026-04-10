package javaStudy;

public class Study03 {
    public static void main(String[] args) {
        int x = -100;
        x = -x;
        System.out.println("x : " + x);

        byte b = 100;
        int y = -b; // byte 타입이 int 타입으로 변환됨
        System.out.println("y : " + y); // -100
    }
}
