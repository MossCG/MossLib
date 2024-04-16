package org.mossmc.mosscg.MossLib.Command;

import org.mossmc.mosscg.MossLib.BasicInfo;
import org.mossmc.mosscg.MossLib.Object.ObjectCommand;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 指令管理类
 * 在使用指令功能前请先在此初始化指令
 */
public class CommandManager {
    /**
     * 指令缓存
     * 指令会被存到这里面
     * string为对应指令的args[0]
     */
    private static Map<String, ObjectCommand> commandMap;
    /**
     * MossLib的日志模块
     */
    private static ObjectLogger logger;
    /**
     * 是否显示日志
     * true情况下注册指令什么的会提示
     */
    private static boolean displayLog;

    /**
     * 初始化部分
     * 不初始化用可是会出问题的
     */
    public static void initCommand(ObjectLogger loggerIn,boolean display) {
        commandMap = new HashMap<>();
        logger = loggerIn;
        displayLog = display;
        commandThread();
        logger.sendInfo("MossLib "+ BasicInfo.versionFull+" Command Module By "+ BasicInfo.author);
    }

    /**
     * 注册指令
     * 用ObjectCommand对象注册就行
     */
    public static void registerCommand(ObjectCommand command) {
        command.prefix().forEach(prefix -> {
            if (commandMap.containsKey(prefix)) {
                logger.sendWarn("发现指令冲突！将使用后注册的指令！："+prefix);
                logger.sendWarn("旧指令："+commandMap.get(prefix).getClass().getCanonicalName());
                logger.sendWarn("新指令："+command.getClass().getCanonicalName());
            }
            commandMap.put(prefix,command);
            if (displayLog) logger.sendInfo("已注册指令："+prefix+" -> "+command.getClass().getCanonicalName());
        });

    }

    /**
     * 指令线程部分
     * 这部分会读入指令并对应处理
     */
    private static void commandThread() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        Thread commandListen = new Thread(() -> {
            try {
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(System.in, Charset.defaultCharset()));
                while (true){
                    String command = bufferedReader.readLine();
                    if (command == null) continue;
                    if (command.length() < 1) continue;
                    logger.sendCommand(command);
                    readCommand(command.split("\\s+"));
                }
            } catch (Exception e) {
                logger.sendException(e);
                logger.sendWarn("指令读取出现错误！");
            }
        });
        commandListen.setName("commandListenerThread");
        singleThreadExecutor.execute(commandListen);
    }

    /**
     * 处理指令
     * @param args 指令通过空格分隔
     */
    private static void readCommand(String[] args) {
        if (commandMap.containsKey(args[0])) {
            boolean result = commandMap.get(args[0]).execute(args,logger);
            if (!result) logger.sendWarn("指令执行出现错误！请检查！");
        } else {
            logger.sendWarn("未知指令！");
        }
    }
}
