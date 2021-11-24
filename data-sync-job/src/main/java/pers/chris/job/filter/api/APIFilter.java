package pers.chris.job.filter.api;

import pers.chris.common.type.SyncTypeEnum;
import pers.chris.common.model.JobConfBO;
import pers.chris.common.model.FilterConfBO;
import pers.chris.common.util.TimeUtil;
import pers.chris.job.base.filter.BaseFilter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class APIFilter extends BaseFilter {

    public APIFilter (JobConfBO jobConf, List<FilterConfBO> filterConfs) {
        this.jobConf = jobConf;
    }

    @Override
    public String run() {
        if (SyncTypeEnum.INCREMENTAL.equals(jobConf.syncType)) {
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
                + jobConf.syncFieldName + "\": "
                + "\"["
                + TimeUtil.intervalTime(jobConf.syncInterval) + "," + nowTime
                + "]\""
                + "}";
    }

}
