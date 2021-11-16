package pers.chris.core.service;


import pers.chris.common.plugin.ResponseParsePluginable;

public interface PluginService {

    void init(String pluginId);

    ResponseParsePluginable getInstance();

}
