package javaStudy;

public class Korean {
    //^필드
    String nation = "대한민국";
    String name;
    String ssn;

    //^생성자
    public Korean(String n, String s) {
        this.name = n;
        ssn = s; //필드와 매개변수 이름이 다르면 this생략 가능
    }
}
