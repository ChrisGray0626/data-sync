package pers.chris.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pers.chris.common.plugin.BaseResponseParsePlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BPlugin extends BaseResponseParsePlugin {

    @Override
    public List<Map<String, String>> run(String response) {
        Map<String, Object> result = new HashMap<>();

        try {
            result = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return objectMapper.convertValue(result.get("data"), new TypeReference<List<Map<String, String>>>() {});
    }

}
