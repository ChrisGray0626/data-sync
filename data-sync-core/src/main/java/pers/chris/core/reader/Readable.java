package pers.chris.core.reader;

import pers.chris.core.common.SyncDataSet;
import pers.chris.core.common.typeEnum.FieldTypeEnum;

import java.util.Map;

public interface Readable {

    void run(SyncDataSet syncDataSet);

    Map<String, FieldTypeEnum> getFields();

}
