package com.premier.simpson.model;

import java.util.ArrayList;
import java.util.List;

public class SparkResult {
    List<SparkResultItem> items = new ArrayList<>();

    public List<SparkResultItem> getItems() {
        return items;
    }

    public void setItems(List<SparkResultItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "SparkResult{" +
                "items=" + items +
                '}';
    }
}
