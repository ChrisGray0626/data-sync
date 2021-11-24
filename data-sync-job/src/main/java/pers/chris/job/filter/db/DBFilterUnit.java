package pers.chris.job.filter.db;


import pers.chris.job.filter.BaseFilterUnit;

import java.util.Map;

public class DBFilterUnit extends BaseFilterUnit {

    private final String rule;

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
