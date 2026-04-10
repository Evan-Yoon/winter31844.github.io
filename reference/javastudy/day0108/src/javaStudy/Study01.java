package javaStudy;

public class Study01 {
    public static void main(String[] args) {
        int[] lotto = new int[6];
        for(int i = 0; i<lotto.length; i++) {
            lotto[i] = (int)(Math.random()*45+1);

            for(int j=0;j<i;j++) {
                if(lotto[i] == lotto [j]) {
                    i--; //*중복된 숫자가 나오면 다시 뽑기 위해 i를 하나 줄여줌
                    break;
                }
            }
        }

        for(int i = 0;i < (lotto.length - 1);i++) { // *-1을 해주는 이유는 마지막 숫자는 비교할 필요가 없기 때문 // 버블정렬
            for(int j = i + 1; j < lotto.length; j++) {
                if(lotto[i] > lotto[j]) {
                    int t = lotto[i];
                    lotto[i] = lotto[j];
                    lotto[j] = t;
                } //*오름차순이 되는 이유는 if문에서 큰 수를 뒤로 보내기 때문
            }
        }

        for(int i = 0; i<lotto.length; i++) {
            System.out.print(lotto[i] + " ");
        }
    }
}
