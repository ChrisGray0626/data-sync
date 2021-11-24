package pers.chris.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.chris.server.model.JobConfDO;

@Repository
public interface JobConfRepo extends JpaRepository<JobConfDO, String> {

    JobConfDO findByJobId(String jobId);

}
