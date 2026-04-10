package javaStudy;

public class Study01 {
    public static void main(String[] args) {
        Korean k1 = new Korean("940608-1234567", "이재명");

        System.out.println(k1.nation);
        System.out.println(k1.ssn);
        System.out.println(k1.name);

        //k1.nation = "일본"; // final 필드이므로 수정 불가
        //k1.ssn = "000202-9876543"; // final 필드이므로 수정 불가

        k1.name = "윤석열"; // name 필드는 수정 가능
    }
}
