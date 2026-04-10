package javaStudy;

public class Smartphone extends Phone { //상속받는 부분은 꼭 생성자를 만들어 줘야 한다.

    Smartphone(String owner) {
        super(owner);
    }

    void internetSearch() {
        System.out.println("인터넷 검색을 합니다.");
    }
}
