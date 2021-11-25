package pers.chris.job.filter.db;

import pers.chris.common.model.FilterConfBO;
import pers.chris.common.model.JobConfBO;
import pers.chris.common.type.SyncTypeEnum;
import pers.chris.common.util.TimeUtil;
import pers.chris.job.base.filter.BaseFilter;

import java.util.LinkedList;
import java.util.List;

/**
 * 数据库过滤器
 */
public class DBFilter extends BaseFilter {

    private final List<DBFilterUnit> filterUnits;

    public DBFilter (JobConfBO jobConf, List<FilterConfBO> filterConfs) {
        this.jobConf = jobConf;
        filterUnits = new LinkedList<>();

        for (FilterConfBO filterConf : filterConfs) {
            DBFilterUnit filterUnit = new DBFilterUnit(filterConf.rule);
            filterUnits.add(filterUnit);
        }
    }

    public String run() {
        return parseRule();
    }

    /**
     * 规则解析
     * 解析为sql语句
     * @return statement sql语句
     */
    private String parseRule() {
        StringBuilder sql = new StringBuilder();

        sql.append(" where ");
        // 增量同步时，需要有第0条规则——时间过滤
        if (SyncTypeEnum.INCREMENTAL.equals(SyncTypeEnum.valueOf(jobConf.syncType))) {
            sql.append(timedFilterRule())
                    .append(" and ");
        }

        for (DBFilterUnit filterUnit : filterUnits) {
            sql.append(filterUnit.getRule())
                    .append(" and ");
        }
        // 删除最后一个" and "
        return sql.toString().replaceAll("( and )$", "");
    }

    /**
     * 时间过滤
     * 数据库的时间过滤一般以sql的where语句的形式实现
     * @return statement sql语句
     */
    private String timedFilterRule() {
        return jobConf.syncFieldName
                + ">='" + TimeUtil.intervalTime(jobConf.syncInterval) + "'";
    }

}
