package javaStudy;

public class E {
    public void method() {
        D d = new D();

        d.field1 = 1;
        d.field2 = 1;
        d.field3 = 1; // private이므로 접근 불가

        d.Method1();
        d.Method2();
        d.Method3();
    }


}
