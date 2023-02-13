package demo;

public class Hello {

    public static void main(String[] args) {
        int n = 10;
        int s = sums(n);
        System.out.println("Sum of 1 to " + n + " is " + s);
    }

    static int sums(int a) {
        int r = 0;
        for(int i = 0; i < a; i++) {
            r += i;
        }
        return r;
    }

}
