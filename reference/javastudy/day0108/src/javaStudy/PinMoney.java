package javaStudy;

public class PinMoney {


    //^필드
    static int cnt = 0; // 내역 번호

    String dateStr; // 날짜
    String content; // 내용
    int amount;   // 금액
    boolean isIncome; // *수입(true)/지출(false)
    static int totalMoney = 0; // 총 수입 금액

    //^생성자
    public PinMoney(String date, String content, int amount, boolean income) {
        this.dateStr = date;
        this.content = content;
        this.amount = amount;
        this.isIncome = income;
        cnt++;
        if(income) {
            totalMoney += amount;
        } else {
            totalMoney -= amount;
        }
    }

    //^메서드
    void showData() {
        System.out.println(this.dateStr + "\t" + this.content + "\t" + this.amount + "\t" + (this.isIncome ? "수입" : "지출")); // *삼항연산자
    }
}
