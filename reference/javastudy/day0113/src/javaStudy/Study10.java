package javaStudy;

public class Study10 {
    public static void main(String[] args) {
    RemoteControl rc = new SmartTelevision();
    rc.turnOn();
    rc.turnOff();

    Searchable searchable = new SmartTelevision();
    searchable.searchable("www.naver.com");
    }


}
