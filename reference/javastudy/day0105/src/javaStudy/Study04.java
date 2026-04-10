package javaStudy;

public class Study04 {
    public static void main(String[] args) {
        int x = 10;
        int y = 20;
        System.out.println(x + "," + y);

        int t = x;
        x = y;
        y = t;
        System.out.println(x + "," + y);
    }
}
