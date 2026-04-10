package javaTest;

import javaStudy.A;
import javaStudy.B;
//import javaStudy.A; // A 클래스는 default이므로 같은 패키지에서만 접근 가능

public class C {
    A a1 = new A(true); // public 생성자이므로 어디서든 접근 가능
    //A a2 = new A(1); // default 생성자이므로 같은 패키지 내에서만 접근 가능
    //A a3 = new A("문자열"); // private 생성자이므로 같은 클래스 내에서만 접근 가능

    public void method(){
        //A a = new A(); // protected 생성자이므로 다른 패키지에서 접근 불가
        //a.field = "value"; // protected 필드이므로 다른 패키지에서 접근 불가
        //a.method(); // protected 메서드이므로 다른 패키지에서 접근 불가
    }
}
