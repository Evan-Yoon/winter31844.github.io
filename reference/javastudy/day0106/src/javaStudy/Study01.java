package javaStudy;

public class Study01 {
    public static void main(String[] args) {
        
    /*  println();
        print();
        printf(); */
    
        System.out.println("Java"); // ln = line
        System.out.println("Study"); // ln = line

        System.out.print("Java"); // 줄바꿈 없음
        System.out.print("Study"); // 줄바꿈 없음

        System.out.println(); // 줄바꿈

        int value = 1000;
        System.out.printf("가격 : %d원\n", value); // %d : 10진 정수 형태로 출력
        System.out.printf("가격 : %6d원\n", value); // 6칸 확보 후 오른쪽 정렬
        System.out.printf("가격 : %-6d원\n", value); // 6칸 확보 후 왼쪽 정렬
        System.out.printf("가격 : %06d원\n", value); // 6칸 확보 후 빈칸 0으로 채우기

        double area = 3.14159 * 10 * 10;
        System.out.printf("반지름이 %d인 원의 넓이 : %10.2f\n", 10, area); // 전체 10칸, 소수점 이하 2자리

        String name = "이재명";
        String job = "대통령";
        System.out.printf("%6d | %-10s | %10s", 1, name, job);
    }
}
