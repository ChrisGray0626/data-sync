package pers.chris.common.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PluginConfBO {

    @Id
    public String pluginId;
    public String pluginName;
    public String jarPath;
    public String className;

}