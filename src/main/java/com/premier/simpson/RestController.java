package com.premier.simpson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premier.simpson.model.SparkResult;
import com.premier.simpson.model.SparkResultCloseRecord;
import com.premier.simpson.model.SparkResultItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping(value = "/refresh")
    private SparkResult refresh() throws Exception {

        updateQuoteSummaryFromWebsite();

        Map<String, Map> response = getSparkDataFromWebsite();

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

        jdbcTemplate.update("delete from DAILY_PRICES");

        for (SparkResultItem item : result.getItems()) {
            String query = "select id from SYMBOLS where symbol = ?";
            Integer symbolId = jdbcTemplate.queryForObject(query, new Object[]{item.getSymbol()}, Integer.class);

            for (SparkResultCloseRecord record : item.getRecords()) {
                String updateSql = "insert into DAILY_PRICES (SYMBOL_ID, CLOSING_DATE, PRICE) VALUES (?, ?, ?)";
                jdbcTemplate.update(updateSql, symbolId, record.getDate(), record.getClose());
            }
        }
        return result;
    }

    private Map getSparkDataFromJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        URL sparkResource = SimpsonApp.class.getClassLoader().getResource("spark-response.json");
        Map<String, Map> response = objectMapper.readValue(sparkResource, Map.class);
        return response;
    }

    private Map getSparkDataFromWebsite() {
        List<String> symbols = jdbcTemplate.queryForList("select SYMBOL from SYMBOLS", String.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "application/json");
        headers.add("X-API-KEY", "7ytjXDgTRMiP9vbcW5Lr7ACrlnjRvZ82Ngv2pmoh");
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        String payload = StringUtils.collectionToDelimitedString(symbols, ",");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange("https://yfapi.net/v8/finance/spark?interval=1d&range=1y&symbols=" + payload, HttpMethod.GET, entity, Map.class);
        return response.getBody();
    }

    private void updateQuoteSummaryFromWebsite() {
        List<Map<String, Object>> results = jdbcTemplate.queryForList("select ID, SYMBOL from SYMBOLS where SECTOR is null");

        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "application/json");
        headers.add("X-API-KEY", "7ytjXDgTRMiP9vbcW5Lr7ACrlnjRvZ82Ngv2pmoh");
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        for (Map<String, Object> item : results) {
            Integer id = (Integer) item.get("ID");
            String symbol = (String) item.get("SYMBOL");
            ResponseEntity<Map> response = restTemplate.exchange("https://yfapi.net/v11/finance/quoteSummary/" + symbol + "?lang=en&region=US&modules=assetProfile", HttpMethod.GET, entity, Map.class);

            Map payload = response.getBody();
            Map quoteSummary = (Map) payload.get("quoteSummary");
            List result = (List) quoteSummary.get("result");

            Map assetProfile = new HashMap();
            if(result != null) {
                assetProfile = (Map) ((Map) result.get(0)).get("assetProfile");
            }

            String sector = (String) assetProfile.get("sector");
            if(sector == null) {
                sector = "X";
            }

            String industry = (String) assetProfile.get("industry");
            if(industry == null) {
                industry = "X";
            }

            String city = (String) assetProfile.get("city");
            if(city == null) {
                city = "X";
            }

            String state = (String) assetProfile.get("state");
            if(state == null) {
                state = "X";
            }

            String zip = (String) assetProfile.get("zip");
            if(zip == null) {
                zip = "X";
            }

            jdbcTemplate.update("update SYMBOLS set SECTOR = ?, INDUSTRY = ?, CITY = ?, STATE = ?, ZIP = ? where ID = ?"
                    , sector, industry, city, state, zip, id);
        }
    }
}
