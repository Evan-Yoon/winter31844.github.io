package javaStudy;

public class Study16 {
    public static void main(String[] args) {
        int v1 = 15;
        int v2 = 0; // v2 변수의 선언 위치 변경(초기화)
        if(v1>10) {
            v2 = v1 - 10;
        }
        int v3 = v1 + v2 + 5;
        System.out.println("v3 = " + v3);
    }
}
