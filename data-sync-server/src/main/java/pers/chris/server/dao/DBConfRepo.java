package pers.chris.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.chris.server.model.DBConfDO;

@Repository
public interface DBConfRepo extends JpaRepository<DBConfDO, String> {

    DBConfDO findByDbId(String dbId);

}
