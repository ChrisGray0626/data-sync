package pers.chris.core.model;


import pers.chris.core.util.ConnectUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "db_conf")
public class DBConf {

    @Id
    public String dbId;
    public String dbType;
    public String hostname;
    public String port;
    public String user;
    public String password;
    public String dbName;
    public String tableName;

    public String getUrl() {
        return ConnectUtil.getUrl(dbType, hostname, port, dbName);
    }

    @Override
    public String toString() {
        return "DBConf{" +
                "dbId='" + dbId + '\'' +
                ", dbType='" + dbType + '\'' +
                ", hostname='" + hostname + '\'' +
                ", port='" + port + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", dbName='" + dbName + '\'' +
                ", tableName='" + tableName + '\'' +
                '}';
    }

}
