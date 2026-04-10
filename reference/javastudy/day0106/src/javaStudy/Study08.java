package javaStudy;

public class Study08 {
    public static void main(String[] args) {
        int x = 5;
        double y = 0.0;
        //double z = x / y; // 5 / 0.0 = Infinity
        double z = x % y; // 5 % 0.0 = NaN

        System.out.println("z : " + z + 2);
        //예외 처리를 하기 위해서는 어떤 값이 나오는지 알아야 함

    }
}
