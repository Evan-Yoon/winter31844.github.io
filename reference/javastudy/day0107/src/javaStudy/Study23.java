package javaStudy;

public class Study23 {
    public static void main(String[] args) {
        String[] oldStrArray = {"이재명", "윤석열", "문재인"};

        String[] newStrArray = new String[5];

        System.arraycopy(oldStrArray, 0, newStrArray, 0, oldStrArray.length); // 배열 oldStrArray의 값을 newStrArray에 복사

        for(int i=0;i < newStrArray.length;i++) {
            System.out.print(newStrArray[i] + ", ");
        } // newStrArray의 값 출력
    }
}
