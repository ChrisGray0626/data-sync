package pers.chris.job.mapper;

import pers.chris.common.SyncDataSet;
import pers.chris.common.typeEnum.FieldTypeEnum;
import pers.chris.common.model.FieldMapConfBO;

import java.util.List;
import java.util.Map;

public interface FieldMappable {

    void init(List<FieldMapConfBO> fieldMapConfs);

    void run(SyncDataSet syncDataSet,
             Map<String, FieldTypeEnum> readFields,
             Map<String, FieldTypeEnum> writeFields);

}
