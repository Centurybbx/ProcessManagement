package com.os.proj;

import com.os.proj.utils.ManageUtils;

import javax.xml.bind.DatatypeConverter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Demo {
    public static void main(String[] args) {
        Date date1 = new Date();
        Date date2 = null;
        SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss");
        try {
            Thread.sleep(1000);
            date2 = new Date();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(dt.format(date1));
        System.out.println(dt.format(date2));
        System.out.println(dt.format(date2).compareTo(dt.format(date1)));
        int i = new Random().nextInt(10);
        System.out.println(i);
    }
}
