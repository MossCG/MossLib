package org.mossmc.mosscg.MossLib.Object;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.mossmc.mosscg.MossLib.Config.ConfigManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 配置文件对象
 * 包含常用的配置文件操作
 */
public class ObjectConfig {
    /**
     * 传入json或map<String,Object>格式的数据
     * 获取ObjCfg对象
     */
    public ObjectConfig(JSONObject json) {
        jsonConfig = json;
        configType = ConfigManager.configType.JSON;
    }
    public ObjectConfig(Map<String,Object> yaml) {
        yamlConfig = yaml;
        configType = ConfigManager.configType.YAML;
    }

    /**
     * ObjectConfig基础信息
     * 以及包装前数据
     */
    @SuppressWarnings("FieldMayBeFinal")
    private ConfigManager.configType configType;
    private JSONObject jsonConfig;
    private Map<String,Object> yamlConfig;

    /**
     * 获取字符串
     */
    public String getString(String key) {
        if (configType.equals(ConfigManager.configType.JSON)) {
            return jsonConfig.getString(key);
        }
        if (configType.equals(ConfigManager.configType.YAML)) {
            return yamlConfig.get(key).toString();
        }
        return null;
    }

    /**
     * 获取数字
     */
    public Integer getInteger(String key) {
        if (configType.equals(ConfigManager.configType.JSON)) {
            return jsonConfig.getInteger(key);
        }
        if (configType.equals(ConfigManager.configType.YAML)) {
            return Integer.parseInt(yamlConfig.get(key).toString());
        }
        return null;
    }

    /**
     * 获取布尔值
     */
    public Boolean getBoolean(String key) {
        if (configType.equals(ConfigManager.configType.JSON)) {
            return jsonConfig.getBoolean(key);
        }
        if (configType.equals(ConfigManager.configType.YAML)) {
            return Boolean.parseBoolean(yamlConfig.get(key).toString());
        }
        return null;
    }

    /**
     * 获取双精度值
     */
    public Double getDouble(String key) {
        if (configType.equals(ConfigManager.configType.JSON)) {
            return jsonConfig.getDouble(key);
        }
        if (configType.equals(ConfigManager.configType.YAML)) {
            return Double.parseDouble(yamlConfig.get(key).toString());
        }
        return null;
    }

    /**
     * 获取JSON对象
     */
    public JSONObject getJSONObject(String key) {
        if (configType.equals(ConfigManager.configType.JSON)) {
            return jsonConfig.getJSONObject(key);
        }
        if (configType.equals(ConfigManager.configType.YAML)) {
            return JSON.parseObject(yamlConfig.get(key).toString());
        }
        return null;
    }

    /**
     * 获取JSON对象
     */
    public JSONArray getJSONArray(String key) {
        if (configType.equals(ConfigManager.configType.JSON)) {
            return jsonConfig.getJSONArray(key);
        }
        if (configType.equals(ConfigManager.configType.YAML)) {
            return JSON.parseArray(yamlConfig.get(key).toString());
        }
        return null;
    }

    /**
     * 获取字符串List
     * 数据格式应为["aaa","bbb","ccc"]
     */
    public List<String> getStringList(String key) {
        if (configType.equals(ConfigManager.configType.JSON)) {
            String data = jsonConfig.getString(key);
            data = data.substring(1,data.length()-1);
            return new ArrayList<>(Arrays.asList(data.split(",")));
        }
        if (configType.equals(ConfigManager.configType.YAML)) {
            String data = yamlConfig.get(key).toString();
            data = data.substring(1,data.length()-1);
            return new ArrayList<>(Arrays.asList(data.split(",")));
        }
        return null;
    }
}
