package addressBook;

import java.io.IOException;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) throws IOException {
        FileManager fm = new FileManager("src\\addressBook\\Data.txt");
        Scanner scanner = new Scanner(System.in, "EUC-KR");

        while (true) {
            showMenu();
            String inputStr = scanner.next();

            switch (inputStr) {
                case "q", "ㅂ":
                    System.out.println("프로그램을 종료합니다.");
                    return;

                case "1": // ^조회
                    System.out.println("-----------------------------------------");
                    System.out.println("순번\t이름\t전화번호\t\t성별");
                    System.out.println("-----------------------------------------");
                    System.out.println(fm.getAllData());
                    break;

                case "2": // ^입력
                    Model m = new Model();
                    System.out.print("이름 :");
                    m.setName(scanner.next());
                    System.out.print("전화번호 :");
                    m.setPhoneNum(scanner.next());
                    System.out.print("성별:");
                    String t = scanner.next();
                    if(t.equals("남자") | t.equals("남"))
                        m.setGender(true);
                    else if(t.equals("여자") | t.equals("여"))
                        m.setGender(false);
                    else {
                        System.out.println("잘못입력하셨습니다.");
                        break;
                    }
                    fm.dataCreate(m.toString() + "\n");
                    System.out.println(m.toString());
                    break;

                case "3": // ^수정
                    System.out.println("-----------------------------------------");
                    System.out.println("순번\t이름\t전화번호\t\t성별");
                    System.out.println("-----------------------------------------");
                    System.out.println(fm.getAllData());
                    System.out.print("수정할 순번 :");
                    int updateNum = scanner.nextInt();
                    System.out.print("새로운 전화번호 :");
                    String updatePhoneNum = scanner.next();
                    fm.updateData(updateNum, updatePhoneNum);
                    break;

                case "4": // ^삭제
                    System.out.println("-----------------------------------------");
                    System.out.println("순번\t이름\t전화번호\t\t성별");
                    System.out.println("-----------------------------------------");
                    System.out.println(fm.getAllData());
                    System.out.print("삭제할 순번 :");
                    int delNum = scanner.nextInt();
                    fm.delData(delNum);
                    break;

                default:
                    System.out.println("잘못 입력하셨습니다.");
                    return;
            }
        }

    }

    static void showMenu(){
        System.out.println("-----------------------------------------");
        System.out.println("1.조회\t2. 입력\t3. 수정\t4. 삭제\t5. 종료");
        System.out.println("-----------------------------------------");
        System.out.print(">>");
    }


}
