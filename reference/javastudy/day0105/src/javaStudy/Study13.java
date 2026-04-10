package javaStudy;

public class Study13 {
    public static void main(String[] args) {
        byte result1 = 10 + 20;
        System.out.println("result1 = " + result1);

        byte v1 = 10;
        byte v2 = 20;
        int result2 = v1 + v2;
        System.out.println("result2 = " + result2); // 10 + 20 = 30

        byte v3 = 10;
        byte v4 = 100;
        long v5 = 1000L;
        long result3 = v3 + v4 + v5;
        System.out.println("result3 = " + result3); // 10 + 100 + 1000L = 1110L
        
        char v6 = 'A';
        char v7 = 1;
        int result4 = v6 + v7;
        System.out.println("result4 = " + result4); // 'A' + 1 = 66
        System.out.println("result4 = " + (char)result4); // 66 -> 'B'

        int v8 = 10;
        int result5 = v8 / 4;
        System.out.println("result5 = " + result5); // 10 / 4 = 2

        int v9 = 10;
        double result6 = v9 / 4.0;
        System.out.println("result6 = " + result6); // 10 / 4.0 = 2.5

        int v10 = 1;
        int v11 = 2;
        double result7 = (double)v10 / v11; // 형변환
        System.out.println("result7 = " + result7); // 1 / 2 = 0.5
    }

}
