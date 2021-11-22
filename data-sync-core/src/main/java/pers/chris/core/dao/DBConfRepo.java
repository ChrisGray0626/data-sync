package pers.chris.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.chris.core.model.DBConfDO;

@Repository
public interface DBConfRepo extends JpaRepository<DBConfDO, String> {

    DBConfDO findByDbId(String dbId);

}
