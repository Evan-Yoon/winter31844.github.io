package javaStudy;

public class Study02 {
    public static void main(String[] args) {
        Person p1 = new Person("이재명");
        personInfo(p1);

        System.out.println("-------------------");

        Person p2 = new Student("윤석열", 10);
        personInfo(p2);
    }

    public static void personInfo(Person person) {
        System.out.println("이름: " + person.name);
        person.walk();

        // if(person instanceof Student) { //instanceof: 형변환 가능여부 확인
        //     Student student = (Student)person;
        //     System.out.println("studentNo: " + student.studentNo);
        //     student.study();
        // }

        if(person instanceof Student student) { //바로 객체를 선언하면서 형변환
            System.out.println("studentNo: " + student.studentNo);
            student.study();
        }
    }
}
