package pers.chris.job.filter;

import pers.chris.common.model.FilterConfBO;
import pers.chris.common.model.JobConfBO;
import pers.chris.job.common.BaseExecutor;

import java.util.List;

public abstract class BaseFilter extends BaseExecutor {

    public abstract void init(JobConfBO jobConf, List<FilterConfBO> filterConfs);

    public abstract String run();

}
