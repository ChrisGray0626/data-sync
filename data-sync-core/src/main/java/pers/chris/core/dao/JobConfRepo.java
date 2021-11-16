package pers.chris.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.chris.core.model.JobConf;

@Repository
public interface JobConfRepo extends JpaRepository<JobConf, String> {

    JobConf findByJobId(String jobId);

}
