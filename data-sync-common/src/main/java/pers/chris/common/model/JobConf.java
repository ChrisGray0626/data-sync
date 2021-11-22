package pers.chris.common.model;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class JobConf {

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
