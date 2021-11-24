package pers.chris.job;

import pers.chris.common.SyncDataSet;
import pers.chris.common.exception.ExecutorNotFoundException;
import pers.chris.common.model.*;
import pers.chris.common.type.DataSourceTypeEnum;
import pers.chris.common.type.ExecutorTypeEnum;
import pers.chris.common.type.SyncTypeEnum;
import pers.chris.common.util.TimeUtil;
import pers.chris.job.filter.api.APIFilter;
import pers.chris.job.filter.db.DBFilter;
import pers.chris.job.base.mapper.BaseMapper;
import pers.chris.job.mapper.Mapper;
import pers.chris.job.base.BaseReader;
import pers.chris.job.reader.api.APIReader;
import pers.chris.job.reader.db.DBReader;
import pers.chris.job.base.BaseWriter;
import pers.chris.job.writer.db.DBWriter;

import java.util.List;

public class SyncJob {

    private JobConfBO jobConf;
    private final BaseReader reader;
    private final BaseMapper mapper;
    private final BaseWriter writer;

    public SyncJob (JobConfBO jobConf,
                    DataSourceConf srcConf, DataSourceConf dstConf,
                    List<FilterConfBO> filterConfs, List<MapperConfBO> mapperConfs)
            throws ExecutorNotFoundException{
        this.jobConf = jobConf;

        switch (jobConf.srcType) {
            case DataSourceTypeEnum.DATABASE:
                reader = new DBReader(srcConf, new DBFilter(jobConf, filterConfs));
                break;
            case DataSourceTypeEnum.API:
                reader = new APIReader(srcConf, new APIFilter(jobConf, filterConfs));
                break;
            default:
                throw new ExecutorNotFoundException(ExecutorTypeEnum.Reader);
        }

        mapper = new Mapper(mapperConfs);

        switch (jobConf.dstType) {
            case DataSourceTypeEnum.DATABASE:
                writer = new DBWriter(dstConf);
                break;
            default:
                throw new ExecutorNotFoundException(ExecutorTypeEnum.Writer);
        }
    }

    @Deprecated
    public void init(JobConfBO jobConf) {
        this.jobConf = jobConf;
    }

    private void run() {
        while (true) {
            SyncDataSet syncDataSet = new SyncDataSet();
            reader.setSyncDataSet(syncDataSet);
            mapper.setSyncDataSet(syncDataSet);
            writer.setSyncDataSet(syncDataSet);

            reader.start();
            mapper.setFields(reader.getFields(), writer.getFields());
            mapper.start();
            writer.start();
            // 全量同步只进行一次
            if (SyncTypeEnum.TOTAL.equals(jobConf.syncType)) {
                break;
            }

            TimeUtil.sleep(jobConf.syncInterval);
        }
    }

    public void start() {
        run();
    }

}
