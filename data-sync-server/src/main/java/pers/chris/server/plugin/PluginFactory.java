package pers.chris.server.plugin;

import pers.chris.common.plugin.BasePlugin;
import pers.chris.server.model.PluginConfDO;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginFactory {

    private PluginConfDO pluginConf;
    private URLClassLoader urlClassLoader;

    public void init(PluginConfDO pluginConf) {
        this.pluginConf = pluginConf;

        try {
            urlClassLoader = new URLClassLoader(new URL[]{new URL(pluginConf.jarPath)});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public BasePlugin getInstance() {
        BasePlugin plugin = null;
        try {
            plugin = (BasePlugin) urlClassLoader.loadClass(pluginConf.className).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return plugin;
    }

}
