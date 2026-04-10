package javaStudy;

public class Study06 {
    public static void main(String[] args) {
        Car myCar = new Car();

        System.out.println("제작회사 : " + myCar.company);
        System.out.println("모델명 : " + myCar.model);
        System.out.println("색상 : " + myCar.color);
        System.out.println("최고속도 : " + myCar.maxSpeed);
        System.out.println("현재속도 : " + myCar.speed);

        myCar.speed = 60;
        System.out.println("현재속도 : " + myCar.speed);

        System.out.println();

        Car yourCar = new Car("소나타", "흰색", 300);

        System.out.println("제작회사 : " + yourCar.company);
        System.out.println("모델명 : " + yourCar.model);
        System.out.println("색상 : " + yourCar.color);
        System.out.println("최고속도 : " + yourCar.maxSpeed);
        System.out.println("현재속도 : " + yourCar.speed);
    }
}
