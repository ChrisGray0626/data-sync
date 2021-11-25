package pers.chris.job.base;

import pers.chris.common.SyncDataSet;
import pers.chris.common.type.DataSourceTypeEnum;
import pers.chris.common.type.FieldTypeEnum;
import pers.chris.job.base.executor.BaseExecutor;

import java.util.Map;

/**
 * 读取器的基类
 */
public abstract class BaseReader extends BaseExecutor {

    protected DataSourceTypeEnum readerType;
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
