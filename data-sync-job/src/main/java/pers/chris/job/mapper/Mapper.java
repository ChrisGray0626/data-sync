package pers.chris.job.mapper;

import pers.chris.common.exception.MapperException;
import pers.chris.common.model.MapperConfBO;
import pers.chris.common.util.FieldUtil;
import pers.chris.job.base.mapper.BaseMapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Mapper extends BaseMapper {

    private final List<MapperUnit> mapperUnits;

    public Mapper (List<MapperConfBO> fieldMapConfs) {
        mapperUnits = new LinkedList<>();

        for (MapperConfBO fieldMapConf: fieldMapConfs) {
            try {
                MapperUnit mapperUnit = new MapperUnit(fieldMapConf.rule);
                mapperUnits.add(mapperUnit);
            } catch (MapperException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void run() {
        for (Map<String, String> row: syncDataSet.getRows()) {
            for (MapperUnit mapperUnit : mapperUnits) {
                mapperUnit.run(row);
            }
        }
    }

    @Override
    public void start() {
        checkField();
        run();
    }

    private void checkField() {
        for (MapperUnit mapperUnit : mapperUnits) {
            List<String> dstFieldNames = mapperUnit.getDstFieldNames();
            List<String> srcFieldNames = mapperUnit.getSrcFieldNames();

            try {
                FieldUtil.checkFieldName(dstFieldNames, srcFieldNames,
                        new ArrayList<>(writerFields.keySet()), new ArrayList<>(readerFields.keySet()));
                FieldUtil.checkFieldType(dstFieldNames, srcFieldNames, writerFields, readerFields);
            } catch (MapperException e) {
                e.printStackTrace();
            }
        }
    }

}
