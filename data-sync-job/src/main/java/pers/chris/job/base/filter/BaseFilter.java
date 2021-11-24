package pers.chris.job.base.filter;

import pers.chris.common.model.JobConfBO;
import pers.chris.job.base.executor.BaseExecutor;


public abstract class BaseFilter extends BaseExecutor {

    protected JobConfBO jobConf;

    public abstract String run();

}
