package pers.chris.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.chris.core.model.DBConf;

@Repository
public interface DBConfRepo extends JpaRepository<DBConf, String> {

    DBConf findByDbId(String dbId);

}
