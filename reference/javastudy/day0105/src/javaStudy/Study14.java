package javaStudy;

public class Study14 {
    public static void main(String[] args) {
        int result1 = 10 + 2 + 8;
        System.out.println("result1 = " + result1); // 20

        String result2 = 10 + 2 + "8"; // 문자열 결합
        System.out.println("result2 = " + result2); // 128
        // 10 + 2 = 12 -> "12" + "8" = "128"

        String result3 = 10 + "2" + 8; // 문자열 결합
        System.out.println("result3 = " + result3); // 1028
        // 10 -> "10" + "2" = "102" -> "102" + "8" = "1028"

        String result4 = "10" + 2 + 8; // 문자열 결합
        System.out.println("result4 = " + result4); // 1028
        // "10" + "2" = "102" -> "102" + "8" = "1028"

        String result5 = "10" + (2 + 8); // 문자열 결합 / 산술 연산
        System.out.println("result5 = " + result5); // 1010
        // 2 + 8 = 10 -> "10" + "10" = "1010"

        
    }
}