package org.mossmc.mosscg.MossLib.File;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * 文件基础检查类
 * 提供一些常用的文件检查方法
 */
public class FileCheck {
    /**
     * 检查文件夹是否存在
     * true则存在/已新建
     * false为新建失败
     */
    public static boolean checkDirExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return file.mkdir();
        }
        return true;
    }

    /**
     * 检查文件是否存在
     * @param path 文件目标路径，参考"./MossFrp/logs.txt"
     * @param packPath 文件在jar包内的路径，参考"MossFrp/logs.txt"
     * true则存在/已新建
     * false为新建失败
     */
    public static boolean checkFileExist(String path,String packPath) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                InputStream input = FileCheck.class.getClassLoader().getResourceAsStream(packPath);
                assert input != null;
                Files.copy(input, file.toPath());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
