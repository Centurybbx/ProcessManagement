package com.os.proj.model;

public class Resource {
    // 空闲资源
    private static int spareResource = 1000;

    public static int getSpareResource() {
        return spareResource;
    }

    public static void delSpareResource(int occupiedRes) {
        Resource.spareResource -= occupiedRes;
    }

    public static void addSpareResource(int releasedRes) {
        Resource.spareResource += releasedRes;
    }
}
