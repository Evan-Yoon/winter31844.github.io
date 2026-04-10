package javaStudy;

public class Study12 {
    public static void main(String[] args) {
        String oldStr = "대한민국의 대통령은 윤석열입니다.";
        String newStr = oldStr.replace("윤석열", "이재명");

        System.out.println(oldStr);
        System.out.println(newStr);
    }
}
