package javaStudy;

public class Study07 {
    public static void main(String[] args) {
        Korean k1 = new Korean("이재명", "630505-1234567");
        System.out.println("국적 : " + k1.nation);
        System.out.println("이름 : " + k1.name);
        System.out.println("주민등록번호 : " + k1.ssn);

        Korean k2 = new Korean("윤석열", "600115-1345678");
        System.out.println("국적 : " + k2.nation);
        System.out.println("이름 : " + k2.name);
        System.out.println("주민등록번호 : " + k2.ssn);
        System.out.println();
    }
}
