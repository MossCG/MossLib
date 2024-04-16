package org.mossmc.mosscg.MossLib.Config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.mossmc.mosscg.MossLib.File.FileCheck;
import org.mossmc.mosscg.MossLib.Object.ObjectConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * 配置文件管理类
 * 实现配置文件的读写之类的
 */
public class ConfigManager {
    /**
     * 配置文件类型
     */
    public enum configType {
        JSON,YAML
    }

    /**
     * 加载配置文件
     * 检查配置文件，不存在则导出
     * @param localDir 本地配置文件的存储文件夹，如"./MossLib/configs"
     * @param filename 配置文件名称，如"config.yml"
     * @param packPath 配置文件在包里的路径，如"configs/config.yml"
     * 返回配置文件对象
     */
    @SuppressWarnings({"ReassignedVariable", "DataFlowIssue"})
    public static ObjectConfig getConfigObject(String localDir, String filename, String packPath) {
        FileCheck.checkDirExist(localDir);
        FileCheck.checkFileExist(localDir+"/"+filename,packPath);
        if (packPath!=null) FileCheck.checkFileExist(localDir+"/"+filename, packPath);
        configType type = configType.YAML;
        if (filename.endsWith(".yml")) type = configType.YAML;
        if (filename.endsWith(".json")) type = configType.JSON;
        ObjectConfig config = null;
        if (type.equals(configType.YAML)) config = new ObjectConfig(loadYamlConfig(localDir, filename));
        if (type.equals(configType.JSON)) config = new ObjectConfig(loadJsonConfig(localDir, filename));
        return config;
    }

    /**
     * 加载yaml格式配置文件
     */
    @SuppressWarnings("unchecked")
    private static Map<String,Object> loadYamlConfig(String localDir, String filename) {
        try {
            Yaml yaml = new Yaml();
            FileInputStream input = new FileInputStream(localDir+"/"+filename);
            return yaml.loadAs(input, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载json格式配置文件
     */
    private static JSONObject loadJsonConfig(String localDir, String filename) {
        try {
            Path path = (new File(localDir+"/"+filename)).toPath();
            BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String read;
            while ((read = reader.readLine())!= null) builder.append(read);
            return JSONObject.parseObject(builder.toString(), Feature.OrderedField);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
