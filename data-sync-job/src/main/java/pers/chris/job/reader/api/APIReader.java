package pers.chris.job.reader.api;

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
import pers.chris.common.model.DataSourceConf;
import pers.chris.common.model.PluginConfBO;
import pers.chris.common.model.FilterConfBO;
import pers.chris.common.plugin.BaseResponseParsePlugin;
import pers.chris.common.SyncDataSet;
import pers.chris.common.plugin.PluginManager;
import pers.chris.common.typeEnum.DataSourceTypeEnum;
import pers.chris.common.typeEnum.FieldTypeEnum;
import pers.chris.job.filter.BaseFilter;
import pers.chris.job.filter.api.APIFilter;
import pers.chris.common.model.APIConfBO;
import pers.chris.job.reader.BaseReader;

import java.net.URI;
import java.util.*;

public class APIReader extends BaseReader {

    private APIConfBO apiConf;
    private APIFilter apiFilter;
    private BaseResponseParsePlugin responseParsePlugin;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(APIReader.class);

    public APIReader (DataSourceConf conf, BaseFilter filter) {
        readerType = DataSourceTypeEnum.API;
        fields = new LinkedHashMap<>();
        apiConf = (APIConfBO) conf;
        apiFilter = (APIFilter) filter;
        responseParsePlugin = new PluginManager(apiConf.responsePluginConf).getInstance();

        console();
    }

    @Deprecated
    public void init(APIConfBO apiConf, APIFilter apiFilter, BaseResponseParsePlugin responseParsePlugin) {
        this.apiConf = apiConf;
        fields = new LinkedHashMap<>();
        this.apiFilter = apiFilter;
        this.responseParsePlugin = responseParsePlugin;

        console();
    }

    @Override
    public void run(SyncDataSet syncDataSet) {
        // 参数设置
        Map<String, String> fetchParams = parseFetchParam(apiConf.paramJson);
        // 时间过滤参数
        fetchParams.putAll(parseFetchParam(apiFilter.run()));

        List<Map<String, String>> rows = fetch(apiConf.url, fetchParams);
        syncDataSet.setRows(rows);
        // 接口数据字段格式统一默认为STRING
        if (!rows.isEmpty()) {
            fieldGenerate(rows.get(0).keySet());
        }
    }

    protected void run() {
        // 参数设置
        Map<String, String> fetchParams = parseFetchParam(apiConf.paramJson);
        // 时间过滤参数
        fetchParams.putAll(parseFetchParam(apiFilter.run()));

        List<Map<String, String>> rows = fetch(apiConf.url, fetchParams);
        syncDataSet.setRows(rows);
    }

    public void start() {
        run();
        // 如有数据则生成字段
        if (!syncDataSet.getRows().isEmpty()) {
            fieldGenerate(syncDataSet.getRows().get(0).keySet());
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
        return responseParsePlugin.run(response.getBody());
    }
    // 参数解析创建
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
    // 字段生成
    private void fieldGenerate(Set<String> fieldNames) {
        for (String fieldName : fieldNames) {
            // 接口数据字段格式统一默认为STRING
            fields.put(fieldName, FieldTypeEnum.STRING);
        }
    }

    private void console() {
        LOGGER.info("APIReaderConf: " + apiConf.toString());
    }

}
