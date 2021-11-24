package pers.chris.server.service;


import pers.chris.common.plugin.BaseResponseParsePlugin;

public interface PluginService {

    void init(String pluginId);

    BaseResponseParsePlugin getInstance();

}
