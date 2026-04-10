package javaStudy;

public class Course {

    // ^모든 종류의 지원자를 받을 수 있다.
    public static void registerCourse1(Applicant<?> applicant) {
        System.out.println(applicant.kind.getClass().getSimpleName() + "이 Course1에 등록함.");
    }

    // ^학생만 등록 가능
    public static void registerCourse2(Applicant<? extends Student> applicant) {
        System.out.println(applicant.kind.getClass().getSimpleName() + "이 Course2에 등록함.");
    }

    // ^학생말고 근로자 및 그 상위 타입만 등록 가능
    public static void registerCourse3(Applicant<? super Worker> applicant) { // <? super T> : T와 그 상위 타입
        System.out.println(applicant.kind.getClass().getSimpleName() + "이 Course3에 등록함.");
    }
}
