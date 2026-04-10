package javaStudy;

public class B {

    A a1 = new A(true); // 어디서든 접근 가능
    A a2 = new A(1); // 같은 패키지 내에서만 접근 가능
    //A a3 = new A("문자열"); // 클래스가 다르므로 접근 불가

    public void method(){
        A a = new A();
        a.field = "value"; // protected 필드이므로 같은 패키지 내에서 접근 가능
        a.method(); // protected 메서드이므로 같은 패키지 내에서 접근 가능
    }
}
