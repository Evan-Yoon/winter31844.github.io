package javaStudy;

public class Study10 {
    public static void main(String[] args) {
        Calculator cal = new Calculator();

        System.err.println(cal.plus(8, 2));

        System.err.println(cal.plus(8, 2, 5));

        System.err.println(cal.plus(8, 2, 5, 3));
    }
}
