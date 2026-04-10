package javaStudy;

public class Study09 {
    public static void main(String[] args) {
        SuperSonicAirplane ssa = new SuperSonicAirplane();
        ssa.takeOff();
        ssa.fly();// 부모의 fly() 메서드 호출
        ssa.flyMode = SuperSonicAirplane.SUPERSONIC;
        ssa.fly();// 자식의 fly() 메서드 호출
        ssa.flyMode = SuperSonicAirplane.NORMAL;
        ssa.fly(); // 부모의 fly() 메서드 호출
        ssa.land();
    }
}