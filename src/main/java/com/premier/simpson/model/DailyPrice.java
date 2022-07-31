package com.premier.simpson.model;

import java.util.Date;

public class DailyPrice {
    private Long id;
    private Long symbolId;
    private Date closingDate;
    private Double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(Long symbolId) {
        this.symbolId = symbolId;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
