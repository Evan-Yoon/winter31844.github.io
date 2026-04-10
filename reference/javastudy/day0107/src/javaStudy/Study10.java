package javaStudy;

public class Study10 {
    public static void main(String[] args) {
        String ssn = "9406081234567";
        char gender = ssn.charAt(6); //charAt() 메서드는 문자열에서 특정 인덱스에 위치한 문자를 반환한다.
        switch (gender) {
            case '1':
            case '3':
                System.out.println("남자");
                break;
            case '2':
            case '4':
                System.out.println("여자");
                break;
            default:
                System.out.println("잘못된 주민등록번호입니다.");
                break;
        }
    }
}
