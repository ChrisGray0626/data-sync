package pers.chris.core.model;

import pers.chris.common.model.APIConfBO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "api_conf")
public class APIConfDO {

    @Id
    @Column(name = "api_id")
    public String apiId;
    public String url;
    public String paramJson;
    public String pluginId;

}
