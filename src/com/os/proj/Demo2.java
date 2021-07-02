package com.os.proj;

import java.util.Calendar;
import java.util.Random;

public class Demo2 {

    public static void main(String[] args) throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
        long timeInMillis1 = calendar.getTimeInMillis();
        Thread.sleep(11);
        calendar = Calendar.getInstance();
        long timeInMillis2 = calendar.getTimeInMillis();
        System.out.println(timeInMillis1);
        System.out.println(timeInMillis2);
        System.out.println(timeInMillis1 < timeInMillis2);
        System.out.println(new Random().nextInt(10));
    }

}
