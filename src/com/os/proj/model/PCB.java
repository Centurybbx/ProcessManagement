package com.os.proj.model;

import com.os.proj.utils.ManageUtils;
import com.os.proj.utils.PCBSortByArriveTime;

import java.util.*;

public class PCB {
    private int PID;
    private String name;
    private int status;
    private int priority;
    private int serveTime;
    private long arriveTime;
    private int requireRes;
    private List<PCB> children;

    // PCB初始化
    public PCB(String name) {
        this.name = name;
        //TODO: 唯一PID设置问题 和 父进程子进程问题
        // 优先级默认为0~10
        this.priority = new Random().nextInt(10);
        Calendar calendar = Calendar.getInstance();
        this.arriveTime = calendar.getTimeInMillis();
//        this.arriveTime = new SimpleDateFormat("hh:mm:ss").format(new Date());
        // 服务时间设置为1-5s内的随机整数
        this.serveTime = new Random().nextInt(5) + 1;
        // 系统资源的请求值在0~800
        this.requireRes = new Random().nextInt(800);
        // 最后请求资源
//        this.request();
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getServeTime() {
        return serveTime;
    }

    public void setServeTime(int serveTime) {
        this.serveTime = serveTime;
    }

    public long getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(long arriveTime) {
        this.arriveTime = arriveTime;
    }

    public int getRequireRes() {
        return requireRes;
    }

    public void setRequireRes(int requireRes) {
        this.requireRes = requireRes;
    }

    public List<PCB> getChildren() {
        return children;
    }

    public void setChildren(List<PCB> children) {
        this.children = children;
    }

    public void delServeTime(int time) {
        this.serveTime -= time;
    }

//    public void request() {
//        // 进程申请资源判断
//        if (Resource.getSpareResource() < this.requireRes) {
//            this.status = ManageUtils.BLOCK;
//            // 如果CPU此时不空闲
//        } else if (!CPU.getStatus()) {
//            // 此时CPU不空闲但可以申请到资源
//            this.status = ManageUtils.READY;
//        } else {
//            // 此时CPU空闲且进程可以申请到资源
//            this.status = ManageUtils.RUNNING;
//            // 资源申请成功后减少
//            Resource.delSpareResource(this.requireRes);
//        }
//    }

    public void request() {
        if (this.getRequireRes() < Resource.getSpareResource() &&
                !CPU.isOccupied()) {
            this.status = ManageUtils.RUNNING;
            // CPU被占用
            CPU.setStatus(true);
            Resource.delSpareResource(this.getRequireRes());
        } else if (this.getRequireRes() < Resource.getSpareResource() &&
                CPU.isOccupied()) {
            this.status = ManageUtils.READY;
            Resource.delSpareResource(this.getRequireRes());
        } else if (Resource.getSpareResource() < this.getRequireRes()) {
            this.status = ManageUtils.BLOCK;
        }
    }

    public void release() {
        Resource.addSpareResource(this.getRequireRes());
        // 释放资源 此时CPU不再被占用
        CPU.setStatus(false);
    }

    @Override
    public String toString() {
        return "PCB{" +
                "PID=" + PID +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", serveTime=" + serveTime +
                ", arriveTime='" + arriveTime + '\'' +
                ", requireRes=" + requireRes +
                ", children=" + children +
                '}';
    }


    public void sortByST() throws InterruptedException {
        Queue<PCB> queue = new PriorityQueue<>(new PCBSortByArriveTime());
        for (int i = 0; i < 3; i++) {
            Thread.sleep(20);
            PCB pcb = new PCB(i + "");
            queue.add(pcb);
        }
        PCB asd = new PCB("asd");
        asd.setArriveTime(1125219999999l);
        queue.add(asd);
        int size = queue.size();
        for (int i = 0; i < size; i ++) {

            System.out.println(queue.poll());
        }
    }

    public static void main(String[] args) {
        PCB pcb = new PCB("aaa");
        try {
            pcb.sortByST();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        Queue<PCB> queue = new LinkedList<>();
//        for (int i = 0; i < 3; i++) {
//            PCB pcb = new PCB(i + "");
//            queue.add(pcb);
//        }
        //todo serveTime 
//        Collections.sort(queue, new PCBSortByServeTime());
    }
}
