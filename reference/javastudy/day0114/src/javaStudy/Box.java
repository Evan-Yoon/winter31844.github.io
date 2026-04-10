package javaStudy;

public class Box<T> {
    public T content;
    private T t;

    public boolean compare(Box<T> other) {
        boolean result = this.content.equals(other.content); // 참조를 비교하는 것이 아니라 Value를 비교
        return result;
    }

    public T get() {
        return t;
    }

    public void set(T t) {
        this.t = t;
    }

}
