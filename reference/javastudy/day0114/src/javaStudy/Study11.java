package javaStudy;

public class Study11 {
    public static void main(String[] args) {
        Integer obj = 100;
        System.out.println("value: " + obj.intValue());

        int value = obj; // Auto Boxing
        System.out.println("value: " + value);

        int result = obj + 100; // Auto Unboxing
        System.out.println("result: " + result);
    }
}
