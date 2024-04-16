package org.mossmc.mosscg.MossLib.File;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 依赖检查
 * 使用本类，需要将所有依赖放在jar包的一个文件夹里面
 */
public class FileDependency {
    /**
     * 加载依赖方法
     * 请先使用FileCheck类检查上级文件夹是否存在！
     * @param localDir 依赖外部保存位置（如"./MossFrp/Dependency"）
     * @param packDir 包内依赖保存位置（如"dependency"，即包根目录下的dependency文件夹）
     */
    public static void loadDependencyDir(String localDir,String packDir) {
        try {
            if (!localDir.endsWith("/") && !localDir.endsWith("\\")) localDir+="/";
            if (!packDir.endsWith("/") && !packDir.endsWith("\\")) packDir+="/";
            FileCheck.checkDirExist(localDir);
            URL url = FileDependency.class.getClassLoader().getResource(packDir);
            assert url != null;
            JarURLConnection connection = (JarURLConnection) url.openConnection();
            JarFile jarFile = connection.getJarFile();
            Enumeration<JarEntry> jarEntry = jarFile.entries();
            while (jarEntry.hasMoreElements()) {
                JarEntry entry = jarEntry.nextElement();
                if (entry.getName().startsWith(packDir) && !entry.isDirectory()) {
                    String name = entry.getName().replace(packDir, "");
                    loadDependency(localDir + name, packDir + name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadDependency(String path, String packPath) throws Exception{
        File file = new File(path);
        if (!file.exists()) {
            InputStream input = FileCheck.class.getClassLoader().getResourceAsStream(packPath);
            assert input != null;
            Files.copy(input, file.toPath());
        }
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURL.setAccessible(true);
        addURL.invoke(ClassLoader.getSystemClassLoader(), file.toURI().toURL());
    }
}
