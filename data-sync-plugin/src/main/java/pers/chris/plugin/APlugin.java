package pers.chris.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pers.chris.common.plugin.BaseResponseParsePlugin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class APlugin extends BaseResponseParsePlugin {

    @Override
    public List<Map<String, String>> run(String response) {
        List<Map<String, String>> rows = new LinkedList<>();
        Map<String, Object> result = new HashMap<>();

        try {
            result = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> datas = objectMapper.convertValue(result.get("data"), new TypeReference<List<Map<String, Object>>>() {});
        for (Map<String, Object> data: datas) {
            String id = objectMapper.convertValue(data.get("id"), new TypeReference<String>() {});
            List<Map<String, String>> partRows = objectMapper.convertValue(data.get("data"), new TypeReference<List<Map<String, String>>>() {});

            for (Map<String, String> row: partRows) {
                row.put("id", id);
            }
            rows.addAll(partRows);
        }
        return rows;
    }
}
