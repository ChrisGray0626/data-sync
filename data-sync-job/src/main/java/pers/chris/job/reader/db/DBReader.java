package pers.chris.job.reader.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.chris.common.SyncDataSet;
import pers.chris.common.model.DataSourceConf;
import pers.chris.common.typeEnum.DataSourceTypeEnum;
import pers.chris.common.typeEnum.FieldTypeEnum;
import pers.chris.job.filter.BaseFilter;
import pers.chris.job.filter.db.DBFilter;
import pers.chris.common.model.DBConfBO;
import pers.chris.common.util.ConnectUtil;
import pers.chris.common.util.FieldUtil;
import pers.chris.job.reader.BaseReader;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DBReader extends BaseReader {

    private DBConfBO dbConf;
    private DBFilter dbFilter;
    private Connection connection;
    private static final Logger LOGGER = LoggerFactory.getLogger(DBReader.class);

    public DBReader (DataSourceConf conf, BaseFilter filter) {
        readerType = DataSourceTypeEnum.DATABASE;
        dbConf = (DBConfBO) conf;
        dbFilter = (DBFilter) filter;
        connection = ConnectUtil.connect(dbConf.dbType, dbConf.getUrl(), dbConf.user, dbConf.password);

        readField();

        console();
    }

    public void init(DBConfBO dbConf, DBFilter dbFilter) {
        this.dbConf = dbConf;
        this.dbFilter = dbFilter;
        connection = ConnectUtil.connect(dbConf.dbType, dbConf.getUrl(), dbConf.user, dbConf.password);

        console();

        readField();
    }

    @Override
    public void run(SyncDataSet syncDataSet) {
        read();
    }

    @Override
    protected void run() {
        read();
    }

    @Override
    public void start() {
        run();
    }

    private void read() {
        List<Map<String, String>> rows = new LinkedList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from " + dbConf.tableName
                            + dbFilter.run());

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

    private void console() {
        LOGGER.info("ReaderConf: " + dbConf.toString());
    }

    public Map<String, FieldTypeEnum> getFields() {
        return fields;
    }

}
