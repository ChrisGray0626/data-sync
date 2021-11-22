package pers.chris.job.filter;

import pers.chris.common.model.JobConf;
import pers.chris.common.model.ValueFilterConfBO;

import java.util.List;

public interface ValueFilterable {

    void init(JobConf jobConf, List<ValueFilterConfBO> valueFilterConfs);

    String run();

}
