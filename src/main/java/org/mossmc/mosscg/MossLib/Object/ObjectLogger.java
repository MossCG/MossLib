package org.mossmc.mosscg.MossLib.Object;

import org.mossmc.mosscg.MossLib.BasicInfo;
import org.mossmc.mosscg.MossLib.Logger.LoggerManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 本类为MossLib的日志对象
 * 基本上这个轮子里面的大部分东西都会需要这个Logger
 * 实际上也就是把三种logger包装到了一个类里面
 */
public class ObjectLogger {
    /**
     * 获取Logger对象
     * @param logSave 日志保存位置（文件夹），为null则不保存日志
     */
    public ObjectLogger(String logSave,java.util.logging.Logger logger) {
        setLogger(logger);
        init(logSave);
    }
    public ObjectLogger(String logSave,org.apache.logging.log4j.Logger logger) {
        setLogger(logger);
        init(logSave);
    }
    public ObjectLogger(String logSave) {
        removeLogger();
        init(logSave);
    }

    private void init(String logSave) {
        logDir = logSave;
        timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        systemOut = new BufferedWriter(new OutputStreamWriter(System.out));
        initWriter();
        sendInfo("MossLib "+BasicInfo.versionFull+" Logger Module By "+ BasicInfo.author);
    }

    /**
     * 基础发送信息方法
     * 平时调用这一块就够用了
     */
    public void sendInfo(String message) {
        sendMessage(message, LoggerManager.logLevel.INFO);
    }

    public void sendWarn(String message) {
        sendMessage(message, LoggerManager.logLevel.WARN);
    }

    public void sendError(String message) {
        sendMessage(message, LoggerManager.logLevel.ERROR);
    }

    public void sendAPI(String message, boolean display) {
        if (display) {
            sendMessage(message, LoggerManager.logLevel.API);
        } else {
            String prefix = "["+ LoggerManager.logLevel.API +"] ";
            String time = "["+timeFormat.format(new Date(System.currentTimeMillis()))+"] ";
            writeMessage(time+prefix+message);
        }
    }

    public void sendCommand(String message) {
        sendMessage(message, LoggerManager.logLevel.COMMAND);
    }

    public void sendException(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter= new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        sendMessage(String.valueOf(stringWriter), LoggerManager.logLevel.EXCEPTION);
        try {
            printWriter.close();
            stringWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 日志类型
     * 有 default java log4j
     * 可以通过setLogger来兼容多种logger框架
     * 默认直接print
     */
    private String loggerType = "default";
    private java.util.logging.Logger getJavaLogger;
    private org.apache.logging.log4j.Logger getL4JLogger;

    public void setLogger(java.util.logging.Logger logger) {
        loggerType = "java";
        getJavaLogger = logger;
    }
    public void setLogger(org.apache.logging.log4j.Logger logger) {
        loggerType = "log4j";
        getL4JLogger = logger;
    }
    public void removeLogger() {
        loggerType = "default";
    }

    /**
     * 消息处理与发送
     * 支持系统流输出/java logger/log4j
     */
    private BufferedWriter systemOut;
    private SimpleDateFormat timeFormat;

    private void sendMessage(String message, LoggerManager.logLevel level) {
        String prefix = "["+level.toString()+"] ";
        String time = "["+timeFormat.format(new Date(System.currentTimeMillis()))+"] ";
        switch (loggerType) {
            case "java":
                switch (level) {
                    case WARN:
                    case ERROR:
                    case EXCEPTION:
                        getJavaLogger.warning(message);
                        break;
                    case INFO:
                    case API:
                    case COMMAND:
                    default:
                        getJavaLogger.info(message);
                        break;
                }
                break;
            case "log4j":
                switch (level) {
                    case COMMAND:
                    case API:
                        getL4JLogger.debug(message);
                        break;
                    case WARN:
                        getL4JLogger.warn(message);
                        break;
                    case ERROR:
                        getL4JLogger.error(message);
                        break;
                    case EXCEPTION:
                        getL4JLogger.fatal(message);
                        break;
                    case INFO:
                    default:
                        getL4JLogger.info(message);
                        break;
                }
                break;
            case "default":
            default:
                fastOut(time+prefix+message);
                break;
        }
        writeMessage(time+prefix+message);
    }

    private void fastOut(String message) {
        try {
            systemOut.write(message);
            systemOut.write("\r\n");
            systemOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 日志写入部分
     * 受上方新建对象给的logSave影响
     * logDir是文件夹！不是文件！该变量为null时不写入日志
     */
    private String logDir;// 如"./logs"
    private String logDate;
    private BufferedWriter writer;
    private static SimpleDateFormat dateFormat;

    private void initWriter() {
        if (logDir == null) return;
        logDate = dateFormat.format(new Date(System.currentTimeMillis()));
        loadWriter();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadWriter() {
        String logFile = logDir+"/"+logDate+".yml";
        try {
            File dir = new File(logDir);
            if (!dir.exists()) dir.mkdir();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile,true), StandardCharsets.UTF_8));
            System.out.println("日志文件保存在："+logFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("无法加载日志模块："+logFile);
        }
    }

    private void checkWriter() {
        String nowDate = dateFormat.format(new Date(System.currentTimeMillis()));
        if (!logDate.equals(nowDate)) {
            logDate = nowDate;
            loadWriter();
        }
    }

    private void writeMessage(String message) {
        if (logDir == null) return;
        if (writer == null) return;
        checkWriter();
        try {
            writer.write(message+"\r\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据无法写入日志！");
        }
    }
}
