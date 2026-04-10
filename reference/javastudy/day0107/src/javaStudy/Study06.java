package javaStudy;

public class Study06 {
    public static void main(){
        int num1;
        int num2;
        int num3;
        int num4;
        int num5;
        int num6;
        num1 = (int)(Math.random()*45) + 1;
        while (true) {
            num2 = (int)(Math.random()*45) + 1;
            if (num2 != num1) break;
        }
        while (true) {
            num3 = (int)(Math.random()*45) + 1;
            if ((num3 != num1)&(num3 != num2)) break;
        }
        while (true) {
            num4 = (int)(Math.random()*45) + 1;
            if ((num3 != num1)&(num4 != num2)&(num4 != num3)) break;
        }
        while (true) {
            num5 = (int)(Math.random()*45) + 1;
            if ((num5 != num1)&(num5 != num2)&(num5 != num3)&(num5 != num4)) break;
        }
        while (true) {
            num6 = (int)(Math.random()*45) + 1;
            if ((num6 != num1)&(num6 != num2)&(num6 != num3)&(num6 != num4)&(num6 != num5)) break;
        }
        System.out.println(num1 + " " + num2 + " " + num3 + " " + num4 + " " + num5 + " " + num6);        
    }
}