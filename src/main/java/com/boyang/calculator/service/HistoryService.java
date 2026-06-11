package com.boyang.calculator.service;

import com.boyang.calculator.model.CalculationRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 历史记录服务，当前版本仅使用内存保存记录。
 */
public class HistoryService {

    private final List<CalculationRecord> records = new ArrayList<>();

    /**
     * 添加一条历史记录。
     */
    public void addRecord(CalculationRecord record) {
        records.add(record);
    }

    /**
     * 删除指定历史记录。
     */
    public void removeRecord(CalculationRecord record) {
        records.remove(record);
    }

    /**
     * 清空全部历史记录。
     */
    public void clearRecords() {
        records.clear();
    }

    /**
     * 获取只读历史记录列表。
     */
    public List<CalculationRecord> getRecords() {
        return Collections.unmodifiableList(records);
    }
}
