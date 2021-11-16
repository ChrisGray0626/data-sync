package pers.chris.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.chris.core.model.SyncConf;

@Repository
public interface SyncConfRepo extends JpaRepository<SyncConf, String> {

    SyncConf findBySyncId(String syncId);

}
