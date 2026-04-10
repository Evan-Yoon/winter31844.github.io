package javaStudy;

public class Study16 {
    public static void main(String[] args) {
        int num = (int)(Math.random()*6) + 1; //1~6사이의 실수 난수 발생

        if(num == 1) {
            System.out.println("1번 나옴");
        } else if(num == 2) {
            System.out.println("2번 나옴");
        } else if(num == 3) {
            System.out.println("3번 나옴");
        } else if(num == 4) {
            System.out.println("4번 나옴");
        } else if(num == 5) {
            System.out.println("5번 나옴");
        } else {
            System.out.println("6번 나옴");
        }
    }
}
