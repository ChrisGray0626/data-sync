package pers.chris.core.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ValueFilterConf {

    @Id
    public String filterId;
    public String jobId;
    public String rule;

}
