package javaStudy;

public class Study15 {
    public static void main(String[] args) {
        String board = "1, 어린왕자, 철없는 왕자 이야기, 생텍쥐";

        String[] strs = board.split(", "); // ', '를 기준으로 문자열을 나누어 배열에 저장

        System.out.println("번호 : " + strs[0]);
        System.out.println("제목 : " + strs[1]);
        System.out.println("내용 : " + strs[2]);
        System.out.println("작가 : " + strs[3]);

        System.out.println();
        for(int i=0; i < strs.length; i++) {
            System.out.println(strs[i]);
        }
    }
}
