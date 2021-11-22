package pers.chris.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pers.chris.core.model.APIConfDO;

public interface APIConfRepo extends JpaRepository<APIConfDO, String> {

    APIConfDO findByApiId(String apiId);


}
