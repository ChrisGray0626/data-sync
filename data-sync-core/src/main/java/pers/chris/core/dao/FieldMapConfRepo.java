package pers.chris.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.chris.core.model.FieldMapConfDO;

import java.util.List;

@Repository
public interface FieldMapConfRepo extends JpaRepository<FieldMapConfDO, String> {

    List<FieldMapConfDO> findByJobId(String jobId);

}
