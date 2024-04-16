package org.mossmc.mosscg.MossLib.Info;

import org.mossmc.mosscg.MossLib.BasicInfo;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * 获取系统当前负载信息的类
 * 需要先初始化
 */
public class InfoManager {
    /**
     * 初始化方法，必须先调用
     * @param loggerIn 日志模块
     * @param display 是否显示debug信息
     */
    public static void initSystemInfo(ObjectLogger loggerIn, boolean display) {
        logger = loggerIn;
        displayLog = display;
        updateInfo();
        logger.sendInfo("MossLib "+ BasicInfo.versionFull+" SystemInfo Module By "+ BasicInfo.author);
    }

    /**
     * 系统信息功能基础信息
     */
    private static ObjectLogger logger;
    private static boolean displayLog;

    /**
     * 更新间隔，单位为秒，信息超过此时长会更新
     */
    private static int updateDelay = 10;
    private static long nextUpdate;
    public static void setUpdateDelay(int delay) {
        updateDelay = delay;
    }
    public static void checkUpdate() {
        //检查更新，true为需要更新
        if (System.currentTimeMillis() > nextUpdate) updateInfo();
    }

    /**
     * OShi系统信息模块基础对象
     * 以及更新方法
     */
    public static SystemInfo info;
    public static HardwareAbstractionLayer hardware;
    public static synchronized void updateInfo() {
        nextUpdate = System.currentTimeMillis()+updateDelay*1000L;
        info = new SystemInfo();
        hardware = info.getHardware();
        Thread processorUpdate = new Thread(() -> {
            try {
                InfoProcessor.updateProcessUse();
            } catch (Exception e) {
                logger.sendException(e);
            }
        });
        processorUpdate.start();
        Thread networkUpdate = new Thread(() -> {
            try {
                InfoNetwork.updateNetworkUse();
            } catch (Exception e) {
                logger.sendException(e);
            }
        });
        networkUpdate.start();
        if (displayLog) logger.sendInfo("已更新系统信息！");
    }
}
