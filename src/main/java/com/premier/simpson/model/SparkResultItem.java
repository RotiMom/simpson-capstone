package com.premier.simpson.model;

import java.util.ArrayList;
import java.util.List;

public class SparkResultItem {
    private String symbol;
    private List<SparkResultCloseRecord> records = new ArrayList<>();

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<SparkResultCloseRecord> getRecords() {
        return records;
    }

    public void setRecords(List<SparkResultCloseRecord> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "SparkResultItem{" +
                "symbol='" + symbol + '\'' +
                ", records=" + records +
                '}';
    }
}
