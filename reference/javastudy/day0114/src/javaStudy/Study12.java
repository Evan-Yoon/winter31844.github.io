package javaStudy;

public class Study12 {

    public static <T extends Number> boolean compare(T t1, T t2) { // T는 Number의 자손이어야 한다.
        System.out.println("compare(" + t1.getClass().getSimpleName() + ", "+ t2.getClass().getSimpleName() + ")"); // getClass().getSimpleName() : 클래스 이름을 문자열로 반환
        double v1 = t1.doubleValue(); //double로 강제 변환
        double v2 = t2.doubleValue();

        return (v1 == v2);
    }

    public static void main(String[] args) {
        boolean result1 = compare(10, 10.0);
        System.out.println(result1);

        System.out.println();

        boolean result2 = compare(4.5, 4.5);
        System.out.println(result2);
    }
}
