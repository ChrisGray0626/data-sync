package pers.chris.core.plugin;

import pers.chris.common.plugin.Pluginable;
import pers.chris.core.model.PluginConf;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginFactory {

    private PluginConf pluginConf;
    private URLClassLoader urlClassLoader;

    public void init(PluginConf pluginConf) {
        this.pluginConf = pluginConf;

        try {
            urlClassLoader = new URLClassLoader(new URL[]{new URL(pluginConf.jarPath)});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public Pluginable getInstance() {
        Pluginable plugin = null;
        try {
            plugin = (Pluginable) urlClassLoader.loadClass(pluginConf.className).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return plugin;
    }

}
