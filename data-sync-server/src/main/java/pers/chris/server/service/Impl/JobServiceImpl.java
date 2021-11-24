package pers.chris.server.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pers.chris.common.SyncDataSet;
import pers.chris.common.exception.ExecutorNotFoundException;
import pers.chris.common.model.*;
import pers.chris.common.typeEnum.DataSourceTypeEnum;
import pers.chris.common.typeEnum.SyncTypeEnum;
import pers.chris.job.SyncJob;
import pers.chris.server.dao.APIConfRepo;
import pers.chris.server.dao.DBConfRepo;
import pers.chris.server.dao.FilterConfRepo;
import pers.chris.server.dao.MapperConfRepo;
import pers.chris.server.factory.ReadServiceFactory;
import pers.chris.server.model.FilterConfDO;
import pers.chris.server.model.MapperConfDO;
import pers.chris.server.service.FieldMapService;
import pers.chris.server.service.JobService;
import pers.chris.server.service.ReadService;
import pers.chris.server.service.WriteService;
import pers.chris.server.model.JobConfDO;
import pers.chris.common.util.TimeUtil;

import java.util.LinkedList;
import java.util.List;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JobServiceImpl implements JobService {

    private SyncJob syncJob;
    private JobConfDO jobConfDO;
    @Autowired
    private DBConfRepo dbConfRepo;
    @Autowired
    private APIConfRepo apiConfRepo;
    @Autowired
    private FilterConfRepo filterConfRepo;
    @Autowired
    private MapperConfRepo mapperConfRepo;
    private ReadService readService;
    @Autowired
    private ReadServiceFactory readServiceFactory;
    @Autowired
    private WriteService writeService;
    @Autowired
    private FieldMapService fieldMapService;
    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);

    @Override
    public void init(JobConfDO jobConfDO) {
        this.jobConfDO = jobConfDO;
    }

    private void initJob() {
        readService = readServiceFactory.get(jobConfDO.srcType);
        readService.init(jobConfDO);
        fieldMapService.init(jobConfDO);
        writeService.init(jobConfDO);
    }

    @Override
//    @Async
    public void run() {
        initJob();

        while (true) {
            SyncDataSet syncDataSet = new SyncDataSet();
            readService.run(syncDataSet);
            fieldMapService.run(syncDataSet, readService.getFields(), writeService.getFields());
            writeService.run(syncDataSet);
            // 全量同步只进行一次
            if (SyncTypeEnum.TOTAL.equals(jobConfDO.syncType)) {
                break;
            }

            TimeUtil.sleep(jobConfDO.syncInterval);
        }
    }

}
