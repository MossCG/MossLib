package org.mossmc.mosscg.MossLib.Info;

import oshi.hardware.CentralProcessor;

import java.util.concurrent.TimeUnit;

public class InfoProcessor {
    /**
     * CPU基础信息
     */
    public static void updateProcessUse() throws Exception{
        CentralProcessor processor = InfoManager.hardware.getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        TimeUnit.SECONDS.sleep(1);
        long[] ticks = processor.getSystemCpuLoadTicks();

        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        usage = 1.0-(idle * 1.0 / totalCpu);
    }

    private static double usage = 0.00;

    /**
     * 获取CPU占用
     * 注意，本方法不会返回即时数值，有可能是上个10s的，请求一次后间隔1.5s左右再次请求最准确
     */
    public static double getUsage() {
        InfoManager.checkUpdate();
        return usage;
    }
}
