package pers.chris.job.base.mapper;

import pers.chris.job.base.executor.BaseUnit;

import java.util.List;

/**
 * 映射器元件的基类
 */
public abstract class BaseMapperUnit extends BaseUnit {

    // 映射规则
    protected String rule;
    // 源字段
    protected List<String> srcFieldNames;
    // 目标字段
    protected List<String> dstFieldNames;

}
