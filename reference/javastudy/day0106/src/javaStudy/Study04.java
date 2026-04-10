package javaStudy;

public class Study04 {
    public static void main(String[] args) {
        int x = 10;
        int y = 10;
        int z;

        x++; // x = x + 1;
        ++x; // x = x + 1;
        System.out.println("x : " + x); // 12
        //두개의 차이점은 다른 연산자와 함께 사용될 때 나타남
        System.out.println("-----------------");

        y--; // y = y - 1;
        --y; // y = y - 1;
        System.out.println("y : " + y); // 8
        System.out.println("-----------------");

        z = x++; // z = 12, x = 13
        System.out.println("z : " + z); // 12
        System.out.println("x : " + x); // 13 /한 줄이 더 실행된 것과 같음
        System.out.println("-----------------");

        z = ++x; // x = 14, z = 14
        System.out.println("z : " + z); // 14
        System.out.println("x : " + x); // 14
        System.out.println("-----------------");

        //x = 14, y = 8
        z = (++x) + (y++); // z = 15 + 8, x = 15, y = 9 / 가독성을 위해 괄호 사용 권장
        System.out.println("z : " + z); // 23
        System.out.println("x : " + x); // 15
        System.out.println("y : " + y); // 9
        System.out.println("-----------------");

    }
}
