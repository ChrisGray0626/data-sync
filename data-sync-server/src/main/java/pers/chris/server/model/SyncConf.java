package pers.chris.server.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SyncConf {

    @Id
    public String syncId;
    public String syncType;
    public Integer interval;
    public String timeFieldName; // 记录时间的字段名

}


