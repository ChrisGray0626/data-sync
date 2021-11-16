package pers.chris.core.service;

import pers.chris.core.model.JobConf;

public interface JobService {

    void init(JobConf jobConf);

    void run();

}
