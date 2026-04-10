package javaStudy;

import java.util.Scanner;

public class Study15 {
    public static void main(String[] args) {
        //int score = 75;

        Scanner scan = new Scanner(System.in); //스캐너 객체 생성 //System.in : 키보드 입력을 받겠다.

        System.out.print("점수: ");
        int score = scan.nextInt(); //nextInt() : 정수형태의 값을 입력받음

        if(score >= 90) {
            System.out.println("A");
        } else if(score >= 80) {
            System.out.println("B");
        } else if(score >= 70) {
            System.out.println("C");
        } else if(score >= 60) {
            System.out.println("D");
        } else {
            System.out.println("F");
        }
    }
}
