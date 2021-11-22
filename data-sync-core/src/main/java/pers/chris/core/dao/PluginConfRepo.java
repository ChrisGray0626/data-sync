package pers.chris.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pers.chris.core.model.PluginConfDO;

public interface PluginConfRepo extends JpaRepository<PluginConfDO, String> {

    PluginConfDO findByPluginId(String pluginId);

}
