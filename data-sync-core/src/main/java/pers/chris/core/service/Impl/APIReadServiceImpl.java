package pers.chris.core.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pers.chris.core.common.SyncDataSet;
import pers.chris.core.common.typeEnum.DataSourceTypeEnum;
import pers.chris.core.common.typeEnum.FieldTypeEnum;
import pers.chris.common.plugin.ResponseParsePluginable;
import pers.chris.core.service.PluginService;
import pers.chris.core.service.ReadService;
import pers.chris.core.service.ValueFilterService;
import pers.chris.core.model.APIConf;
import pers.chris.core.model.JobConf;
import pers.chris.core.dao.APIRepo;

import java.net.URI;
import java.util.*;

@Service(DataSourceTypeEnum.API)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class APIReadServiceImpl implements ReadService {

    private APIConf APIConf;
    private Map<String, FieldTypeEnum> fields;
    private ResponseParsePluginable responseParsePluginable;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(APIReadServiceImpl.class);
    @Autowired
    private APIRepo APIRepo;
    @Autowired
    @Qualifier(DataSourceTypeEnum.API + "ValueFilter")
    private ValueFilterService valueFilterService;
    @Autowired
    private PluginService pluginService;

    @Override
    public void init(JobConf jobConf) {
        fields = new LinkedHashMap<>();
        APIConf = APIRepo.findByAPIId(jobConf.srcConfId);

        valueFilterService.init(jobConf);
        pluginService.init(APIConf.pluginId);
        responseParsePluginable = pluginService.getInstance();

        console();
    }

    @Override
    public void run(SyncDataSet syncDataSet) {
        // 参数设置
        Map<String, String> fetchParams = parseFetchParam(APIConf.paramJson);
        // 时间过滤参数
        fetchParams.putAll(parseFetchParam(valueFilterService.run()));

        List<Map<String, String>> rows = fetch(APIConf.url, fetchParams);
        syncDataSet.setRows(rows);
        // 接口数据字段格式统一默认为STRING
        if (!rows.isEmpty()) {
            fieldGenerate(new LinkedList<>(rows.get(0).keySet()));
        }
    }

    public List<Map<String, String>> fetch(String url, Map<String, String> params) {
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
        return responseParsePluginable.run(response.getBody());
    }
    // 参数创建
    private Map<String, String> parseFetchParam(String paramJson) {
        Map<String, String> params = new HashMap<>();

        try {
            if (!("".equals(paramJson) || paramJson == null)) {
                params = objectMapper.readValue(paramJson, new TypeReference<Map<String, String>>() {});
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Type of params is incorrect: " + paramJson);
        }
        return params;
    }

    private void fieldGenerate(List<String> fieldNames) {
        for (String fieldName: fieldNames) {
            fields.put(fieldName, FieldTypeEnum.STRING);
        }
    }

    @Override
    public Map<String, FieldTypeEnum> getFields() {
        return fields;
    }

    private void console() {
        LOGGER.info("ReadServiceConf: " + APIConf.toString());
    }

}
