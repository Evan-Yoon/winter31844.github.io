package javaStudy;

class A{}

class B extends A{
}

class C extends A{
}

class D extends B{
}

class E extends C{
}


public class Study10 {
    public static void main(String[] args) {
        B b = new B();
        C c = new C();
        D d = new D();
        E e = new E();

        A a1 = b;
        A a2 = c;
        A a3 = d;
        A a4 = e;
        //이게 가능한 이유는 A가 B,C,D,E의 조상 클래스이기 때문이다.

        B b1 = d;
        C c1 = e;

        //B b2 = e; //E는 C의 자식이므로 B타입으로 변환 불가
        //C c2 = d; //D는 B의 자식이므로 C타입으로 변환 불가
    }
}
