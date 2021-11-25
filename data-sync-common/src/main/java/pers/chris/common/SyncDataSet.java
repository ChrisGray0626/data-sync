package pers.chris.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 同步数据集
 */
public class SyncDataSet {

    private List<Map<String, String>> rows;

    public SyncDataSet() {
        rows = new LinkedList<>();
    }

    public List<Map<String, String>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, String>> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "SyncData{" +
                "rows=" + rows +
                '}';
    }

}
