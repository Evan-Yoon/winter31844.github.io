package javaStudy;

public class Study09 {
    public static void main(String[] args) {
        int num1 = 10;
        int num2 = 10;
        boolean result1 = (num1 == num2); // true
        System.out.println("result1 : " + result1);

        boolean result2 = (num1 != num2); // false
        System.out.println("result2 : " + result2);

        boolean result3 = (num1 <= num2); // true
        System.out.println("result3 : " + result3);

        char char1 = 'A'; // 65
        char char2 = 'B'; // 66
        boolean result4 = (char1 < char2); // true
        System.out.println("result4 : " + result4);

        int num3 = 1;
        double num4 = 1.0;
        boolean result5 = (num3 == num4); // true /더 큰 타입으로 자동 형변환 후 비교
        System.out.println("result5 : " + result5);
        //int와 double 비교 시 int가 double로 자동 형변환 됨

        float num5 = 0.1f;
        double num6 = 0.1;
        boolean result6 = (num5 == num6); // false /float와 double는 정확히 일치하지 않음
        System.out.println("result6 : " + result6);
        //float와 double 비교 시 float가 double로 자동 형변환 됨
        //0.1f -> 0.100000001490116122109375
        //0.1   -> 0.1000000000000000055511151231257827021181583404541015625
        //따라서 정확히 일치하지 않음
        // num6을 float로 형변환 하면 true가 나옴
        boolean result7 = (num5 == (float)num6); // true
        System.out.println("result7 : " + result7);

        String str1 = "자바";
        String str2 = "Java";
        boolean result8 = str1.equals(str2); //string 비교 시 equals() 사용
        boolean result9 = !(str1.equals(str2));
        System.out.println("result8 : " + result8);
        System.out.println("result9 : " + result9);
    }
}
