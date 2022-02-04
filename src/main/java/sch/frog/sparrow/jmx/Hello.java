package sch.frog.sparrow.jmx;

import java.util.Random;

public class Hello implements HelloMBean{

    private final String application;

    public Hello(String application) {
        this.application = application;
    }

//    @Override
//    public String getName() {
//        return application;
//    }
//
//    @Override
//    public int add(int a, int b) {
//        return a + b;
//    }

    @Override
    public Integer getRandom() {
        return new Random().nextInt(100);
    }
}
