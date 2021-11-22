package pers.chris.job;

import pers.chris.common.SyncDataSet;
import pers.chris.common.model.*;
import pers.chris.common.typeEnum.SyncTypeEnum;
import pers.chris.job.filter.ValueFilterable;
import pers.chris.job.mapper.FieldMappable;
import pers.chris.common.util.TimeUtil;
import pers.chris.job.reader.DBReader;
import pers.chris.job.reader.Readable;
import pers.chris.job.writer.Writeable;

import java.util.List;

public class SyncJob {

    private JobConf jobConf;
    private Readable reader;
    private Writeable writer;
    private FieldMappable mapper;


    // TODO Generator
    public SyncJob (JobConf jobConf, DBConfBO srcDBConf, DBConfBO dstDBConf,
                    List<ValueFilterConfBO> valueFilterConfs, List<FieldMapConfBO> fieldMapConfs) {

    }

    public SyncJob (JobConf jobConf, APIConfBO apiConf, DBConfBO dstDBConf,
                    List<ValueFilterConfBO> valueFilterConfs, List<FieldMapConfBO> fieldMapConfs,
                    PluginConfBO pluginConf) {

    }

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
