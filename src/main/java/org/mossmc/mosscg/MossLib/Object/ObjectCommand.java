package org.mossmc.mosscg.MossLib.Object;

import java.util.List;

/**
 * 本类为MossLib的指令对象
 * 你可以通过实现本类来对象化一条指令
 */
public abstract class ObjectCommand {
    /**
     * 指令关键词前缀
     * 如设置为restart
     * 则args[0]为restart时，指令传入本类
     */
    public List<String> prefix() {
        return null;
    };

    /**
     * 指令执行方法，触发指令时会调用
     * @return true为执行成功，false为执行失败
     * @param args 指令传入参数
     */
    public boolean execute(String[] args,ObjectLogger logger) {
        return false;
    }
}
