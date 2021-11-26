package pers.chris.job.mapper;

import pers.chris.common.exception.MapRuleSyntaxException;
import pers.chris.job.base.mapper.BaseMapperUnit;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * （常规）映射器元件
 */
public class MapperUnit extends BaseMapperUnit {

    public MapperUnit(String rule) throws MapRuleSyntaxException {
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

    /**
     * 规则检查
     * @throws MapRuleSyntaxException 不符合规范的规则
     */
    private void checkRule() throws MapRuleSyntaxException {
        Pattern pattern = Pattern.compile("\\{.*?\\}=(([\\s\\S]*)\\{.*?\\})+");
        Matcher matcher = pattern.matcher(rule);
        if (!matcher.matches()) {
            throw new MapRuleSyntaxException(rule);
        }
    }

    /**
     * 规则解析
     */
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

    public List<String> getDstFieldNames() {
        return dstFieldNames;
    }

    public List<String> getSrcFieldNames() {
        return srcFieldNames;
    }

}
