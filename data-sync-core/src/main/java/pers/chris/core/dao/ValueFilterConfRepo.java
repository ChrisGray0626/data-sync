package pers.chris.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.chris.core.model.ValueFilterConfDO;

import java.util.List;

@Repository
public interface ValueFilterConfRepo extends JpaRepository<ValueFilterConfDO, String> {

    List<ValueFilterConfDO> findByJobId(String jobId);

}
