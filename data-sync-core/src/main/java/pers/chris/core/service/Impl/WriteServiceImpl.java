package pers.chris.core.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import pers.chris.common.SyncDataSet;
import pers.chris.common.typeEnum.FieldTypeEnum;
import pers.chris.core.service.WriteService;
import pers.chris.core.model.DBConfDO;
import pers.chris.core.model.JobConfDO;
import pers.chris.core.dao.DBConfRepo;
import pers.chris.common.util.ConnectUtil;
import pers.chris.common.util.FieldUtil;
import pers.chris.common.util.SQLGenerateUtil;

import java.sql.*;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WriteServiceImpl implements WriteService {

    private DBConfDO dbConf;
    private Map<String, FieldTypeEnum> fields;
    private Connection connection;
    private static final Logger LOGGER = LoggerFactory.getLogger(WriteServiceImpl.class);

    @Autowired
    private DBConfRepo dbConfRepo;


    public void init(JobConfDO jobConf) {
        dbConf = dbConfRepo.findByDbId(jobConf.dstConfId);
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

    private void console() {
        LOGGER.info("WriteServiceConf: " + dbConf.toString());
    }

    public Map<String, FieldTypeEnum> getFields() {
        return fields;
    }

}
