package javaStudy;

public class Study13 {
    public static void main(String[] args) {
        String ssn = "940608-1234567";
        
        String firstNum = ssn.substring(0, 6); // 0번 인덱스부터 6번 인덱스 직전까지 잘라낸다.
        System.out.println(firstNum);

        String secondNum = ssn.substring(7); // 7번 인덱스부터 끝까지 잘라낸다.
        System.out.println(secondNum);
    }
}
