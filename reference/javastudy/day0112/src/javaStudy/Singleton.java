package javaStudy;

public class Singleton {
    //^필드
    private static Singleton singleton = new Singleton();

    //^생성자
    private Singleton(){}

    //^메서드
    public static Singleton getInstance(){
        return singleton;
    }
}
// 싱글톤 패턴: 객체의 생성을 하나로 제한하는 디자인 패턴