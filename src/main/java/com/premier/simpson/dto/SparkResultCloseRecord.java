package com.premier.simpson.dto;

import java.util.Date;

public class SparkResultCloseRecord {
    private Date date;
    private Double close;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    @Override
    public String toString() {
        return "SparkResultCloseRecord{" +
                "date=" + date +
                ", close=" + close +
                '}';
    }
}
