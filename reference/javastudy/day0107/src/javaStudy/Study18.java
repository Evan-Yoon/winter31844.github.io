package javaStudy;

public class Study18 {
    public static void main(String[] args) {
        int[] scores = new int[3];

        int sum = 0;
        for(int i=0; i<scores.length; i++) {
            sum += scores[i];
        }
        System.out.println("총합 : " + sum);

        double avg = (double)sum / scores.length; // 평균을 구할 때는 length 사용
        System.out.println("평균 : " + avg);
    }
}
