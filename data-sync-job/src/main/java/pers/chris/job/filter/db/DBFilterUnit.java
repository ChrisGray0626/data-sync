package pers.chris.job.filter.db;


import pers.chris.job.base.filter.BaseFilterUnit;

import java.util.Map;

/**
 * 数据库过滤器的基类
 */
public class DBFilterUnit extends BaseFilterUnit {

    public DBFilterUnit(String rule) {
        this.rule = rule;
    }

    @Override
    public void run(Map<String, String> data) {
    }

    public String getRule() {
        return rule;
    }

}
