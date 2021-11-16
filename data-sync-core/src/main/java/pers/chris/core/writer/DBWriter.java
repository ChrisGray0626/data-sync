package pers.chris.core.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.chris.core.common.SyncDataSet;
import pers.chris.core.common.typeEnum.FieldTypeEnum;
import pers.chris.core.model.DBConf;
import pers.chris.core.service.Impl.WriteServiceImpl;
import pers.chris.core.util.ConnectUtil;
import pers.chris.core.util.FieldUtil;
import pers.chris.core.util.SQLGenerateUtil;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class DBWriter implements Writeable{

    private DBConf dbConf;
    private Map<String, FieldTypeEnum> fields;
    private Connection connection;
    private static final Logger LOGGER = LoggerFactory.getLogger(DBWriter.class);

    @Override
    public void init(DBConf dbConf) {
        this.dbConf = dbConf;
        connection = ConnectUtil.connect(dbConf.dbType, dbConf.getUrl(), dbConf.user, dbConf.password);

        readField();

        console();
    }

    @Override
    public void run(SyncDataSet syncDataSet) {
        List<Map<String, String>> rows = syncDataSet.getRows();

        for (Map<String, String> row: rows) {
            String SQL = SQLGenerateUtil.insertSQL(dbConf.tableName, row);

            try {
                Statement statement = connection.createStatement();
                LOGGER.info(SQL);
                statement.execute(SQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
        LOGGER.info("WriterConf: " + dbConf.toString());
    }

    @Override
    public Map<String, FieldTypeEnum> getFields() {
        return fields;
    }

}
