package pers.chris.core.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FieldMapConf {

    @Id
    public String mapId;
    public String jobId;
    public String rule;

}
