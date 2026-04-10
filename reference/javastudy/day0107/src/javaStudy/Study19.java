package javaStudy;

public class Study19 {
    public static void main(String[] args) {
        int[][] scores = {
            {80, 90, 96},
            {76, 88}
        };

        System.out.println("1차원 배열 길이 : " + scores.length);
        System.out.println("2차원 배열 길이(첫번째 row)의 길이 : " + scores[0].length);
        System.out.println("2차원 배열 길이(두번째 row)의 길이 : " + scores[1].length);
    }
}
