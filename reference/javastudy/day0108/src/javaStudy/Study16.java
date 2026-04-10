package javaStudy;

import java.util.Scanner;

public class Study16 {
    public static void main(String[] args) {
        PinMoney[] pm = new PinMoney[50];
        Scanner scan = new Scanner(System.in, "EUC-KR");
        String op, date, content;
        int amount;
        boolean income;

        while (true) {
            showMenu();
            op = scan.next();
            switch (op) {
                case "1": // *조회

                    for(int i = 0; i < PinMoney.cnt;i++) {
                        pm[i].showData();
                    }
                    System.out.println("총 잔액 : " + PinMoney.totalMoney + "원");
                    break;

                case "2": // *수입
                    System.out.print("날짜 : ");
                    date = scan.next();
                    System.out.print("내용 : ");
                    content = scan.next();
                    System.out.print("금액 : ");
                    amount = scan.nextInt();
                    pm[PinMoney.cnt] = new PinMoney(date, content, amount, income = true);
                    break;

                case "3": // *지출
                    System.out.print("날짜 : ");
                    date = scan.next();
                    System.out.print("내용 : ");
                    content = scan.next();
                    System.out.print("금액 : ");
                    amount = scan.nextInt();
                    pm[PinMoney.cnt] = new PinMoney(date, content, amount, income = false);
                    break;

                case "q": // *종료
                    scan.close();
                    return;

                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }

    }

    static void showMenu() {
        System.out.println("-----------------------------------------------");
        System.out.println("1. 내역 조회 | 2. 수입 | 3. 지출 | q. 종료");
        System.out.println("-----------------------------------------------");
        System.out.print(">> 선택: ");
    }
}
