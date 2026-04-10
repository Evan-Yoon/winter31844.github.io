package javaStudy;

public class Study12 {
    public static void main(String[] args) {
        Parent parent = new Child();

        parent.field1 = "data1";
        parent.method1();
        parent.method2();
        //parent.field2 = "data2"; // !Parent타입으로는 field2 접근 불가
        //parent.method3(); // !Parent타입으로는 method3() 호출 불가

        Child child = (Child)parent; // *강제 타입 변환
        child.field2 = "data2";
        child.method3(); // Child-method3()
    } //메탈슬러그로 생각하면 기본 권총으로 쏘다가 H를 만나면 권총을 버리고 H무기를 들고 싸우는 것과 비슷
}
