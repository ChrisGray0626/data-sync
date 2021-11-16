package pers.chris.core.filter;

import pers.chris.core.common.Unit;

import java.util.Map;

public class ValueFilterUnit implements Unit {

    private final String rule;

    public ValueFilterUnit(String rule) {
        this.rule = rule;
    }

    @Override
    public void run(Map<String, String> data) {
    }

    public String getRule() {
        return rule;
    }

}
