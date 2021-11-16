package pers.chris.core.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import pers.chris.core.model.PluginConf;
import pers.chris.common.plugin.ResponseParsePluginable;
import pers.chris.core.service.PluginService;
import pers.chris.core.dao.PluginRepo;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PluginServiceImpl implements PluginService {

    private PluginConf pluginConf;
    private URLClassLoader urlClassLoader;
    @Autowired
    private PluginRepo pluginRepo;

    public void init(String pluginId) {
        pluginConf = pluginRepo.findByPluginId(pluginId);

        try {
            urlClassLoader = new URLClassLoader(new URL[]{new URL(pluginConf.jarPath)});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public ResponseParsePluginable getInstance() {
        ResponseParsePluginable plugin = null;
        try {
            plugin = (ResponseParsePluginable) urlClassLoader.loadClass(pluginConf.className).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return plugin;
    }

}
