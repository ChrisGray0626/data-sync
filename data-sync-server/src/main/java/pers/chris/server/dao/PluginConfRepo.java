package pers.chris.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pers.chris.server.model.PluginConfDO;

public interface PluginConfRepo extends JpaRepository<PluginConfDO, String> {

    PluginConfDO findByPluginId(String pluginId);

}
