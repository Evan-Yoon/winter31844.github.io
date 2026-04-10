package javaStudy;

public class Calculator {

    double pi = 3.14159;

    //^메소드
    void powerOn() { // void는 반환값이 없다는 뜻
        System.out.println("전원을 켭니다.");
    }

    void powerOff() {
        System.out.println("전원을 끕니다.");
    }

    // int plus(int x, int y) {
    //     int result = x + y;
    //     return result;
    // }

    int plus(int ... values) {
        int sum = 0;

        for(int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    } // *가변 길이 매개변수

    double divide(int x, int y) {
        double result = (double) x / (double) y;
        return result;
    }

    double areaRectangle(double width) {
        return width * width;
    }

    double areaRectangle(double width, double height) {
        return width * height;
    }
}
