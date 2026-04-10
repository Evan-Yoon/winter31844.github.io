package javaStudy;

public class Study13 {
    public static void main(String[] args) {
        Course.registerCourse1(new Applicant<Person>(new Person()));
        Course.registerCourse1(new Applicant<Worker>(new Worker()));
        Course.registerCourse1(new Applicant<Student>(new Student()));
        Course.registerCourse1(new Applicant<HighStudent>(new HighStudent()));
        Course.registerCourse1(new Applicant<MiddleStudent>(new MiddleStudent()));
        System.out.println();

        //Course.registerCourse2(new Applicant<Person>(new Person())); // Student의 하위타입이 아니므로 오류
        //Course.registerCourse2(new Applicant<Worker>(new Worker())); // Student의 하위타입이 아니므로 오류
        Course.registerCourse2(new Applicant<Student>(new Student()));
        Course.registerCourse2(new Applicant<HighStudent>(new HighStudent()));
        Course.registerCourse2(new Applicant<MiddleStudent>(new MiddleStudent()));
        System.out.println();

        Course.registerCourse3(new Applicant<Person>(new Person()));
        Course.registerCourse3(new Applicant<Worker>(new Worker()));
        //Course.registerCourse3(new Applicant<Student>(new Student())); // Worker의 상위타입이 아니므로 오류
        //Course.registerCourse3(new Applicant<HighStudent>(new HighStudent())); // Worker의 상위타입이 아니므로 오류
        //Course.registerCourse3(new Applicant<MiddleStudent>(new MiddleStudent())); // Worker의 상위타입이 아니므로 오류
        System.out.println();
    }
}
