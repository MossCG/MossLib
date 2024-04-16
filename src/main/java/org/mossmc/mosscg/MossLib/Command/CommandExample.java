package org.mossmc.mosscg.MossLib.Command;

import org.mossmc.mosscg.MossLib.Object.ObjectCommand;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * 该类为示例
 * 指令怎么写可以参考本类
 */
public class CommandExample extends ObjectCommand {
    /**
     * 可以有多个指令前缀映射到单个指令
     * @return 返回前缀列表
     */
    @Override
    public List<String> prefix() {
        List<String> prefixList = new ArrayList<>();
        prefixList.add("test");
        prefixList.add("example");
        return prefixList;
    }

    /**、
     * 指令执行方法
     * @param args 指令传入参数
     * @return 返回是否执行成功
     */
    @Override
    public boolean execute(String[] args, ObjectLogger logger) {
        if (args[0].equals("test")) {
            logger.sendInfo("test execute!");
            return false;
        } else {
            logger.sendInfo("example execute!");
            return true;
        }
    }
}
