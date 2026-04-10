package javaTest;

import javaStudy.A;

public class D extends A{
    public D() {
        super(); // protected 생성자이므로 자식 클래스에서 접근 가능
    }

    public void method1(){
        this.field = "value";
        this.method1();
    }

    public void method2(){
        // 직접 객체 생성해서 사용하는 것은 불가능
        A a = new A();
        //a.field = "value"; // protected 필드이므로 자식 클래스가 아니면 접근 불가
        //a.method(); // protected 메서드이므로 자식 클래스가 아니면 접근 불가
    }
}
