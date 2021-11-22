package pers.chris.common.model;


import pers.chris.common.util.ConnectUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

public class DBConfBO {

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
                "dbType='" + dbType + '\'' +
                ", hostname='" + hostname + '\'' +
                ", port='" + port + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", dbName='" + dbName + '\'' +
                ", tableName='" + tableName + '\'' +
                '}';
    }

}
