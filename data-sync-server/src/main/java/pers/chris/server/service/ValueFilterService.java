package pers.chris.server.service;

import pers.chris.server.model.JobConfDO;

public interface ValueFilterService {

    void init(JobConfDO jobConf);

    String run();

}
