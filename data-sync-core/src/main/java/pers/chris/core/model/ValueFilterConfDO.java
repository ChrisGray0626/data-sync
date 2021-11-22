package pers.chris.core.model;

import pers.chris.common.model.ValueFilterConfBO;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ValueFilterConfDO {

    @Id
    public String filterId;
    public String jobId;
    public String rule;

}
