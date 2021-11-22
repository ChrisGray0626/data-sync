package pers.chris.job.reader;

import pers.chris.common.SyncDataSet;
import pers.chris.common.typeEnum.FieldTypeEnum;

import java.util.Map;

public interface Readable {

    void run(SyncDataSet syncDataSet);

    Map<String, FieldTypeEnum> getFields();

}
