package javaStudy;

public class Study04 {
    public static void main(String[] args) {

        try {
            findClass();
        } catch (ClassNotFoundException e) {
            System.out.println("예외 처리: " + e.toString()); //toString은 예외 클래스와 메시지를 함께 출력
        }
    }

    public static void findClass() throws ClassNotFoundException { // 예외 던지기
        Class.forName("java.lang.String2");
    }
}
