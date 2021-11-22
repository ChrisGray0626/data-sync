package pers.chris.job.writer;

import pers.chris.common.SyncDataSet;
import pers.chris.common.typeEnum.FieldTypeEnum;
import pers.chris.common.model.DBConfBO;

import java.util.Map;

public interface Writeable {

    void init(DBConfBO dbConf);

    void run(SyncDataSet syncDataSet);

    Map<String, FieldTypeEnum> getFields();

}
