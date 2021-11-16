package pers.chris.core.service;

import pers.chris.core.model.JobConf;

public interface ValueFilterService {

    void init(JobConf jobConf);

    String run();

}
