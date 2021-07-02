package com.os.proj.utils;

import com.os.proj.manage.ProcessManageImpl;
import com.os.proj.model.PCB;

import java.util.*;

public class ManageUtils {

    // 系统初始化生成的第一UID, 所有进程都为其孩子
    public static final int ROOT_PID = 0;
    // 进程运行态为1 阻塞态为2 就绪态为3
    public static final int RUNNING = 1;
    public static final int BLOCK = 2;
    public static final int READY = 3;

    /**
     * 打印进程相关信息
     * 是辅助函数
     */
    public static void printProcessInfo(Object obj) {
        if (obj instanceof List) {
            List<PCB> list = (List<PCB>) obj;
            for (PCB pcb : list)
                System.out.println(pcb);
        } else if (obj instanceof Queue) {
            Queue<PCB> queue = (Queue<PCB>) obj;
            for (PCB pcb : queue)
                System.out.println(pcb);
        } else {
            System.out.println("打印失败!");
        }
    }

    /**
     * 通过名称获取PCB对象
     * @param name PCB名称
     * @param list 含有PCB的全列表
     * @return 找到的PCB对象
     */
    public static PCB getPCBByName(String name, List<PCB> list) {
        PCB pcb = null;
        for (PCB p : list) {
            if (name.equals(p.getName())) {
                pcb = p;
            }
        }
        return pcb;
    }

    public static PCB getPCBByPID(int pid, List<PCB> list) {
        PCB pcb = null;
        for (PCB p : list) {
            if (pid == p.getPID()) {
                pcb = p;
            }
        }
        return pcb;
    }

    public static int getCurrentMaxPID(List<PCB> list) {
        Collections.sort(list, new Comparator<PCB>() {
            @Override
            public int compare(PCB o1, PCB o2) {
                return o1.getPID() - o2.getPID();
            }
        });
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return list.get(size - 1).getPID();
    }

    public static void sortPCBListUsingFCFS(List<PCB> list) {
        Collections.sort(list, new Comparator<PCB>() {
            @Override
            public int compare(PCB o1, PCB o2) {
                return (o1.getArriveTime() - o2.getArriveTime()) > 0
                        ? 1 : -1;
            }
        });
    }

    public static void sortPCBListUsingHPF(List<PCB> list) {
        Collections.sort(list, new Comparator<PCB>() {
            @Override
            public int compare(PCB o1, PCB o2) {
                return o1.getPriority() - o2.getPriority();
            }
        });
    }

    public static void printDebug(ProcessManageImpl processManage) {
        System.out.println("PCB列表:");
        ManageUtils.printProcessInfo(processManage.getPcbList());
        System.out.println("--------------------------------");
        System.out.println("RUN列表:");
        ManageUtils.printProcessInfo(processManage.getRunList());
        System.out.println("--------------------------------");
        System.out.println("READY列表:");
        ManageUtils.printProcessInfo(processManage.getReadyQueue());
        System.out.println("--------------------------------");
        System.out.println("BLOCK列表:");
        ManageUtils.printProcessInfo(processManage.getBlockQueue());
        System.out.println("--------------------------------");

    }

    public static void main(String[] args) {
        int i = 0;
        List<PCB> list = new ArrayList<>();
//        while (i < 5) {
//            PCB pcb = new PCB(i + "");
//            pcb.setPID(i);
//            list.add(pcb);
//            i++;
//        }
        ManageUtils.printProcessInfo(list);
        int currentMaxPID = ManageUtils.getCurrentMaxPID(list);
        System.out.println(currentMaxPID);
    }

}
