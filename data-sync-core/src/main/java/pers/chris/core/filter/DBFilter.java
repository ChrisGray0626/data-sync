package pers.chris.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.chris.core.common.typeEnum.SyncTypeEnum;
import pers.chris.core.model.JobConf;
import pers.chris.core.model.ValueFilterConf;
import pers.chris.core.util.TimeUtil;

import java.util.LinkedList;
import java.util.List;

public class DBFilter implements ValueFilterable {

    private JobConf jobConf;
    private List<ValueFilterUnit> valueFilterUnits;
    private static final Logger LOGGER = LoggerFactory.getLogger(DBFilter.class);

    public void init(JobConf jobConf, List<ValueFilterConf> valueFilterConfs) {
        this.jobConf = jobConf;
        valueFilterUnits = new LinkedList<>();

        for (ValueFilterConf valueFilterConf : valueFilterConfs) {
            ValueFilterUnit valueFilterUnit = new ValueFilterUnit(valueFilterConf.rule);
            valueFilterUnits.add(valueFilterUnit);
        }
    }

    public String run() {
        return parseRule();
    }

    private String parseRule() {
        StringBuilder sql = new StringBuilder();

        sql.append(" where ");
        // 增量同步时，需要有第0条规则,时间过滤
        if (jobConf.syncType.equals(SyncTypeEnum.INCREMENTAL)) {
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
        return jobConf.syncFieldName
                + ">='" + TimeUtil.intervalTime(jobConf.syncInterval) + "'";
    }

}
