package javaStudy;

public class SmartPhone extends Phone {

    public boolean wifi;

    public SmartPhone(String model, String color) {
        super(); // 부모 클래스의 생성자 호출
        this.model = model;
        this.color = color;
        System.out.println("SmartPhone(String model, String color) 생성자 호출");
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
        System.out.println("와이파이 상태 변경");
    }

    public void internet() {
        if (wifi == true) {
            System.out.println("인터넷에 연결합니다.");
        }
    }
}
