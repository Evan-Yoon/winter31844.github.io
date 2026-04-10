package javaStudy;

public class Study02 {
    public static void main(String[] args) {
        try {
            Class.forName("java.lang.String"); //Class는 클래스 중에서 가장 최상위 클래스 / 존재하는 클래스
            System.out.println("java.lang.String 클래스가 존재합니다.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); //printStackTrace()는 예외가 발생한 위치와 원인을 추적하는 데 유용한 메서드
        }

        System.out.println();

        try {
            Class.forName("java.lang.String2"); //존재하지 않는 클래스
            System.out.println("java.lang.String2 클래스가 존재합니다.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
