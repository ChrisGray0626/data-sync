package pers.chris.job.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.chris.common.SyncDataSet;
import pers.chris.common.typeEnum.FieldTypeEnum;
import pers.chris.common.exception.FieldMapException;
import pers.chris.common.model.MapperConfBO;
import pers.chris.common.util.FieldUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Mapper extends BaseMapper {

    private List<MapperUnit> mapperUnits;
    private static final Logger LOGGER = LoggerFactory.getLogger(Mapper.class);

    public Mapper (List<MapperConfBO> fieldMapConfs) {
        mapperUnits = new LinkedList<>();

        for (MapperConfBO fieldMapConf: fieldMapConfs) {
            MapperUnit mapperUnit = new MapperUnit(fieldMapConf.rule);
            mapperUnits.add(mapperUnit);
        }
    }

    @Override
    public void init(List<MapperConfBO> fieldMapConfs) {
        mapperUnits = new LinkedList<>();

        for (MapperConfBO fieldMapConf: fieldMapConfs) {
            MapperUnit mapperUnit = new MapperUnit(fieldMapConf.rule);
            mapperUnits.add(mapperUnit);
        }
    }

    @Override
    public void run(SyncDataSet syncDataSet,
                    Map<String, FieldTypeEnum> readerFields,
                    Map<String, FieldTypeEnum> writerFields) {
        checkField(readerFields, writerFields);

        List<Map<String, String>> rows = syncDataSet.getRows();

        for (Map<String, String> row: rows) {
            for (MapperUnit mapperUnit : mapperUnits) {
                mapperUnit.run(row);
            }
        }

        syncDataSet.setRows(rows);
    }

    @Override
    protected void run() {
        // TODO 迭代引用
//        List<Map<String, String>> rows = syncDataSet.getRows();

        for (Map<String, String> row: syncDataSet.getRows()) {
            for (MapperUnit mapperUnit : mapperUnits) {
                mapperUnit.run(row);
            }
        }

//        syncDataSet.setRows(rows);
    }

    @Override
    public void start() {
        checkField();
        run();
    }

    @Deprecated
    private void checkField(Map<String, FieldTypeEnum> readerFields,
                            Map<String, FieldTypeEnum> writerFields) {
        for (MapperUnit mapperUnit : mapperUnits) {
            List<String> dstFieldNames = mapperUnit.getDstFieldNames();
            List<String> srcFieldNames = mapperUnit.getSrcFieldNames();

            try {
                FieldUtil.checkFieldName(dstFieldNames, srcFieldNames,
                        new ArrayList<>(writerFields.keySet()), new ArrayList<>(readerFields.keySet()));
                FieldUtil.checkFieldType(dstFieldNames, srcFieldNames, writerFields, readerFields);
            } catch (FieldMapException e) {
                LOGGER.error(String.valueOf(e));
            }
        }
    }

    private void checkField() {
        for (MapperUnit mapperUnit : mapperUnits) {
            List<String> dstFieldNames = mapperUnit.getDstFieldNames();
            List<String> srcFieldNames = mapperUnit.getSrcFieldNames();

            try {
                FieldUtil.checkFieldName(dstFieldNames, srcFieldNames,
                        new ArrayList<>(writerFields.keySet()), new ArrayList<>(readerFields.keySet()));
                FieldUtil.checkFieldType(dstFieldNames, srcFieldNames, writerFields, readerFields);
            } catch (FieldMapException e) {
                LOGGER.error(String.valueOf(e));
            }
        }
    }

}
