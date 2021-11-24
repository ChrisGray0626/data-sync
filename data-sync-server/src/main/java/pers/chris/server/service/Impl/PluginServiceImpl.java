package pers.chris.server.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import pers.chris.server.model.PluginConfDO;
import pers.chris.common.plugin.BaseResponseParsePlugin;
import pers.chris.server.service.PluginService;
import pers.chris.server.dao.PluginConfRepo;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PluginServiceImpl implements PluginService {

    private PluginConfDO pluginConf;
    private URLClassLoader urlClassLoader;
    @Autowired
    private PluginConfRepo pluginConfRepo;

    public void init(String pluginId) {
        pluginConf = pluginConfRepo.findByPluginId(pluginId);

        try {
            urlClassLoader = new URLClassLoader(new URL[]{new URL(pluginConf.jarPath)});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public BaseResponseParsePlugin getInstance() {
        BaseResponseParsePlugin plugin = null;
        try {
            plugin = (BaseResponseParsePlugin) urlClassLoader.loadClass(pluginConf.className).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return plugin;
    }

}
