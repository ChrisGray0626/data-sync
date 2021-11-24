package pers.chris.server.service.Impl;

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
import pers.chris.common.SyncDataSet;
import pers.chris.common.typeEnum.DataSourceTypeEnum;
import pers.chris.common.typeEnum.FieldTypeEnum;
import pers.chris.common.plugin.BaseResponseParsePlugin;
import pers.chris.server.service.PluginService;
import pers.chris.server.service.ReadService;
import pers.chris.server.service.ValueFilterService;
import pers.chris.server.model.APIConfDO;
import pers.chris.server.model.JobConfDO;
import pers.chris.server.dao.APIConfRepo;

import java.net.URI;
import java.util.*;

@Service(DataSourceTypeEnum.API)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class APIReadServiceImpl implements ReadService {

    private APIConfDO APIConf;
    private Map<String, FieldTypeEnum> fields;
    private BaseResponseParsePlugin responseParsePluginable;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(APIReadServiceImpl.class);
    @Autowired
    private APIConfRepo APIConfRepo;
    @Autowired
    @Qualifier(DataSourceTypeEnum.API + "ValueFilter")
    private ValueFilterService valueFilterService;
    @Autowired
    private PluginService pluginService;

    @Override
    public void init(JobConfDO jobConf) {
        fields = new LinkedHashMap<>();
        APIConf = APIConfRepo.findByApiId(jobConf.srcConfId);

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
