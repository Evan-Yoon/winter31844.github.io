package javaStudy;

public class A { //default 클래스

    protected String field; // protected 필드

    A a1 = new A(true); // public 생성자이므로 어디서든 접근 가능
    A a2 = new A(1); // default 생성자이므로 같은 패키지 내에서만 접근 가능
    A a3 = new A("문자열"); // private 생성자이므로 같은 클래스 내에서만 접근 가능

    protected A(){} // protected 생성자

    public A(boolean b) {
    }

    A(int b) {
    }

    private A(String s) {
    }

    protected void method(){} // protected 메서드
}
