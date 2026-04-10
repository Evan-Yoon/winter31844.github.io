package javaStudy;

public class Study04 {
    public static void main(String[] args) {
        // Singleton obj1 = new Singleton(); // 생성자가 private이므로 외부에서 객체 생성 불가

        Singleton obj1 = Singleton.getInstance();
        Singleton obj2 = Singleton.getInstance();

        if(obj1 == obj2) {
            System.out.println("같은 Singleton 객체입니다.");
        } else {
            System.out.println("다른 Singleton 객체입니다.");
        }
    }
}
