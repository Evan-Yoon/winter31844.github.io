package javaStudy2;

public class Study17 {
    public static void main(String[] args) {
    ImpleClass impl = new ImpleClass();

    InterfaceA ia = impl;
    ia.methodA();
    System.out.println();

    InterfaceB ib = impl;
    ib.methodA();
    ib.methodB();
    System.out.println();

    InterfaceC ic = impl;
    ic.methodA();
    ic.methodB();
    ic.methodC();
    }
}
