package pers.chris.job.reader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pers.chris.common.model.PluginConfBO;
import pers.chris.common.model.ValueFilterConfBO;
import pers.chris.common.plugin.ResponseParsePluginable;
import pers.chris.common.SyncDataSet;
import pers.chris.common.typeEnum.FieldTypeEnum;
import pers.chris.job.filter.APIFilter;
import pers.chris.common.model.APIConfBO;

import java.net.URI;
import java.util.*;

public class APIReader implements Readable, APIReadable {

    private APIConfBO apiConf;
    private Map<String, FieldTypeEnum> fields;
    private APIFilter filter;
    private ResponseParsePluginable responseParsePlugin;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(APIReader.class);

    public APIReader (APIConfBO apiConf, List<ValueFilterConfBO> valueFilterConfs, PluginConfBO pluginConf) {

    }

    @Override
    public void init(APIConfBO apiConf, APIFilter apiFilter, ResponseParsePluginable responseParsePluginable) {
        this.apiConf = apiConf;
        fields = new LinkedHashMap<>();
        filter = apiFilter;
        responseParsePlugin = responseParsePluginable;

        console();
    }

    @Override
    public void run(SyncDataSet syncDataSet) {
        // 参数设置
        Map<String, String> fetchParams = parseFetchParam(apiConf.paramJson);
        // 时间过滤参数
        fetchParams.putAll(parseFetchParam(filter.run()));

        List<Map<String, String>> rows = fetch(apiConf.url, fetchParams);
        syncDataSet.setRows(rows);
        // 接口数据字段格式统一默认为STRING
        if (!rows.isEmpty()) {
            fieldGenerate(new LinkedList<>(rows.get(0).keySet()));
        }
    }

    private List<Map<String, String>> fetch(String url, Map<String, String> params) {
        // Request
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        params.forEach(builder::queryParam);
        URI uri = builder.build().toUri();

        RequestEntity<Void> request = RequestEntity.get(uri).build();
        // Build
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        // Response
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        LOGGER.info(response.getBody());
        return responseParsePlugin.run(response.getBody());
    }
    // 参数创建
    private Map<String, String> parseFetchParam(String paramJson) {
        Map<String, String> params = new HashMap<>();

        try {
            if (!("".equals(paramJson) || paramJson == null)) {
                params = objectMapper.readValue(paramJson, new TypeReference<Map<String, String>>() {
                });
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Type of params is incorrect: " + paramJson);
        }
        return params;
    }

    private void fieldGenerate(List<String> fieldNames) {
        for (String fieldName : fieldNames) {
            fields.put(fieldName, FieldTypeEnum.STRING);
        }
    }

    @Override
    public Map<String, FieldTypeEnum> getFields() {
        return fields;
    }

    private void console() {
        LOGGER.info("ReadServiceConf: " + apiConf.toString());
    }

}
