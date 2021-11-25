package pers.chris.common.model;

public class APIConfBO extends DataSourceConf {

    public String apiId;
    public String url;
    public String paramJson;
    public String pluginId;
    public PluginConfBO responsePluginConf;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParamJson() {
        return paramJson;
    }

    public void setParamJson(String paramJson) {
        this.paramJson = paramJson;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public PluginConfBO getResponsePluginConf() {
        return responsePluginConf;
    }

    public void setResponsePluginConf(PluginConfBO responsePluginConf) {
        this.responsePluginConf = responsePluginConf;
    }

    @Override
    public String toString() {
        return "APIConfBO{" +
                "apiId='" + apiId + '\'' +
                ", url='" + url + '\'' +
                ", paramJson='" + paramJson + '\'' +
                ", pluginId='" + pluginId + '\'' +
                ", responsePluginConf=" + responsePluginConf +
                '}';
    }
}
