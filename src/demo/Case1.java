package demo;

public class Case1 {

    public static void main(String[] args) {
        String hello = "Hello";
        String world = "world";
        String s1 = hello + world;
        String s2 = new StringBuilder().append(hello).append(world).toString();
    }

}
