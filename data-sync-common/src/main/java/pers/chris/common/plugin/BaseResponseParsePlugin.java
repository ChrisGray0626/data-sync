package pers.chris.common.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public abstract class BaseResponseParsePlugin extends BasePlugin {

    protected static final ObjectMapper objectMapper = new ObjectMapper();

    public abstract List<Map<String, String>> run(String response);

}
