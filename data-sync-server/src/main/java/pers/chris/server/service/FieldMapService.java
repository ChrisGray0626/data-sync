package pers.chris.server.service;

import pers.chris.common.SyncDataSet;
import pers.chris.common.typeEnum.FieldTypeEnum;
import pers.chris.server.model.JobConfDO;

import java.util.Map;

public interface FieldMapService {

    void init(JobConfDO jobConf);

    void run(SyncDataSet syncDataSet, Map<String, FieldTypeEnum> readFields, Map<String, FieldTypeEnum> writeFields);

}
