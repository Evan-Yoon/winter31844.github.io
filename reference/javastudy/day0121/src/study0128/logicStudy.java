package study0128;

public class logicStudy {

    public static void main(String[] args) {
        int a = 1;
        if (a++ >= 1 && a >= 2) {
            a++;
        }
        System.out.println(a);

        int b = 1;
        if (b++ >= 2 && b >= 1) {
            b++;
        }
        System.out.println(b);
    }
}