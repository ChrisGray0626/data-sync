package pers.chris.common.model;

import javax.persistence.Entity;
import javax.persistence.Id;

public class MapperConfBO {

    public String mapperId;
    public String jobId;
    public String rule;

    public String getMapperId() {
        return mapperId;
    }

    public void setMapperId(String mapperId) {
        this.mapperId = mapperId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
