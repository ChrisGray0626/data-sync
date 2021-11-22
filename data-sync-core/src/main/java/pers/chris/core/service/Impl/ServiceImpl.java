package pers.chris.core.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pers.chris.common.model.*;
import pers.chris.core.dao.*;
import pers.chris.core.model.FieldMapConfDO;
import pers.chris.core.model.ValueFilterConfDO;
import pers.chris.job.SyncJob;

import java.util.LinkedList;
import java.util.List;

public class ServiceImpl {

    private SyncJob syncJob;
    @Autowired
    private APIConfRepo apiConfRepo;
    @Autowired
    private DBConfRepo dbConfRepo;
    @Autowired
    private ValueFilterConfRepo valueFilterConfRepo;
    @Autowired
    private FieldMapConfRepo fieldMapConfRepo;
    @Autowired
    private PluginConfRepo pluginConfRepo;
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceImpl.class);

    public void init(JobConf jobConf) {
        DBConfBO srcDBConf = new DBConfBO();
        APIConfBO srcAPIConf = new APIConfBO();
        DBConfBO dstDBConf = new DBConfBO();
        List<ValueFilterConfBO> valueFilterConfs = new LinkedList<>();
        List<FieldMapConfBO> fieldMapConfs = new LinkedList<>();
        PluginConfBO pluginConf = new PluginConfBO();

        BeanUtils.copyProperties(dbConfRepo.findByDbId(jobConf.srcConfId), srcDBConf);
        BeanUtils.copyProperties(apiConfRepo.findByApiId(jobConf.srcConfId), srcAPIConf);
        BeanUtils.copyProperties(dbConfRepo.findByDbId(jobConf.dstConfId), dstDBConf);
        for (ValueFilterConfDO valueFilterConfDO: valueFilterConfRepo.findByJobId(jobConf.jobId)) {
            ValueFilterConfBO valueFilterConfBO = new ValueFilterConfBO();
            BeanUtils.copyProperties(valueFilterConfDO, valueFilterConfBO);
            valueFilterConfs.add(valueFilterConfBO);
        }
        for (FieldMapConfDO fieldMapConfDO: fieldMapConfRepo.findByJobId(jobConf.jobId)) {
            FieldMapConfBO fieldMapConfBO = new FieldMapConfBO();
            BeanUtils.copyProperties(fieldMapConfDO, fieldMapConfBO);
            fieldMapConfs.add(fieldMapConfBO);
        }
        BeanUtils.copyProperties(pluginConfRepo.findByPluginId(srcAPIConf.pluginId), pluginConf);


    }
}
