package pers.chris.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pers.chris.server.service.JobService;
import pers.chris.server.model.JobConfDO;
import pers.chris.server.dao.JobConfRepo;
import pers.chris.server.service.JobService2;


@RestController
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Controller {

    @Autowired
    private JobConfRepo jobConfRepo;
    @Autowired
    private JobService jobService;
    @Autowired
    private JobService2 jobService2;

    @GetMapping("/run/{jobId}")
    public String runOne(@PathVariable("jobId") String jobId) {
        JobConfDO jobConfDO = jobConfRepo.findByJobId(jobId);
        jobService2.init(jobConfDO);
        jobService2.start();
//        jobService.init(jobConfDO);
//        jobService.run();
        // TODO return
        return "success";
    }

}
