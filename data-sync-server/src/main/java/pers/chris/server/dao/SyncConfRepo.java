package pers.chris.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.chris.server.model.SyncConf;

@Repository
public interface SyncConfRepo extends JpaRepository<SyncConf, String> {

    SyncConf findBySyncId(String syncId);

}
