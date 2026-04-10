package javaStudy;

import java.util.Scanner;

public class Study14 {
    public static void main(String[] args) {
        //int score = 93;
        Scanner scan = new Scanner(System.in); //스캐너 객체 생성 //System.in : 키보드 입력을 받겠다.

        System.out.print("점수: ");
        int score = scan.nextInt(); //nextInt() : 정수형태의 값을 입력받음

        if (score >= 90) {
            System.out.println("A");
        }else {
            System.out.println("B");
        }
    }
}
