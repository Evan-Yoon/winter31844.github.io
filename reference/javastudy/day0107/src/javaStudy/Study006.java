package javaStudy;

public class Study006 {
    public static void main(String[] args) {
        int[] arr1; //배열 변수 arr1 선언
        int[] arr2;
        int[] arr3;

        arr1 = new int [] {1,2,3};
        arr2 = new int [] {1,2,3};
        arr3 = arr2;
        System.out.println(arr1 == arr2); //주소가 다르기 때문에 다르다고 하는 것.
        System.out.println(arr2 == arr3);
    }
}
