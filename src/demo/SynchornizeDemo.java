package demo;

public class SynchornizeDemo {

    int i = 0;
    Object lock = new Object();

    void test(){

        synchronized(lock){
            i += 1;    // do something
        }
    }

}
