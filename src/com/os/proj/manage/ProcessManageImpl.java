package com.os.proj.manage;

import com.os.proj.model.CPU;
import com.os.proj.model.PCB;
import com.os.proj.model.Resource;
import com.os.proj.utils.ManageUtils;
import com.os.proj.utils.PCBSortByArriveTime;

import java.util.*;

public class ProcessManageImpl implements ProcessManage {
    public PCB root;
    // 系统的PCB列表，里面含有所有未被销毁的进程信息
    private List<PCB> pcbList;
    // 创建新进程的列表
    private List<PCB> runList;
    // 就绪队列
    private Queue<PCB> readyQueue;
    // 阻塞队列
    private Queue<PCB> blockQueue;

    /**
     * 初始化root进程，使其优先级为0，UID为0
     */
    @Override
    public void init() {
        root = new PCB("root");
        // root优先级为0 优先级最高
        root.setPriority(0);
        // root的UID为0 其他的依次往后
        root.setPID(ManageUtils.ROOT_PID);
        pcbList.add(root);
    }

    public ProcessManageImpl() {
        pcbList = new ArrayList<>();
        runList = new ArrayList<>();
        // 管理中默认是FCFS
        readyQueue = new PriorityQueue<>(new PCBSortByArriveTime());
        blockQueue = new PriorityQueue<>(new PCBSortByArriveTime());
    }

    public PCB getRoot() {
        return root;
    }

    public List<PCB> getPcbList() {
        return pcbList;
    }

    public List<PCB> getRunList() {
        return runList;
    }

    public Queue<PCB> getReadyQueue() {
        return readyQueue;
    }

    public Queue<PCB> getBlockQueue() {
        return blockQueue;
    }

    @Override
    public boolean create(String name) {
        boolean flag = true;
        PCB pcb = new PCB(name);
        int currentMaxPID = ManageUtils.getCurrentMaxPID(pcbList);
        pcb.setPID(currentMaxPID + 1);
        // 把PCB放到最大的PCB列表中
        pcbList.add(pcb);
        boolean isAdded = addPCB2Collection(pcb);
        if (!isAdded)
            flag = false;
        return flag;
    }

    @Override
    public boolean destroy(int pid) {
        boolean flag = true;
        PCB pcb = ManageUtils.getPCBByPID(pid, pcbList);
        pcbList.remove(pcb);
        if (pcb.getStatus() == ManageUtils.RUNNING) {
            runList.remove(pcb);
            // 进程销毁后释放资源
            pcb.release();
        } else if (pcb.getStatus() == ManageUtils.BLOCK) {
            blockQueue.remove(pcb);
        } else if (pcb.getStatus() == ManageUtils.READY) {
            readyQueue.remove(pcb);
            pcb.release();
        } else {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean block(int pid) {
        boolean flag = true;
        PCB pcb = ManageUtils.getPCBByPID(pid, pcbList);
        if (pcb.getStatus() != ManageUtils.RUNNING) {
            flag = false;
        } else {
            pcb.setStatus(ManageUtils.BLOCK);
            runList.remove(pcb);
            // 进程从运行态到阻塞态需要释放资源
            pcb.release();
            blockQueue.add(pcb);
        }
        return flag;
    }

    @Override
    public boolean revoke(int pid) {
        boolean flag = true;
        PCB pcb = ManageUtils.getPCBByPID(pid, pcbList);
        if (pcb.getStatus() != ManageUtils.BLOCK) {
            flag = false;
        } else {
            pcb.setStatus(ManageUtils.READY);
            blockQueue.remove(pcb);
            readyQueue.add(pcb);
        }
        return flag;
    }

    @Override
    public boolean createByBatching(int num) {
        boolean flag = true;
        // 可以设置种子
        Random random = new Random(11);
        int currentMaxPID = ManageUtils.getCurrentMaxPID(pcbList);
        for (int i = 0; i < num; i++) {
            PCB pcb = new PCB("test" + currentMaxPID + i);
            pcb.setPID(currentMaxPID + i);
            // 批量生成的进程其时间间隔在10~60ms之间
            try {
                Thread.sleep(random.nextInt(50)+10);
            } catch (InterruptedException e) {
                flag = false;
                e.printStackTrace();
            }
            pcbList.add(pcb);
//            boolean isAdded = addPCB2Collection(pcb);
//            if (!isAdded) {
//                flag = false;
//            }
        }
        addPCB2CollectionUsingFCFS(pcbList);
        return flag;
    }

    private void addPCB2CollectionUsingFCFS(List<PCB> list) {
        ManageUtils.sortPCBListUsingFCFS(list);
        PCB firstPcb = list.get(0);
        firstPcb.setStatus(ManageUtils.RUNNING);
        runList.add(firstPcb);
        CPU.setStatus(true);
        Resource.delSpareResource(firstPcb.getRequireRes());
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).getRequireRes() < Resource.getSpareResource()) {
                PCB readyPcb = list.get(i);
                Resource.delSpareResource(readyPcb.getRequireRes());
                readyPcb.setStatus(ManageUtils.READY);
                readyQueue.add(readyPcb);
            } else {
                for (int j = i; j < list.size(); j++) {
                    PCB blockPcb = list.get(j);
                    blockPcb.setStatus(ManageUtils.BLOCK);
                    blockQueue.add(blockPcb);
                }
                return;
            }
        }
    }

    private boolean addPCB2Collection(PCB pcb) {
        boolean flag = true;
        int status = pcb.getStatus();
        if (status == ManageUtils.RUNNING) {
            runList.add(pcb);
        } else if (status == ManageUtils.READY) {
            readyQueue.add(pcb);
        } else if (status == ManageUtils.BLOCK) {
            blockQueue.add(pcb);
        } else {
            flag = false;
        }
        return flag;
    }

    @Override
    public void SJF() {

    }

    public static void main(String[] args) {
//        ProcessManageImpl processManage = new ProcessManageImpl();
//        processManage.createByBatching(6);
//        System.out.println("PCB列表:");
//        ManageUtils.printProcessInfo(processManage.getPcbList());
//        System.out.println("--------------------------------");
//        System.out.println("RUN列表:");
//        ManageUtils.printProcessInfo(processManage.getRunList());
//        System.out.println("--------------------------------");
//        System.out.println("READY列表:");
//        ManageUtils.printProcessInfo(processManage.getReadyQueue());
//        System.out.println("--------------------------------");
//        System.out.println("BLOCK列表:");
//        ManageUtils.printProcessInfo(processManage.getBlockQueue());
//        System.out.println("--------------------------------");

    }

}
