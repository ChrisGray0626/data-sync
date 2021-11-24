package pers.chris.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.chris.server.model.MapperConfDO;

import java.util.List;

@Repository
public interface MapperConfRepo extends JpaRepository<MapperConfDO, String> {

    List<MapperConfDO> findByJobId(String jobId);

}
