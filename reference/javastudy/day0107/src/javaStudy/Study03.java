package javaStudy;
import java.util.Scanner;
public class Study03 {
    public static void main(String[] args) {
        System.out.println("메시지를 입력하세요.");
        System.out.println("프로그램을 종료하려면 q를 입력하세요.");

        Scanner scan = new Scanner(System.in);
        String inputStr;

        do {
            System.out.println(">");
            inputStr = scan.nextLine();
        } while (!inputStr.equals("q")); // q가 아닌 동안 반복

        System.out.println("프로그램 종료");
        scan.close();
    }
}
