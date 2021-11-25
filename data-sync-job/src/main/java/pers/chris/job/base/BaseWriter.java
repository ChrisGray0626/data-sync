package pers.chris.job.base;

import pers.chris.common.SyncDataSet;
import pers.chris.common.type.DataSourceTypeEnum;
import pers.chris.common.type.FieldTypeEnum;
import pers.chris.job.base.executor.BaseExecutor;

import java.util.Map;

/**
 * 写入器的基类
 */
public abstract class BaseWriter extends BaseExecutor {

    protected DataSourceTypeEnum writerType;
    protected SyncDataSet syncDataSet;
    protected Map<String, FieldTypeEnum> fields;

    /**
     * 非公开的执行方法
     */
    protected abstract void run();

    /**
     * 公开的启动方法
     */
    public abstract void start();

    public void setSyncDataSet(SyncDataSet syncDataSet) {
        this.syncDataSet = syncDataSet;
    }

    public Map<String, FieldTypeEnum> getFields() {
        return fields;
    }
}
