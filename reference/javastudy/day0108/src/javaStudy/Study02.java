package javaStudy;

public class Study02 {
    public static void main(String[] args) {
        int[] scores = {95, 71, 88, 60, 55};

        int sum = 0;
        for(int score : scores) {
            sum += score;
        }
        System.out.println("총합 :" + sum);
        double avg = sum / (double)scores.length;
        System.out.println("평균 : " + avg);
    }
}
