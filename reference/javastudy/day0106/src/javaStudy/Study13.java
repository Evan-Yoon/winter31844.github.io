package javaStudy;

public class Study13 {
    public static void main(String[] args) {
        int score = 77;
        char grade = (score >= 90) ? 'A' :
                     (score >= 80) ? 'B' :
                     (score >= 70) ? 'C' :
                     (score >= 60) ? 'D' : 'F';
        System.out.println("Score: " + score + ", Grade: " + grade);
    }
}
