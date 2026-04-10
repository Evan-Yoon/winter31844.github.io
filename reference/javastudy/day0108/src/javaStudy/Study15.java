package javaStudy;

import java.util.Scanner;

public class Study15 {
    // 1. main 메소드: 프로그램의 시작점
    public static void main(String[] args) {
        AddressBook[] abs = new AddressBook[20];
        Scanner scan = new Scanner(System.in, "EUC-KR");
        String input;

        while (true) {
            printMenu(); // 메소드 호출 (선언이 아님)

            input = scan.nextLine();
            switch (input) {
                case "1":
                    System.out.println("------------------------");
                    System.out.println("이름\t폰번호\t\t나이");
                    System.out.println("------------------------");
                    for (int i = 0; i < AddressBook.cnt; i++) {
                        abs[i].showData();
                    }
                    break;
                case "2":
                    abs[AddressBook.cnt] = new AddressBook();
                    System.out.print("이름 : ");
                    abs[AddressBook.cnt].name = scan.next();
                    System.out.print("폰번호 : ");
                    abs[AddressBook.cnt].phone = scan.next();
                    System.out.print("나이 : ");
                    abs[AddressBook.cnt].age = Integer.parseInt(scan.next());
                    AddressBook.cnt++;
                    scan.nextLine(); // next() 사용 후 남은 엔터 제거(버퍼 비우기)
                    break;
                case "q":
                    System.out.println("프로그램을 종료합니다.");
                    scan.close();
                    return; // main 메소드 종료
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            } // switch 끝
        } // while 끝
    } // main 메소드 끝

    // 2. printMenu 메소드: main 바깥, 클래스 안쪽에 위치
    static void printMenu() {
        System.out.println("----------------");
        System.out.println("1. 조회 2. 입력");
        System.out.println("----------------");
        System.out.print(">> ");
    }
} // 클래스 끝