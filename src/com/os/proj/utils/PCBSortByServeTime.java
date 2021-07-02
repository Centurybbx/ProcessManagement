package com.os.proj.utils;

import com.os.proj.model.PCB;

import java.util.Comparator;

public class PCBSortByServeTime implements Comparator<PCB> {
    @Override
    public int compare(PCB o1, PCB o2) {
        return o1.getServeTime() - o2.getServeTime();
    }
}
