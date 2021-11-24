package pers.chris.server.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import pers.chris.common.typeEnum.DataSourceTypeEnum;
import pers.chris.common.typeEnum.SyncTypeEnum;
import pers.chris.server.model.FilterConfDO;
import pers.chris.server.filter.ValueFilterUnit;
import pers.chris.server.service.ValueFilterService;
import pers.chris.server.model.JobConfDO;
import pers.chris.server.dao.FilterConfRepo;
import pers.chris.common.util.TimeUtil;

import java.util.LinkedList;
import java.util.List;

@Service(DataSourceTypeEnum.DATABASE + "ValueFilter")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DBValueFilterServiceImpl implements ValueFilterService {

    private List<ValueFilterUnit> valueFilterUnits;
    private String syncType;
    private Integer syncInterval;
    private String syncFieldName;
    private static final Logger LOGGER = LoggerFactory.getLogger(DBValueFilterServiceImpl.class);

    @Autowired
    private FilterConfRepo filterConfRepo;

    @Override
    public void init(JobConfDO jobConf) {
        valueFilterUnits = new LinkedList<>();
        List<FilterConfDO> valueFilterConfs = filterConfRepo.findByJobId(jobConf.jobId);

        for (FilterConfDO valueFilterConf : valueFilterConfs) {
            ValueFilterUnit valueFilterUnit = new ValueFilterUnit(valueFilterConf.rule);
            valueFilterUnits.add(valueFilterUnit);
        }

        // 增量同步时需要有同步间隔、同步字段
        syncType = jobConf.syncType;
        if (SyncTypeEnum.INCREMENTAL.equals(syncType)) {
            syncInterval = jobConf.syncInterval;
            syncFieldName = jobConf.syncFieldName;
        }
    }

    @Override
    public String run() {
        return parseRule();
    }

    private String parseRule() {
        StringBuilder sql = new StringBuilder();

        sql.append(" where ");
        // 增量同步时，需要有第0条规则,时间过滤
        if (syncType.equals(SyncTypeEnum.INCREMENTAL)) {
            sql.append(timedFilterRule())
                    .append(" and ");
        }

        for (ValueFilterUnit valueFilterUnit : valueFilterUnits) {
            sql.append(valueFilterUnit.getRule())
                    .append(" and ");

        }
        // 删除最后一个" and "
        return sql.toString().replaceAll("( and )$", "");
    }

    // 时间过滤
    private String timedFilterRule() {
        return syncFieldName
                + ">='" + TimeUtil.intervalTime(syncInterval) + "'";
    }

}
