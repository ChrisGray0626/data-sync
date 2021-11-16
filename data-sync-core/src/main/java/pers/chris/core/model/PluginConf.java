package pers.chris.core.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PluginConf {

    @Id
    public String pluginId;
    public String pluginName;
    public String jarPath;
    public String className;

}