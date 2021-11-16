package pers.chris.core.mapper;

import pers.chris.core.common.SyncDataSet;
import pers.chris.core.common.typeEnum.FieldTypeEnum;
import pers.chris.core.model.FieldMapConf;

import java.util.List;
import java.util.Map;

public interface FieldMappable {

    void init(List<FieldMapConf> fieldMapConfs);

    void run(SyncDataSet syncDataSet,
             Map<String, FieldTypeEnum> readFields,
             Map<String, FieldTypeEnum> writeFields);

}
