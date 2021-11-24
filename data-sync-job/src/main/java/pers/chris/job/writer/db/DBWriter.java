package pers.chris.job.writer.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.chris.common.model.DBConfBO;
import pers.chris.common.model.DataSourceConf;
import pers.chris.common.type.DataSourceTypeEnum;
import pers.chris.common.util.ConnectUtil;
import pers.chris.common.util.FieldUtil;
import pers.chris.common.util.SQLGenerateUtil;
import pers.chris.job.base.BaseWriter;

import java.sql.*;
import java.util.Map;

public class DBWriter extends BaseWriter {

    private final DBConfBO dbConf;
    private final Connection connection;
    private static final Logger LOGGER = LoggerFactory.getLogger(DBWriter.class);

    public DBWriter (DataSourceConf conf) {
        writerType = DataSourceTypeEnum.DATABASE;
        dbConf = (DBConfBO) conf;
        connection = ConnectUtil.connect(dbConf.dbType, dbConf.getUrl(), dbConf.user, dbConf.password);

        readField();

        console();
    }

    @Override
    protected void run() {
        write();
    }

    @Override
    public void start() {
        run();
    }

    private void write() {
        for (Map<String, String> row: syncDataSet.getRows()) {
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

}
