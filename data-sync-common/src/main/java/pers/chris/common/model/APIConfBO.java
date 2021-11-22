package pers.chris.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

public class APIConfBO {

    public String url;
    public String paramJson;
    public String pluginId;

    @Override
    public String toString() {
        return "APIConf{" +
                "url='" + url + '\'' +
                ", paramJson='" + paramJson + '\'' +
                ", pluginId='" + pluginId + '\'' +
                '}';
    }
}
