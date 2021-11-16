package pers.chris.core.filter;

import pers.chris.core.model.JobConf;
import pers.chris.core.model.ValueFilterConf;

import java.util.List;

public interface ValueFilterable {

    void init(JobConf jobConf, List<ValueFilterConf> valueFilterConfs);

    String run();

}
