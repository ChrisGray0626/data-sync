package pers.chris.job.filter;

import pers.chris.common.typeEnum.SyncTypeEnum;
import pers.chris.common.model.JobConf;
import pers.chris.common.model.ValueFilterConfBO;
import pers.chris.common.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class APIFilter implements ValueFilterable {

    private JobConf jobConf;

    public APIFilter (JobConf jobConf, List<ValueFilterConfBO> valueFilterConfs) {

    }

    @Override
    public void init(JobConf jobConf, List<ValueFilterConfBO> valueFilterConfs) {
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
