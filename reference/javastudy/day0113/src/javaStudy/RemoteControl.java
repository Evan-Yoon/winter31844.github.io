package javaStudy;

public interface RemoteControl {

    // ^상수필드
    int MAX_VOLUME = 10;
    int MIN_VOLUME = 0;

    // ^추상 메소드
    public void turnOn();
    public void turnOff();
    public void setVolume(int setVolume);

    // ^디폴트 인스턴스 메소드
    default void setMute(boolean mute){
        if(mute){
            System.out.println("무음 처리합니다.");
            setVolume(MIN_VOLUME);
        } else {
            System.out.println("무음을 해제합니다.");
        }
    }

    // ^정적 메소드
    static void changeBattery(){
        System.out.println("리모컨의 배터리를 교환합니다.");
    }
}
