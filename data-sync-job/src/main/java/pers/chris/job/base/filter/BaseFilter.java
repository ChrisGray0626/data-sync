package pers.chris.job.base.filter;

import pers.chris.common.model.JobConfBO;
import pers.chris.job.base.executor.BaseExecutor;

/**
 * 过滤器的基类
 */
public abstract class BaseFilter extends BaseExecutor {

    protected JobConfBO jobConf;

    /**
     * 执行方法，常规的过滤方法：生成用于拼接的语句
     * @return statement 过滤语句
     */
    public abstract String run();

}
