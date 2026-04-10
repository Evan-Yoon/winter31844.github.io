package javaStudy;

public class Study11 {
    public static void main(String[] args) {
        System.out.println("45 & 25 = " + 45 & 25)); // AND 연산
        System.out.println("45 | 25 = " + (45 | 25)); // OR 연산
        System.out.println("45 ^ 25 = " + (45 ^ 25)); // XOR 연산
        System.out.println("~45 = " + (~45));       // NOT 연산
        // 45의 2진수 : 0010 1101
        // 25의 2진수 : 0001 1001
        // 따라서 AND : 0000 1001 = 9
        // 따라서 OR  : 0011 1101 = 61
        // 따라서 XOR : 0011 0100 = 52
        // 따라서 NOT : 1101 0010 = -46 (음수는 2의 보수로 표현)
    }
}
