package com.os.proj.manage;


public interface ProcessManage {

    void init();

    boolean create(String name);

    boolean destroy(int pid);

    boolean block(int pid);

    boolean revoke(int pid);

    boolean createByBatching(int num);

    //TODO:调度算法
    void SJF();
}
