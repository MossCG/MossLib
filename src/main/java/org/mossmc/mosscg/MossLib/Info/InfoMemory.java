package org.mossmc.mosscg.MossLib.Info;

import static org.mossmc.mosscg.MossLib.Info.InfoManager.*;

public class InfoMemory {
    /**
     * 内存基础信息
     */
    public static long getMemoryAvailableByte() {
        checkUpdate();
        return hardware.getMemory().getAvailable();
    }
    public static long getMemoryTotalByte() {
        checkUpdate();
        return hardware.getMemory().getAvailable();
    }
    public static long getMemoryUsedByte() {
        checkUpdate();
        return hardware.getMemory().getAvailable()-hardware.getMemory().getAvailable();
    }
    public static double getMemoryAvailableGigabyte() {
        return getMemoryAvailableByte()/1024.0/1024.0/1024.0;
    }
    public static double getMemoryTotalGigabyte() {
        return getMemoryTotalByte()/1024.0/1024.0/1024.0;
    }
    public static double getMemoryUsedGigabyte() {
        return getMemoryUsedByte()/1024.0/1024.0/1024.0;
    }
}
