package pers.chris.server.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pers.chris.common.exception.ExecutorNotFoundException;
import pers.chris.common.model.*;
import pers.chris.common.type.DataSourceTypeEnum;
import pers.chris.server.dao.*;
import pers.chris.server.model.JobConfDO;
import pers.chris.server.model.MapperConfDO;
import pers.chris.server.model.FilterConfDO;
import pers.chris.job.SyncJob;
import pers.chris.server.service.JobService;

import java.util.LinkedList;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private SyncJob syncJob;
    @Autowired
    private APIConfRepo apiConfRepo;
    @Autowired
    private DBConfRepo dbConfRepo;
    @Autowired
    private FilterConfRepo filterConfRepo;
    @Autowired
    private MapperConfRepo mapperConfRepo;
    @Autowired
    private PluginConfRepo pluginConfRepo;
    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);

    public void init(JobConfDO jobConfDO) {
        JobConfBO jobConf = new JobConfBO();
        DataSourceConf srcConf = null;
        DataSourceConf dstConf = null;
        List<FilterConfBO> filterConfs = new LinkedList<>();
        List<MapperConfBO> mapperConfs = new LinkedList<>();

        BeanUtils.copyProperties(jobConfDO, jobConf);

        switch (jobConf.srcType) {
            case DataSourceTypeEnum.DATABASE:
                DBConfBO dbConf = new DBConfBO();
                BeanUtils.copyProperties(dbConfRepo.findByDbId(jobConf.srcConfId), dbConf);
                srcConf = dbConf;
                break;
            case DataSourceTypeEnum.API:
                APIConfBO apiConf = new APIConfBO();
                PluginConfBO pluginConf = new PluginConfBO();
                BeanUtils.copyProperties(apiConfRepo.findByApiId(jobConf.srcConfId), apiConf);
                BeanUtils.copyProperties(pluginConfRepo.findByPluginId(apiConf.pluginId), pluginConf);
                apiConf.responsePluginConf = pluginConf;
                srcConf = apiConf;
                break;
            default:
        }

        switch (jobConf.dstType) {
            case DataSourceTypeEnum.DATABASE:
                DBConfBO dbConf = new DBConfBO();
                BeanUtils.copyProperties(dbConfRepo.findByDbId(jobConf.dstConfId), dbConf);
                dstConf = dbConf;
                break;
            default:
        }

        for (FilterConfDO filterConfDO: filterConfRepo.findByJobId(jobConf.jobId)) {
            FilterConfBO filterConf = new FilterConfBO();
            BeanUtils.copyProperties(filterConfDO, filterConf);
            filterConfs.add(filterConf);
        }

        for (MapperConfDO mapperConfDO: mapperConfRepo.findByJobId(jobConf.jobId)) {
            MapperConfBO mapperConf = new MapperConfBO();
            BeanUtils.copyProperties(mapperConfDO, mapperConf);
            mapperConfs.add(mapperConf);
        }

        try {
            syncJob = new SyncJob(jobConf, srcConf, dstConf, filterConfs, mapperConfs);
        } catch (ExecutorNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void run() {
        syncJob.start();
    }

    @Async
    public void start() {
        run();
    }
}
