package pers.chris.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.chris.core.model.ValueFilterConf;

import java.util.List;

@Repository
public interface ValueFilterConfRepo extends JpaRepository<ValueFilterConf, String> {

    List<ValueFilterConf> findByJobId(String jobId);

}
