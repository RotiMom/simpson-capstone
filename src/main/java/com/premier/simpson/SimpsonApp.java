package com.premier.simpson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premier.simpson.model.SparkResult;
import com.premier.simpson.model.SparkResultCloseRecord;
import com.premier.simpson.model.SparkResultItem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SimpsonApp {
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        URL sparkResource = SimpsonApp.class.getClassLoader().getResource("spark-response.json");
        Map<String, Map> response = objectMapper.readValue(sparkResource, Map.class);

        SparkResult result = new SparkResult();
        for (Map.Entry<String, Map> entry : response.entrySet()) {
            SparkResultItem item = new SparkResultItem();
            item.setSymbol(entry.getKey());

            List<Integer> timestamps = (ArrayList<Integer>) entry.getValue().get("timestamp");
            List<Double> closes = (ArrayList<Double>) entry.getValue().get("close");

            for( int i = 0; i < timestamps.size(); i++) {
                Integer timestamp = timestamps.get(i);
                Object close = closes.get(i);

                SparkResultCloseRecord record = new SparkResultCloseRecord();
                if(close instanceof Integer) {
                    record.setClose(Double.valueOf((Integer)close));
                }

                if(close instanceof Double) {
                    record.setClose((Double)close);
                }

                Date date = new Date(timestamp * 1000L); // multiply by 1000L because api is unix time not java time
                record.setDate(date);
                item.getRecords().add(record);
            }
            result.getItems().add(item);
        }

        System.out.println(result);


    }
}
