package pers.chris.core.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.chris.core.common.SyncDataSet;
import pers.chris.core.common.typeEnum.FieldTypeEnum;
import pers.chris.core.exception.FieldMapException;
import pers.chris.core.model.FieldMapConf;
import pers.chris.core.util.FieldUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Mapper implements FieldMappable {

    private List<FieldMapperUnit> fieldMapperUnits;
    private static final Logger LOGGER = LoggerFactory.getLogger(Mapper.class);

    @Override
    public void init(List<FieldMapConf> fieldMapConfs) {
        fieldMapperUnits = new LinkedList<>();

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
