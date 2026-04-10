package javaStudy;

public class Study12 {
    public static void main(String[] args) {
        int result = 0;

        result += 10; // result = result + 10;
        System.out.println("result: " + result);

        result -= 5; // result = result - 5;
        System.out.println("result: " + result);
        result *= 3; // result = result * 3;
        System.out.println("result: " + result);
        result /= 5; // result = result / 5;
        System.out.println("result: " + result);
        result %= 3; // result = result % 3;
        System.out.println("result: " + result);
        // 복합 대입 연산자는 변수에만 사용할 수 있다.
        // 10 += result; // 에러 발생
    }
}
