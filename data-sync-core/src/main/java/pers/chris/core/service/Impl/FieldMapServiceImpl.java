package pers.chris.core.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import pers.chris.core.common.SyncDataSet;
import pers.chris.core.mapper.FieldMapperUnit;
import pers.chris.core.common.typeEnum.FieldTypeEnum;
import pers.chris.core.model.FieldMapConf;
import pers.chris.core.service.FieldMapService;
import pers.chris.core.model.JobConf;
import pers.chris.core.dao.FieldMapConfRepo;
import pers.chris.core.exception.FieldMapException;
import pers.chris.core.util.FieldUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FieldMapServiceImpl implements FieldMapService {

    private List<FieldMapperUnit> fieldMapperUnits;
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldMapServiceImpl.class);
    @Autowired
    private FieldMapConfRepo fieldMapConfRepo;

    @Override
    public void init(JobConf jobConf) {
        fieldMapperUnits = new LinkedList<>();
        List<FieldMapConf> fieldMapConfs = fieldMapConfRepo.findByJobId(jobConf.jobId);

        for (FieldMapConf fieldMapConf: fieldMapConfs) {
            FieldMapperUnit fieldMapperUnit = new FieldMapperUnit(fieldMapConf.rule);
            fieldMapperUnits.add(fieldMapperUnit);
        }
    }

    @Override
    public void run(SyncDataSet syncDataSet,
                    Map<String, FieldTypeEnum> readFields,
                    Map<String, FieldTypeEnum> writeFields) {
        checkField(readFields, writeFields);

        List<Map<String, String>> rows = syncDataSet.getRows();

        for (Map<String, String> row: rows) {
            for (FieldMapperUnit fieldMapperUnit : fieldMapperUnits) {
                fieldMapperUnit.run(row);
            }
        }

        syncDataSet.setRows(rows);
    }

    @Deprecated
    private void checkRule() {
        for (FieldMapperUnit fieldMapperUnit : fieldMapperUnits) {
            String rule = fieldMapperUnit.getRule();
            Pattern pattern = Pattern.compile("\\{.*?\\}=(([\\s\\S]*)\\{.*?\\})+");
            Matcher matcher = pattern.matcher(rule);
            try {
                if (!matcher.matches()) {
                    throw new FieldMapException("Rule Syntax Error: '" + rule + "' exists syntax error");
                }
            } catch (FieldMapException e) {
                LOGGER.error(String.valueOf(e));
            }
        }
    }

    @Deprecated
    private void parseRule() {
        for (FieldMapperUnit fieldMapperUnit : fieldMapperUnits) {
            String rule = fieldMapperUnit.getRule();
            // 匹配目标字段"{DstFieldName}"
            Pattern dstPattern = Pattern.compile("(?<=\\{).*?(?=\\}=)");
            Matcher dstMatcher = dstPattern.matcher(rule);
            while (dstMatcher.find()) {
                fieldMapperUnit.getDstFieldNames().add(dstMatcher.group(0));
            }
            // 去除目标字段内容"{DstFieldName}="
            rule = rule.replaceAll("\\{.*?\\}=", "");
            // 匹配源字段"{SrcFieldName}"
            Pattern srcPattern = Pattern.compile("(?<=\\{).*?(?=\\})");
            Matcher srcMatcher = srcPattern.matcher(rule);
            while (srcMatcher.find()) {
                fieldMapperUnit.getSrcFieldNames().add(srcMatcher.group(0));
            }
            // 替换源字段内容"{SrcFieldName}"为"%s"
            rule = rule.replaceAll("\\{.*?\\}", "%s");
            fieldMapperUnit.setRule(rule);
        }
    }

    private void checkField(Map<String, FieldTypeEnum> readFields,
                            Map<String, FieldTypeEnum> writeFields) {
        for (FieldMapperUnit fieldMapperUnit : fieldMapperUnits) {
            List<String> dstFieldNames = fieldMapperUnit.getDstFieldNames();
            List<String> srcFieldNames = fieldMapperUnit.getSrcFieldNames();

            try {
                FieldUtil.checkFieldName(dstFieldNames, srcFieldNames,
                        new ArrayList<>(writeFields.keySet()), new ArrayList<>(readFields.keySet()));
                FieldUtil.checkFieldType(dstFieldNames, srcFieldNames, writeFields, readFields);
            } catch (FieldMapException e) {
                LOGGER.error(String.valueOf(e));
            }
        }
    }

}
