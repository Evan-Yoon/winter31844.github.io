package javaStudy;

public class Study17 {
    public static void main(String[] args) {
        // 아래 줄의 공백을 깨끗하게 정리했습니다.
        int num = (int)(Math.random() * 6) + 1; 

        switch (num) {
            case 1:
                System.out.println("1번 나옴");
                break;
            case 2:
                System.out.println("2번 나옴");
                break;
            case 3:
                System.out.println("3번 나옴");
                break;
            case 4:
                System.out.println("4번 나옴");
                break;
            case 5:
                System.out.println("5번 나옴");
                break;
            default:
                System.out.println("6번 나옴");
        }
    }
}