package javaStudy;

public class Account {
    private long balance;

    public long getBalance() {
        return this.balance;
    }

    public void deposit(int money) {
        balance += money;
    }

    public void withdraw(int money) throws InsufficientExeption {
        if(balance < money) {
            throw new InsufficientException("잔고 부족: " + (money - balance) + " 부족");
        }
        balance -= money;
    }
}
