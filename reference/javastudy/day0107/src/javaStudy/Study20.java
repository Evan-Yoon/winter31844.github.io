package javaStudy;

public class Study20 {
    public static void main(String[] args) {
        int[][] mathScores = new int[2][3];

        for(int i=0;i<mathScores.length;i++)
            for(int j=0;j<mathScores[i].length;j++)
                System.out.println("mathScores[" + i + "][" + j + "]=" + mathScores[i][j]);
    System.out.println();

    mathScores[0][0] = 80;
    mathScores[0][1] = 83;
    mathScores[0][2] = 85;
    mathScores[1][0] = 86;
    mathScores[1][1] = 90;
    mathScores[1][2] = 92;
    }
}
