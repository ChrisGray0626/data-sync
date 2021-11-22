package pers.chris.core.model;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class JobConfDO {

    @Id
    public String jobId;
    public String srcType;
    public String srcConfId;
    public String dstType;
    public String dstConfId;
    public String syncType;
    public Integer syncInterval;
    public String syncFieldName;

}