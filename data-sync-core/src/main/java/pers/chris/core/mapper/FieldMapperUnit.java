package pers.chris.core.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.chris.core.common.Unit;
import pers.chris.core.exception.FieldMapException;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldMapperUnit implements Unit {

    private String rule;
    private final List<String> dstFieldNames; // 目标字段
    private final List<String> srcFieldNames; // 源字段
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldMapperUnit.class);

    public FieldMapperUnit(String rule) {
        this.rule = rule;
        dstFieldNames = new ArrayList<>();
        srcFieldNames = new ArrayList<>();

        checkRule();
        parseRule();
    }

    @Override
    public void run(Map<String, String> data) {
        // 当前仅支持映射到唯一目标字段
        String dstField = dstFieldNames.get(0);
        List<String> srcValues = new ArrayList<>();
        for (String srcField: srcFieldNames) {
            srcValues.add(data.get(srcField));
        }
        // 格式化目标值
        Formatter formatter = new Formatter();
        formatter.format(rule, srcValues.toArray());
        String dstValue = formatter.toString();
        // 移除源字段
        for (String srcField: srcFieldNames) {
            data.remove(srcField);
        }
        // 新增目标字段
        data.put(dstField, dstValue);
    }

    private void checkRule() {
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

    private void parseRule() {
        // 匹配目标字段"{DstFieldName}"
        Pattern dstPattern = Pattern.compile("(?<=\\{).*?(?=\\}=)");
        Matcher dstMatcher = dstPattern.matcher(rule);
        while (dstMatcher.find()) {
            dstFieldNames.add(dstMatcher.group(0));
        }
        // 去除目标字段内容"{DstFieldName}="
        rule = rule.replaceAll("\\{.*?\\}=", "");
        // 匹配源字段"{SrcFieldName}"
        Pattern srcPattern = Pattern.compile("(?<=\\{).*?(?=\\})");
        Matcher srcMatcher = srcPattern.matcher(rule);
        while (srcMatcher.find()) {
            srcFieldNames.add(srcMatcher.group(0));
        }
        // 替换源字段内容"{SrcFieldName}"为"%s"
        rule = rule.replaceAll("\\{.*?\\}", "%s");
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public List<String> getDstFieldNames() {
        return dstFieldNames;
    }

    public List<String> getSrcFieldNames() {
        return srcFieldNames;
    }

}
