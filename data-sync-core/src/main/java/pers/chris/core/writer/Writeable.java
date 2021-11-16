package pers.chris.core.writer;

import pers.chris.core.common.SyncDataSet;
import pers.chris.core.common.typeEnum.FieldTypeEnum;
import pers.chris.core.model.DBConf;

import java.util.Map;

public interface Writeable {

    void init(DBConf dbConf);

    void run(SyncDataSet syncDataSet);

    Map<String, FieldTypeEnum> getFields();

}
