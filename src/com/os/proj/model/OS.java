package com.os.proj.model;

import com.os.proj.manage.ProcessManageImpl;
import com.os.proj.utils.ManageUtils;

import java.util.List;
import java.util.Queue;


/**
 * 模拟进程管理的总系统
 * 所有操作都在这里进行
 */
public class OS {
    private ProcessManageImpl processManage;
    // 处理器运行时间为1000ms
    private static final int SERVE_TIME = 1000;
    // 0表示FCFS 1表示SJF 2表示优先级 默认是0
    private static int schedulingAlgo = 0;

    public OS() {
        processManage = new ProcessManageImpl();
//        processManage.init();
        processManage.createByBatching(6);
    }

    public static int getSchedulingAlgo() {
        return schedulingAlgo;
    }

    public ProcessManageImpl getProcessManage() {
        return processManage;
    }

    public void setSchedulingAlgo(int schedulingAlgo) {
        this.schedulingAlgo = schedulingAlgo;
    }

    public void serve() {
        List<PCB> pcbList = processManage.getPcbList();
        List<PCB> runList = processManage.getRunList();
        Queue<PCB> blockQueue = processManage.getBlockQueue();
        Queue<PCB> readyQueue = processManage.getReadyQueue();
        // 当CPU占用并且进程正在运行时
        while (!pcbList.isEmpty()) {
            try {
                Thread.sleep(OS.SERVE_TIME);
                int schedulingAlgo = OS.getSchedulingAlgo();
                if (schedulingAlgo == 0)
                    FCFSServe(runList, blockQueue, readyQueue);
                else
                    HPFServe(pcbList, runList, blockQueue, readyQueue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void HPFServe(List<PCB> pcbList, List<PCB> runList, Queue<PCB> blockQueue, Queue<PCB> readyQueue) {
        ManageUtils.sortPCBListUsingHPF(pcbList);
//        ManageUtils.printDebug(processManage);
        FCFSServe(runList, blockQueue, readyQueue);
    }

    private void FCFSServe(List<PCB> runList, Queue<PCB> blockQueue, Queue<PCB> readyQueue) {
        // 每次执行则执行时间减1
        PCB runningPcb = null;
        // 在单核CPU中只能运行一个进程
        if (processManage.getRunList().get(0) != null)
            runningPcb = processManage.getRunList().get(0);
        else
            return;
        // fuck you!
        int serveTime = runningPcb.getServeTime();
        if (serveTime != 0)
            runningPcb.setServeTime(serveTime - 1);
        else {
            // 销毁结束的进程
            processManage.destroy(runningPcb.getPID());
            // 把block的转换为ready
            block2Ready(blockQueue, readyQueue);
            ready2Run(readyQueue, runList);
            System.out.println("\n\n\n\n");
            ManageUtils.printDebug(processManage);
        }
    }

    private void block2Ready(Queue<PCB> blockQueue, Queue<PCB> readyQueue) {
        int size = blockQueue.size();
        if (blockQueue.isEmpty()) {
            System.out.println("阻塞队列为空,无法再转换!");
        }
        for (int i = 0; i < size; i++) {
            PCB firstBlockPcb = blockQueue.peek();
            if (firstBlockPcb.getRequireRes() < Resource.getSpareResource()) {
                PCB block2ReadyPcb = blockQueue.poll();
                block2ReadyPcb.setStatus(ManageUtils.READY);
                Resource.delSpareResource(block2ReadyPcb.getRequireRes());
                readyQueue.add(block2ReadyPcb);
            } else {
                return;
            }
        }
    }

    private void ready2Run(Queue<PCB> readyQueue, List<PCB> runList) {
        if (!readyQueue.isEmpty()) {
            PCB ready2RunPcb = readyQueue.poll();
            ready2RunPcb.setStatus(ManageUtils.RUNNING);
            runList.add(ready2RunPcb);
        } else {
            System.out.println("就绪队列已空，无法加入执行!");
        }
    }

    public static void main(String[] args) {
        OS os = new OS();
//        ManageUtils.printDebug(os.getProcessManage());
        os.setSchedulingAlgo(1);
        os.serve();
    }

}
