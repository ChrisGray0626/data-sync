package pers.chris.core.service;

import pers.chris.core.common.SyncDataSet;
import pers.chris.core.common.typeEnum.FieldTypeEnum;
import pers.chris.core.model.JobConf;

import java.util.Map;

public interface WriteService {

    void init(JobConf jobConf);

    void run(SyncDataSet syncDataSet);

    public Map<String, FieldTypeEnum> getFields();

}
