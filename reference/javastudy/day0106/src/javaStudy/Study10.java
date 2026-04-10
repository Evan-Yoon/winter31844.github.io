package javaStudy;

public class Study10 {
    public static void main(String[] args) {
        int charCode = 'A'; // 'A'의 유니코드 65가 저장됨

        if((charCode <= 65) && (charCode <= 90)) {
            System.out.println("대문자이군요");
        } else if((charCode >= 97) && (charCode <= 122)) {
            System.out.println("소문자이군요");
        } else if((charCode >= 48) && (charCode <= 57)) {
            System.out.println("0~9 숫자이군요");
        } else {
            System.out.println("기타 문자이군요");
        }
        System.out.println("charCode : " + charCode);

        int value = 6;
        if((value % 2 == 0) || (value % 3 == 0)) {
            System.out.println("2 또는 3의 배수이군요");
        }
        boolean result = (value % 2 == 0) ? true : false;
        System.out.println("result : " + result);
            

    }
}
