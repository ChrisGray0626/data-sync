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
import pers.chris.common.plugin.BaseResponseParsePlugin;
import pers.chris.common.plugin.PluginManager;
import pers.chris.common.type.DataSourceTypeEnum;
import pers.chris.common.type.FieldTypeEnum;
import pers.chris.job.base.filter.BaseFilter;
import pers.chris.job.filter.api.APIFilter;
import pers.chris.common.model.APIConfBO;
import pers.chris.job.base.BaseReader;

import java.net.URI;
import java.util.*;

/**
 * 接口读取器
 */
public class APIReader extends BaseReader {

    private final APIConfBO apiConf;
    private final APIFilter apiFilter;
    private final BaseResponseParsePlugin responseParsePlugin;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(APIReader.class);

    public APIReader (DataSourceConf conf, BaseFilter filter) {
        readerType = DataSourceTypeEnum.API;
        fields = new LinkedHashMap<>();
        apiConf = (APIConfBO) conf;
        apiFilter = (APIFilter) filter;
        responseParsePlugin = new PluginManager(apiConf.responsePluginConf).getInstance();

        console();
    }

    protected void run() {
        // 参数设置
        Map<String, String> fetchParams = parseFetchParam(apiConf.paramJson);
        // 时间过滤参数设置
        fetchParams.putAll(parseFetchParam(apiFilter.run()));
        // 数据拉取、解析
        syncDataSet.setRows(responseParsePlugin.run(fetch(apiConf.url, fetchParams)));
    }

    public void start() {
        run();
        // 如有数据则生成字段
        if (!syncDataSet.getRows().isEmpty()) {
            fieldGenerate(syncDataSet.getRows().get(0).keySet());
        }
    }

    /**
     * 数据拉取
     * @param url 接口链接
     * @param params 接口参数
     * @return data 接口数据
     */
    private String fetch(String url, Map<String, String> params) {
        // Request
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        params.forEach(builder::queryParam);
        URI uri = builder.build().toUri();

        RequestEntity<Void> request = RequestEntity.get(uri).build();
        // Build
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        // Response
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        return response.getBody();
    }

    /**
     * 接口参数解析
     * @param paramJson JSON格式的接口参数
     * @return param Map格式的接口参数
     */
    private Map<String, String> parseFetchParam(String paramJson) {
        Map<String, String> params = new HashMap<>();

        try {
            if (!("".equals(paramJson) || paramJson == null)) {
                params = OBJECT_MAPPER.readValue(paramJson, new TypeReference<Map<String, String>>() {
                });
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Type of params is incorrect: " + paramJson);
        }
        return params;
    }

    /**
     * 字段生成
     * 接口数据字段格式统一默认为STRING
     * @param fieldNames 字段名
     */
    private void fieldGenerate(Set<String> fieldNames) {
        for (String fieldName : fieldNames) {
            fields.put(fieldName, FieldTypeEnum.STRING);
        }
    }

    private void console() {
        LOGGER.info(readerType + ": " + apiConf.toString());
    }

}
