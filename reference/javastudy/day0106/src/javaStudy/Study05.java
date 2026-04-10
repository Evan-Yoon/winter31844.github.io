package javaStudy;

public class Study05 {
    public static void main(String[] args) {
        byte V1 = 10;
        byte V2 = 4;
        int V3 = 5;
        long V4 = 10L;

        int result1 = V1 + V2; // byte + byte = int
        System.out.println("result1 : " + result1);

        long result2 = V1 + V2 - V4; // byte + byte - long = long
        System.out.println("result2 : " + result2);

        long result = V1 / V2; // byte / byte = int -> int가 long으로 자동 형변환
        System.out.println("result : " + result);

        double result4 = (double)V1 / V2; // (double)을 붙여서 강제 형변환
        System.out.println("result4 : " + result4);

        int result5 = V1 % V2; // 나머지 연산
        System.out.println("result5 : " + result5);
    }
}
