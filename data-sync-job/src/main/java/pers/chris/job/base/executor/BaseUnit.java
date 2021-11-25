package pers.chris.job.base.executor;

import java.util.Map;

/**
 * 执行器元件的基类
 */
public abstract class BaseUnit {

    /**
     * 执行方法
     * @param data 数据行，每次仅操作一行数据
     */
    public abstract void run(Map<String, String> data);

}
