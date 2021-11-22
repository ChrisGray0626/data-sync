package pers.chris.core.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pers.chris.common.SyncDataSet;
import pers.chris.common.typeEnum.SyncTypeEnum;
import pers.chris.core.factory.ReadServiceFactory;
import pers.chris.core.service.FieldMapService;
import pers.chris.core.service.JobService;
import pers.chris.core.service.ReadService;
import pers.chris.core.service.WriteService;
import pers.chris.core.model.JobConfDO;
import pers.chris.common.util.TimeUtil;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JobServiceImpl implements JobService {

    private JobConfDO jobConf;
    private ReadService readService;
    @Autowired
    private ReadServiceFactory readServiceFactory;
    @Autowired
    private WriteService writeService;
    @Autowired
    private FieldMapService fieldMapService;
    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);

    @Override
    public void init(JobConfDO jobConf) {
        this.jobConf = jobConf;
    }

    private void initJob() {
        readService = readServiceFactory.get(jobConf.srcType);
        readService.init(jobConf);
        fieldMapService.init(jobConf);
        writeService.init(jobConf);
    }

    @Override
    @Async
    public void run() {
        initJob();

        while (true) {
            SyncDataSet syncDataSet = new SyncDataSet();
            readService.run(syncDataSet);
            fieldMapService.run(syncDataSet, readService.getFields(), writeService.getFields());
            writeService.run(syncDataSet);
            // 全量同步只进行一次
            if (SyncTypeEnum.TOTAL.equals(jobConf.syncType)) {
                break;
            }

            TimeUtil.sleep(jobConf.syncInterval);
        }
    }

}
