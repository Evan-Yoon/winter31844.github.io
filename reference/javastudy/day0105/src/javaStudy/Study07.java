package javaStudy;

public class Study07 {
    public static void main(String[] args) {
        long var1 = 10;
        long var2 = 20L;
        //long var3 = 10000000000;
        long var4 = 10000000000L;
        System.out.println("var1 = " + var1);
        System.out.println("var2 = " + var2);
        System.out.println("var4 = " + var4);
        }
}
// 작은 용량의 정수 타입을 큰 용량의 정수 타입에 대입하는 것은 문제가 없지만,
// 큰 용량의 정수 타입을 작은 용량의 정수 타입에 대입하려고 하면 오류가 발생한다.
// 그래서 long 타입의 값을 int 타입의 변수에 대입하려고 하면 오류가 발생한다.
// 위의 코드에서 var3 변수에 10000000000을 대입하려고 하면 오류가 발생한다.
// 이 문제를 해결하려면 10000000000 뒤에 L 또는 l을 붙여서 long 타입의 값임을 명시해야 한다.