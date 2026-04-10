package javaStudy;

public class Study11 {
    public static void main(String[] args) {
        String ssn = "9406081234567";
        int length = ssn.length();

        if(length == 13){
            System.out.println("정상적인 번호");
        } else {
            System.out.println("잘못된 번호");
        }
    }
}

