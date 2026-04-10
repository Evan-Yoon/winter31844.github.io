package javaStudy;

import java.util.Scanner;

public class Study05 {
    public static void main(String[] args) {
        // "UNI-KR" 대신 일반적으로 System.in만 쓰거나 인코딩이 필요하면 "UTF-8" 등을 씁니다.
        Scanner scan = new Scanner(System.in);
        String op; // 변수 선언 필요

        while (true) {
            showMenu();
            op = scan.next();

            switch (op) {
                case "1": // (중요) 세미콜론(;)이 아니라 콜론(:)이어야 합니다.
                    BankTuple t = new BankTuple();

                    System.out.print("날짜 : ");
                    t.setDate(scan.next());

                    System.out.print("내역 : ");
                    t.setContent(scan.next());

                    // 입금/출금 여부를 묻는 로직이 없어서 임시로 입금(true) 처리
                    // (실제로는 사용자에게 묻는 코드가 더 필요할 수 있습니다)
                    t.setIncome(true);

                    System.out.print("금액 : ");
                    t.setAmount(scan.nextInt());

                    // (중요) 여기서 Bank 클래스에 데이터를 넘겨줘야 저장이 됩니다!
                    // 이 줄이 없으면 입력만 받고 데이터가 날아갑니다.
                    Bank.getBank().setBankTuple(t);
                    System.out.println(">>> 저장되었습니다.");
                    break;

                case "2": // 출금 구현 (간단 예시)
                    BankTuple t2 = new BankTuple();
                    System.out.print("날짜 : ");
                    t2.setDate(scan.next());
                    System.out.print("내역 : ");
                    t2.setContent(scan.next());

                    t2.setIncome(false); // 출금 설정

                    System.out.print("금액 : ");
                    t2.setAmount(scan.nextInt());

                    Bank.getBank().setBankTuple(t2);
                    System.out.println(">>> 출금 처리되었습니다.");
                    break;

                case "3": // 조회
                    // Bank 클래스의 showBankBook을 호출해야 출력이 됩니다.
                    Bank.getBank().showBankBook();
                    break;

                case "q": // 종료
                    System.out.println("프로그램을 종료합니다.");
                    scan.close();
                    return;

                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    static void showMenu() {
        System.out.println("\n--------------------------");
        System.out.println("1.입금\t2.출금\t3.조회\tq.종료");
        System.out.println("--------------------------");
        System.out.print("선택>> ");
    }
}