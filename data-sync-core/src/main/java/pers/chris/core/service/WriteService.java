package pers.chris.core.service;

import pers.chris.common.SyncDataSet;
import pers.chris.common.typeEnum.FieldTypeEnum;
import pers.chris.core.model.JobConfDO;

import java.util.Map;

public interface WriteService {

    void init(JobConfDO jobConf);

    void run(SyncDataSet syncDataSet);

    public Map<String, FieldTypeEnum> getFields();

}
