package pers.chris.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.chris.core.model.FieldMapConf;

import java.util.List;

@Repository
public interface FieldMapConfRepo extends JpaRepository<FieldMapConf, String> {

    List<FieldMapConf> findByJobId(String jobId);

}
