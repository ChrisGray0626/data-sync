package pers.chris.job.base.mapper;

import pers.chris.common.SyncDataSet;
import pers.chris.common.type.FieldTypeEnum;
import pers.chris.job.base.executor.BaseExecutor;

import java.util.Map;

/**
 * 映射器的基类
 */
public abstract class BaseMapper extends BaseExecutor {

    protected SyncDataSet syncDataSet;
    protected Map<String, FieldTypeEnum> readerFields;
    protected Map<String, FieldTypeEnum> writerFields;

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

    public void setFields(Map<String, FieldTypeEnum> readerFields,
                          Map<String, FieldTypeEnum> writerFields) {
        this.readerFields = readerFields;
        this.writerFields = writerFields;
    }
}
