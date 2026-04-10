package javaStudy;

public interface Service {
    // ^defalut method
    default void defaultMethod1(){
        System.out.println("defaultMethod1 종속 코드");
        defaultCommon();
        staticCommon(); // ^default method에서 static method의 private static method 호출 가능
    }

    default void defaultMethod2(){
        System.out.println("defaultMethod2 종속 코드");
        defaultCommon();
    }

    // ^private method
    private void defaultCommon(){
        System.out.println("defaultCommon 중복 코드A");
        System.out.println("defaultCommon 중복 코드B");
    }

    // ^static method
    static void staticMethod1(){
        System.out.println("staticMethod1 종속 코드");
        staticCommon();
    }

    static void staticMethod2(){
        System.out.println("staticMethod2 종속 코드");
        staticCommon();
    }

    // ^private static method
    private static void staticCommon(){
        System.out.println("staticCommon 중복 코드C");
        System.out.println("staticCommon 중복 코드D");
    }
}

