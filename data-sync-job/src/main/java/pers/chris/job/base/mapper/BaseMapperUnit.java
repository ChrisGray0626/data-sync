package pers.chris.job.base.mapper;

import pers.chris.job.base.executor.BaseUnit;

import java.util.List;

public abstract class BaseMapperUnit extends BaseUnit {

    protected String rule;
    protected List<String> dstFieldNames; // 目标字段
    protected List<String> srcFieldNames; // 源字段

}
