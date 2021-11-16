package pers.chris.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pers.chris.core.model.APIConf;

public interface APIRepo extends JpaRepository<APIConf, String> {

    APIConf findByAPIId(String APIId);


}
