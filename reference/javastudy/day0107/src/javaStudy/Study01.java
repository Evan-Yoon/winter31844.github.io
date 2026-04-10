package javaStudy;

public class Study01 {
    public static void main(String[] args) {
        for(float x=0;x<=1.0f;x+=0.1f) {
            System.out.println(x);
        }

        // 0.1f씩 더해지는 값이 정확히 표현되지 않아서 발생하는 문제
        // 해결 방법: 정수로 계산 후 나누기
        for(int i=0;i<=10;i++) {
            float x = i / 10.0f;
            System.out.println(x);
        }

        //전체 구구단 만들기
        for(int i=1;i<=9;i++) {   //단 반복
            for(int j=1;j<=9;j++) {  //각 단의 곱 반복
                System.out.printf("%d x %d = %d\t", i, j, i*j); //\t는 탭 간격
            }
            System.out.println();
        }
    }
}
