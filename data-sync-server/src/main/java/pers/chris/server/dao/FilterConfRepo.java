package pers.chris.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.chris.server.model.FilterConfDO;

import java.util.List;

@Repository
public interface FilterConfRepo extends JpaRepository<FilterConfDO, String> {

    List<FilterConfDO> findByJobId(String jobId);

}
