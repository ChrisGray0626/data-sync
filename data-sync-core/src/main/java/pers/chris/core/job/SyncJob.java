package pers.chris.core.job;

import pers.chris.core.common.SyncDataSet;
import pers.chris.core.common.typeEnum.SyncTypeEnum;
import pers.chris.core.filter.ValueFilterable;
import pers.chris.core.mapper.FieldMappable;
import pers.chris.core.model.JobConf;
import pers.chris.core.reader.Readable;
import pers.chris.core.util.TimeUtil;
import pers.chris.core.writer.Writeable;

public class SyncJob {

    private JobConf jobConf;
    private Readable reader;
    private Writeable writer;
    private FieldMappable mapper;
    private ValueFilterable filter;

    // TODO Init
    public void init(JobConf jobConf) {
        this.jobConf = jobConf;
    }

    public void run() {
        while (true) {
            SyncDataSet syncDataSet = new SyncDataSet();
            reader.run(syncDataSet);
            mapper.run(syncDataSet, reader.getFields(), writer.getFields());
            writer.run(syncDataSet);
            // 全量同步只进行一次
            if (SyncTypeEnum.TOTAL.equals(jobConf.syncType)) {
                break;
            }

            TimeUtil.sleep(jobConf.syncInterval);
        }
    }

}
