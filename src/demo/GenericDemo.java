package demo;

public class GenericDemo {

    interface Ordering<T> {
        int compare(T a, T b);
    }

    static class StringOrdering implements Ordering<String> {
        public int compare(String a, String b) {
            return a.compareTo(b);
        }
    }

    static class IntegerOrdering implements Ordering<Integer> {
        public int compare(Integer a, Integer b) {
            return a.compareTo(b);
        }
    }

    public static void main(String[] args) {
        Ordering<String> stringOrdering = new StringOrdering();
        int r = stringOrdering.compare("hello", "world");
        System.out.println(r);
    }

}
