package pers.chris.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pers.chris.core.service.JobService;
import pers.chris.core.model.JobConf;
import pers.chris.core.dao.JobConfRepo;


@RestController
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Controller {

    @Autowired
    private JobConfRepo jobConfRepo;
    @Autowired
    private JobService jobService;

    @GetMapping("/run/{jobId}")
    public String runOne(@PathVariable("jobId") String jobId) {
        JobConf jobConf = jobConfRepo.findByJobId(jobId);
        jobService.init(jobConf);
        jobService.run();
        // TODO return
        return "success";
    }

}
