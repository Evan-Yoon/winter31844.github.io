package javaStudy;

public class Study05 {
    public static void main(String[] args) {
        Account account = new Account();

        account.deposit(10000);
        System.out.println("예금액 : " + account.getBalance());

        try {
            account.withdraw(30000);
        } catch (InsufficientExeption e) {
            String message = e.getMessage();
            System.out.println(message);
        }
    }
}
