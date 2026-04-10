package javaStudy;

public class Study22 {
    public static void main(String[] args) {
        int[] oldIntArray = {1, 2, 3};

        int[] newIntArray = new int[5];

        for(int i=0; i < oldIntArray.length; i++) {
            newIntArray[i] = oldIntArray[i];
        } // 배열 oldIntArray의 값을 newIntArray에 복사

        for(int i=0;i < newIntArray.length;i++) {
            System.out.println(newIntArray[i]);
        } // newIntArray의 값 출력
    }
}
