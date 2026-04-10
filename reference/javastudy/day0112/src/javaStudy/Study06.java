package javaStudy;

public class Study06 {
    public static void main(String[] args) {
        SmartPhone myPhone = new SmartPhone("갤럭시", "블랙");

        System.out.println("모델: " + myPhone.model);
        System.out.println("색상: " + myPhone.color);

        System.out.println("와이파이 상태: " + myPhone.wifi);

        myPhone.bell();
        myPhone.sendVoice("여보세요");
        myPhone.receiveVoice("안뇽~! 100만원 입금하세요!");
        myPhone.sendVoice("뭐야 이 스팸전화는?");
        myPhone.hangUp();

        myPhone.setWifi(true);
        myPhone.internet();
    }
}
