package pers.chris.core.service;

import pers.chris.core.model.JobConfDO;

public interface ValueFilterService {

    void init(JobConfDO jobConf);

    String run();

}
