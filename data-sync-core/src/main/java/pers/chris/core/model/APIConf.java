package pers.chris.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "api_conf")
public class APIConf {

    @Id
    @Column(name = "api_id")
    public String APIId;
    public String url;
    public String paramJson;
    public String pluginId;

    @Override
    public String toString() {
        return "InterfaceConf{" +
                "interfaceId='" + APIId + '\'' +
                ", url='" + url + '\'' +
                ", paramJson='" + paramJson + '\'' +
                ", pluginId='" + pluginId + '\'' +
                '}';
    }
}
