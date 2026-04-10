package javaStudy;

public class BankTuple {
    String date;
    String content;
    boolean isIncome;
    int amount;

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIncome(boolean isIncome) {
        this.isIncome = isIncome;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public int getAmount() {
        return amount;
    }

}
