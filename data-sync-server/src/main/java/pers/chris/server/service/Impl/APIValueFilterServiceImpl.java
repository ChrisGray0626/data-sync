package pers.chris.server.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import pers.chris.common.typeEnum.DataSourceTypeEnum;
import pers.chris.common.typeEnum.SyncTypeEnum;
import pers.chris.server.service.ValueFilterService;
import pers.chris.server.model.JobConfDO;
import pers.chris.common.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service(DataSourceTypeEnum.API + "ValueFilter")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class APIValueFilterServiceImpl implements ValueFilterService {

    private String syncType;
    private Integer syncInterval;
    private String syncFieldName;
    private static final Logger LOGGER = LoggerFactory.getLogger(APIValueFilterServiceImpl.class);

    @Override
    public void init(JobConfDO jobConf) {
        syncType = jobConf.syncType;
        // 增量同步时需要有同步间隔、同步字段
        if (SyncTypeEnum.INCREMENTAL.equals(syncType)) {
            syncInterval = jobConf.syncInterval;
            syncFieldName = jobConf.syncFieldName;
        }
    }

    @Override
    public String run() {
        if (SyncTypeEnum.INCREMENTAL.equals(syncType)) {
            return timedFilterParam();
        } else {
            return "";
        }
    }

    // 时间过滤
    private String timedFilterParam() {
        Calendar calendar = Calendar.getInstance();
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String nowTime = simpleDateFormat.format(calendar.getTime());
        return "{\""
                + syncFieldName + "\": "
                + "\"["
                + TimeUtil.intervalTime(syncInterval) + "," + nowTime
                + "]\""
                + "}";
    }

}
