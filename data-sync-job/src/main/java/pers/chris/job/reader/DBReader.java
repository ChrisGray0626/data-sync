package pers.chris.job.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.chris.common.SyncDataSet;
import pers.chris.common.model.ValueFilterConfBO;
import pers.chris.common.typeEnum.FieldTypeEnum;
import pers.chris.job.filter.DBFilter;
import pers.chris.common.model.DBConfBO;
import pers.chris.common.util.ConnectUtil;
import pers.chris.common.util.FieldUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DBReader implements Readable, DBReadable {

    private DBConfBO dbConf;
    private DBFilter filter;
    private Map<String, FieldTypeEnum> fields;
    private Connection connection;
    private static final Logger LOGGER = LoggerFactory.getLogger(DBReader.class);

    public DBReader (DBConfBO dbConf, List<ValueFilterConfBO> valueFilterConfs) {

    }

    @Override
    public void init(DBConfBO dbConf, DBFilter dbFilter) {
        this.dbConf = dbConf;
        this.filter = dbFilter;
        connection = ConnectUtil.connect(dbConf.dbType, dbConf.getUrl(), dbConf.user, dbConf.password);

        console();

        readField();
    }

    @Override
    public void run(SyncDataSet syncDataSet) {
        List<Map<String, String>> rows = read();
        syncDataSet.setRows(rows);
    }

    private void readField() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, "%", dbConf.tableName, "%");
            fields = FieldUtil.read(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, String>> read() {
        List<Map<String, String>> rows = new LinkedList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from " + dbConf.tableName
                            + filter.run());

            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                // 根据字段名称获取对应数据
                for (String fieldName : fields.keySet()) {
                    row.put(fieldName, resultSet.getString(fieldName));
                }

                rows.add(row);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    private void console() {
        LOGGER.info("ReaderConf: " + dbConf.toString());
    }

    public Map<String, FieldTypeEnum> getFields() {
        return fields;
    }

}
