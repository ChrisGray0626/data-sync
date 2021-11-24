package pers.chris.job.writer;

import pers.chris.common.SyncDataSet;
import pers.chris.common.typeEnum.FieldTypeEnum;
import pers.chris.job.common.BaseExecutor;

import java.util.Map;

public abstract class BaseWriter extends BaseExecutor {

    protected String writerType;
    protected SyncDataSet syncDataSet;
    protected Map<String, FieldTypeEnum> fields;

    public abstract void run(SyncDataSet syncDataSet);

    protected abstract void run();

    public abstract void start();

    public void setSyncDataSet(SyncDataSet syncDataSet) {
        this.syncDataSet = syncDataSet;
    }

    public Map<String, FieldTypeEnum> getFields() {
        return fields;
    };
}
