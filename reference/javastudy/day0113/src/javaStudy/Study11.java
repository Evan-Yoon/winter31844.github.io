package javaStudy;

public class Study11 {
    public static void main(String[] args) {
        InterfaceCImpl impl = new InterfaceCImpl();

        InterfaceA ia = impl;
        ia.methodA();
        //ia.methodB(); // 컴파일 에러
        System.out.println();

        InterfaceB ib = impl;
        ib.methodB();
        //ib.methodA(); // 컴파일 에러
        System.out.println();

        InterfaceC ic = impl;
        ic.methodA();
        ic.methodB();
        ic.methodC();
        }
}
