package pers.chris.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pers.chris.core.model.PluginConf;

public interface PluginRepo extends JpaRepository<PluginConf, String> {

    PluginConf findByPluginId(String pluginId);

}
