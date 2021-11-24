package pers.chris.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pers.chris.server.model.APIConfDO;

public interface APIConfRepo extends JpaRepository<APIConfDO, String> {

    APIConfDO findByApiId(String apiId);


}
