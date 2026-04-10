package javaStudy;

public class Study14 {
    public static void main(String[] args) {
        String subject = "자바 프로그래밍";

        int location = subject.indexOf("프로그래밍"); // '프로그래밍' 문자열의 시작 인덱스 반환
        String substring = subject.substring(location); // '프로그래밍'부터 끝까지의 부분 문자열 반환

        System.out.println(substring); // 출력: 프로그래밍

        location = subject.indexOf("자바"); // '자바' 문자열의 시작 인덱스 반환
        if(location != -1) { // '자바' 문자열이 존재하는지 확인 // indexOf는 찾는 문자열이 없으면 -1 반환
            System.out.println("자바와 관련된 책");
        } else {
            System.out.println("자바와 관련 없는 책");
        }

        boolean result = subject.contains("파이썬");
        if(result) {
            System.out.println("자바와 관련된 책");
        } else {
            System.out.println("자바와 관련 없는 책");
        }
    }
}