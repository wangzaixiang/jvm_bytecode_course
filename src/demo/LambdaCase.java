package demo;

import java.util.concurrent.Callable;

public class LambdaCase {

    private int age;

    private void showAge(){
        System.out.println("age = " + age);
    }

    public static void main(String[] args) {

        int n = 10;

        int sums = profile(() -> {
            int s = 0;
            for (int i = 0; i < n; i++) {
                s += i;
            }
            return s;
        });

        System.out.println("Sum of 1 to " + n + " is " + sums);

    }

    void testPrivateAccess() throws Exception {
        Callable<Void> callable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                System.out.println("age = " + age);
                showAge();
                return null;
            }
        };
        callable.call();
    }

    static <T> T profile(Callable<T> callable) {
        long start = System.currentTimeMillis();
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            long end = System.currentTimeMillis();
            System.out.println("Time taken: " + (end - start) + "ms");
        }
    }

}
