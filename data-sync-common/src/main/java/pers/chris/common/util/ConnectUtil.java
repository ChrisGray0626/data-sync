package pers.chris.common.util;

import pers.chris.common.type.DBTypeEnum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConnectUtil {

    private ConnectUtil() {}

    private static final Map<DBTypeEnum, String> driverNameMap;

    static {
        driverNameMap = new HashMap<>();
        driverNameMap.put(DBTypeEnum.MYSQL, "com.mysql.cj.jdbc.Driver");
        driverNameMap.put(DBTypeEnum.POSTGRESQL, "org.postgresql.Driver");
        driverNameMap.put(DBTypeEnum.SQLSERVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
    }

    public static synchronized Connection connect(String dbType, String url, String user, String password) {
        String driverName = driverNameMap.get(DBTypeEnum.valueOf(dbType));
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static String getUrl(String dbType, String hostname, String port, String dbName) {
        return "jdbc:" + dbType.toLowerCase() + "://" + hostname + ":" + port + "/" + dbName;
    }

}
