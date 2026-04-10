package javaStudy;

public sealed class Person permits Employee, Manager {

    public String name;

    // public Person(String name) {
    //     this.name = name;
    // }

    public void walk() {
        System.out.println("걷습니다.");
    }

    public void work() {
        System.out.println("하는 일이 결정되지 않았습니다.");
    }
}
