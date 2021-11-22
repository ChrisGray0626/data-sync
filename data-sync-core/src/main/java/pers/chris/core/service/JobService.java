package pers.chris.core.service;

import pers.chris.core.model.JobConfDO;

public interface JobService {

    void init(JobConfDO jobConf);

    void run();

}
