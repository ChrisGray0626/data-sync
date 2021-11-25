package pers.chris.common.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * Response 解析插件的基类
 * 用于接口数据
 */
public abstract class BaseResponseParsePlugin extends BasePlugin {

    protected static final ObjectMapper objectMapper = new ObjectMapper();

    public abstract List<Map<String, String>> run(String response);

}
