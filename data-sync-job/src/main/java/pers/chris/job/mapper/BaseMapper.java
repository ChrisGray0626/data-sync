package pers.chris.job.mapper;

import pers.chris.common.SyncDataSet;
import pers.chris.common.model.MapperConfBO;
import pers.chris.common.typeEnum.FieldTypeEnum;
import pers.chris.job.common.BaseExecutor;

import java.util.List;
import java.util.Map;

public abstract class BaseMapper extends BaseExecutor {

    protected SyncDataSet syncDataSet;
    protected Map<String, FieldTypeEnum> readerFields;
    protected Map<String, FieldTypeEnum> writerFields;


    public abstract void init(List<MapperConfBO> fieldMapConfs);

    public abstract void run(SyncDataSet syncDataSet,
                             Map<String, FieldTypeEnum> readFields,
                             Map<String, FieldTypeEnum> writeFields);

    protected abstract void run();

    public abstract void start();

    public void setSyncDataSet(SyncDataSet syncDataSet) {
        this.syncDataSet = syncDataSet;
    }

    public void setFields(Map<String, FieldTypeEnum> readerFields,
                          Map<String, FieldTypeEnum> writerFields) {
        this.readerFields = readerFields;
        this.writerFields = writerFields;
    }
}
