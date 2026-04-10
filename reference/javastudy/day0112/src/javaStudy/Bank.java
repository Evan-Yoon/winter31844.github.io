package javaStudy;

public class Bank {

    //^ 1. 필드
    // 싱글톤 인스턴스 생성
    private static Bank bank = new Bank();

    // 데이터 저장소 (static을 빼고 인스턴스 변수로 변경 추천)
    private BankTuple[] bankBook = new BankTuple[100];
    private int cnt = 0; // static 제거 (인스턴스별 관리, 싱글톤이라 상관없으나 통일성 유지)

    //^ 2. 생성자 (외부 생성 차단)
    private Bank(){}

    public static Bank getInstance() {
        return bank;
    }

    //^ 3. 메서드

    // 외부에서 싱글톤 인스턴스를 가져가기 위한 메서드 추가
    public static Bank getBank() {
        return bank;
    }

    public void setBankTuple(BankTuple bt){
        // 배열 범위 초과 방지 로직을 넣는 것이 안전함
        if (cnt < bankBook.length) {
            bank.bankBook[cnt++] = bt;
        } else {
            System.out.println("더 이상 저장할 수 없습니다.");
        }
    }

    public void showBankBook(){
        int sum = 0;

        System.out.println("날짜\t내역\t구분\t금액\t잔액");
        System.out.println("----------------------------------------");

        for(int i=0; i < cnt; i++){
            // 문법 오류 수정: if-else 블록에 중괄호 명시
            if(bank.bankBook[i].isIncome()) {
                sum += bank.bankBook[i].getAmount();
            } else {
                sum -= bank.bankBook[i].getAmount();
            }

            // 출력문이 for문 안에 있어야 내역이 하나씩 출력됨
            System.out.println(
                bank.bankBook[i].getDate() + "\t" +
                bank.bankBook[i].getContent() + "\t" +
                (bank.bankBook[i].isIncome() ? "입금" : "출금") + "\t" +
                bank.bankBook[i].getAmount() + "\t" +
                sum
            );
        }
    }
}