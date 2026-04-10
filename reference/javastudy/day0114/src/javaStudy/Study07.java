package javaStudy;

public class Study07 {
    public static void main(String[] args) {
        Product<TV, String> product1 = new Product<>();

        product1.setKind(new TV());
        product1.setModel("Smart TV");

        TV tv = product1.getKind();
        String model = product1.getModel();

        Product<Car, String> product2 = new Product<>();

        product2.setKind(new Car());
        product2.setModel("SUV");

        Car car = product2.getKind();
        String carModel = product2.getModel();
    }
}
