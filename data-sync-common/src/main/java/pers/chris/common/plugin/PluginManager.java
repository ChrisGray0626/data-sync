package pers.chris.common.plugin;

import pers.chris.common.model.PluginConfBO;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginManager {

    private final PluginConfBO pluginConf;
    private URLClassLoader urlClassLoader;

    public PluginManager(PluginConfBO pluginConf) {
        this.pluginConf = pluginConf;

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
