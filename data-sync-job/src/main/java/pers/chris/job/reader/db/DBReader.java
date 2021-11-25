package pers.chris.job.reader.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.chris.common.model.DBConfBO;
import pers.chris.common.model.DataSourceConf;
import pers.chris.common.type.DataSourceTypeEnum;
import pers.chris.common.util.ConnectUtil;
import pers.chris.common.util.FieldUtil;
import pers.chris.job.base.BaseReader;
import pers.chris.job.base.filter.BaseFilter;
import pers.chris.job.filter.db.DBFilter;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 数据库读取器
 */
public class DBReader extends BaseReader {

    private final DBConfBO dbConf;
    private final DBFilter dbFilter;
    private final Connection connection;
    private static final Logger LOGGER = LoggerFactory.getLogger(DBReader.class);

    public DBReader (DataSourceConf conf, BaseFilter filter) {
        readerType = DataSourceTypeEnum.DATABASE;
        dbConf = (DBConfBO) conf;
        dbFilter = (DBFilter) filter;
        connection = ConnectUtil.connect(dbConf.dbType, dbConf.getUrl(), dbConf.user, dbConf.password);

        readField();

        console();
    }

    @Override
    protected void run() {
        read();
    }

    @Override
    public void start() {
        run();
    }

    /**
     * 数据读取
     * 使用sql的select语句进行数据读取
     */
    private void read() {
        List<Map<String, String>> rows = new LinkedList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from " + dbConf.tableName + dbFilter.run());

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

    /**
     * 字段读取
     * 使用sql的元数据模块 DatabaseMetaData 提前进行字段的读取
     */
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
        LOGGER.info(readerType + ": " + dbConf.toString());
    }

}
