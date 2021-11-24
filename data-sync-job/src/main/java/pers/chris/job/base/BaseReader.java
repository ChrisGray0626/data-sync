package pers.chris.job.base;

import pers.chris.common.SyncDataSet;
import pers.chris.common.type.FieldTypeEnum;
import pers.chris.job.base.executor.BaseExecutor;

import java.util.Map;

public abstract class BaseReader extends BaseExecutor {

    protected String readerType;
    protected SyncDataSet syncDataSet;
    protected Map<String, FieldTypeEnum> fields;

    protected abstract void run();

    public abstract void start();

    public void setSyncDataSet(SyncDataSet syncDataSet) {
        this.syncDataSet = syncDataSet;
    }

    public Map<String, FieldTypeEnum> getFields() {
        return fields;
    }

}
