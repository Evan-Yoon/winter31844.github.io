package javaStudy;

public class Study08 {
    public static void main(String[] args) {
        String strVar1 = "이재명";
        String strVar2 = "이재명";

        if(strVar1 == strVar2){
            System.out.println("strVar1과 strVar2는 참조가 같다.");
        } else {
            System.out.println("strVar1과 strVar2는 참조가 다르다.");
        }

        if(strVar1.equals(strVar2)){
            System.out.println("strVar1과 strVar2는 문자열이 같음");
        }
        String strVar3 = new String("윤석열");
        String strVar4 = new String("윤석열");

        if(strVar3 == strVar4){
            System.out.println("strVar3과 strVar4는 참조가 같다.");
        } else {
            System.out.println("strVar3과 strVar4는 참조가 다르다.");
        }

        if(strVar3.equals(strVar4)){
            System.out.println("strVar3과 strVar4는 문자열이 같음");
        }
    }
}