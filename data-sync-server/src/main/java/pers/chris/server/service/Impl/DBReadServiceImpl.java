package pers.chris.server.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import pers.chris.common.SyncDataSet;
import pers.chris.common.typeEnum.DataSourceTypeEnum;
import pers.chris.common.typeEnum.FieldTypeEnum;
import pers.chris.server.service.ReadService;
import pers.chris.server.service.ValueFilterService;
import pers.chris.server.model.DBConfDO;
import pers.chris.server.model.JobConfDO;
import pers.chris.server.dao.DBConfRepo;
import pers.chris.common.util.ConnectUtil;
import pers.chris.common.util.FieldUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service(DataSourceTypeEnum.DATABASE)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DBReadServiceImpl implements ReadService {

    private DBConfDO dbConf;
    private Map<String, FieldTypeEnum> fields;
    private Connection connection;
    private static final Logger LOGGER = LoggerFactory.getLogger(DBReadServiceImpl.class);

    @Autowired
    private DBConfRepo dbConfRepo;
    @Autowired
    @Qualifier(DataSourceTypeEnum.DATABASE + "ValueFilter")
    private ValueFilterService valueFilterService;

    @Override
    public void init(JobConfDO jobConf) {
        valueFilterService.init(jobConf);
        dbConf = dbConfRepo.findByDbId(jobConf.srcConfId);
        connection = ConnectUtil.connect(dbConf.dbType, dbConf.getUrl(), dbConf.user, dbConf.password);

        readField();

        console();
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

    @Override
    public void run(SyncDataSet syncDataSet) {
        List<Map<String, String>> rows = read();
        syncDataSet.setRows(rows);
    }

    private List<Map<String, String>> read() {
        List<Map<String, String>> rows = new LinkedList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from " + dbConf.tableName
                            + valueFilterService.run());

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
            LOGGER.error(String.valueOf(e));
        }
        return rows;
    }

    private void console() {
        LOGGER.info("ReadServiceConf: " + dbConf.toString());
    }

    public Map<String, FieldTypeEnum> getFields() {
        return fields;
    }

}
