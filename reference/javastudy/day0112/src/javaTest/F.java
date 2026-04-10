package javaTest;

import javaStudy.D;

public class F {
      public void method() {
        D d = new D();

        d.field1 = 1; // public이므로 어디서든 접근 가능
        d.field2 = 1; // default이므로 같은 패키지 내에서만 접근 가능
        d.field3 = 1; // private이므로 접근 불가

        d.Method1();
        d.Method2();
        d.Method3();
        // public만 되는 이유는 F 클래스가 javaTest 패키지에 있고 D 클래스가 javaStudy 패키지에 있기 때문이다.
    }
}
