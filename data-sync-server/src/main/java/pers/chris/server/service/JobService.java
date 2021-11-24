package pers.chris.server.service;

import pers.chris.server.model.JobConfDO;

public interface JobService {

    void init(JobConfDO jobConfDO);

    void start();

}
