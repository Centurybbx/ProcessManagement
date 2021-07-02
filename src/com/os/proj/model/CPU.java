package com.os.proj.model;

public class CPU {
    // CPU的使用状态: 0表示空闲 1表示使用中
    private static boolean isOccupied = false;

    public static boolean isOccupied() {
        return isOccupied;
    }

    public static void setStatus(boolean status) {
        CPU.isOccupied = status;
    }

}
