package javaStudy;

public class CalculatorStatic {

    static double pi = 3.14159; // static을 쓰면 객체 생성 없이 사용 가능 /static이 없으면 객체 생성 후 사용 가능

    //^메소드
    static void powerOn() { // void는 반환값이 없다는 뜻
        System.out.println("전원을 켭니다.");
    }

    static void powerOff() {
        System.out.println("전원을 끕니다.");
    }

    // int plus(int x, int y) {
    //     int result = x + y;
    //     return result;
    // }

    static int plus(int ... values) {
        int sum = 0;

        for(int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    } // *가변 길이 매개변수

    static double divide(int x, int y) {
        double result = (double) x / (double) y;
        return result;
    }

    static double areaRectangle(double width) {
        return width * width;
    }

    static double areaRectangle(double width, double height) {
        return width * height;
    }
}
