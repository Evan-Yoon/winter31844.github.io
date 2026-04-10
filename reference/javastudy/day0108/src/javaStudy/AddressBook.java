package javaStudy;

public class AddressBook {

    static int cnt = 0; //주소록 개수 세기 위한 변수

    String name;
    String phone;
    int age;

    void showData() {
        System.out.println(this.name + "\t" + this.phone + "\t" + this.age);
    }
}
