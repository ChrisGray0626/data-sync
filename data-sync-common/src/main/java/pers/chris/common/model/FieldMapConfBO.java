package pers.chris.common.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FieldMapConfBO {

    @Id
    public String mapId;
    public String jobId;
    public String rule;

}
