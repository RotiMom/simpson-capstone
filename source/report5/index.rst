Report 5
========

| Author: Andrya Carter
| Posted: 6/27/2022
| Report 5: JDBC Template
| Hours: 11

This report covers activities performed between 6/20/22 – 6/27/22. See Summary
at the bottom for high level overview of task performed.

.. contents::


Spring Boot Application
-----------------------

I converted my Java application into a Spring Boot web application using the following
website as a guide: https://www.baeldung.com/spring-boot-start.  This required
adding an annotation and changing my main method.

.. code-block:: java

    @SpringBootApplication
    public class SimpsonApp {
        public static void main(String[] args) throws IOException {
            SpringApplication.run(SimpsonApp.class, args);
        }
    }

Rest Controller
---------------

Since the Spring Boot application required changing my main method.  I needed to
place the existing code somewhere.  I think in the long-term it would be useful
to have a button in the webpage that would allow for refreshing the dataset.  Therefore,
I created a controller called RestController which has a method called refresh.

.. code-block:: java

    @org.springframework.web.bind.annotation.RestController
    public class RestController {

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @GetMapping(value = "/refresh")
        private SparkResult refresh() throws IOException {
            // existing code from report4
        }
    }

Setting up Data Source
----------------------

In order to connect to the database for loading data, I needed to setup a Java
Data Source. The following website provided documentation on how to create a
Data Source inside of a Spring application: https://www.baeldung.com/spring-jdbc-jdbctemplate

.. code-block:: java

    @Bean
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://192.168.1.14:3306/simpson_capstone");
        dataSource.setUsername("simpsoncs");
        dataSource.setPassword("simpsoncspass");
        return dataSource;
    }

Using JDBC Template
-------------------

The existing code from report4, creates the SparkResult object.  For populating the
data into the database, I looped over the items collection in the SparkResult.
For each date and closing price in the SparkResultItem, I inserted a row into
the DAILY_PRICES table.

To allow the code to run multiple times, I also added a delete statement to clean
up the existing data before the inserts run.

The current code does require manual inserts into the SYMBOLS table for each of
the symbols in the JSON data file.

.. code-block:: sql

    INSERT INTO `simpson_capstone`.`SYMBOLS`(`ID`, `SYMBOL`) VALUES (1, 'GOOG');
    INSERT INTO `simpson_capstone`.`SYMBOLS`(`ID`, `SYMBOL`) VALUES (2, 'AAPL');
    INSERT INTO `simpson_capstone`.`SYMBOLS`(`ID`, `SYMBOL`) VALUES (3, 'WFC');
    INSERT INTO `simpson_capstone`.`SYMBOLS`(`ID`, `SYMBOL`) VALUES (4, 'BRK-B');
    INSERT INTO `simpson_capstone`.`SYMBOLS`(`ID`, `SYMBOL`) VALUES (5, 'PETM');
    INSERT INTO `simpson_capstone`.`SYMBOLS`(`ID`, `SYMBOL`) VALUES (6, 'HD');
    INSERT INTO `simpson_capstone`.`SYMBOLS`(`ID`, `SYMBOL`) VALUES (7, 'WMT');

.. code-block:: java

    jdbcTemplate.update("delete from DAILY_PRICES");

    for (SparkResultItem item : result.getItems()) {
        String query = "select id from SYMBOLS where symbol = ?";
        Integer symbolId = jdbcTemplate.queryForObject(query, new Object[]{item.getSymbol()}, Integer.class);

        for (SparkResultCloseRecord record : item.getRecords()) {
            String updateSql = "insert into DAILY_PRICES (SYMBOL_ID, CLOSING_DATE, PRICE) VALUES (?, ?, ?)";
            jdbcTemplate.update(updateSql, symbolId, record.getDate(), record.getClose());
        }
    }

Summary
-------
In this report, the application now loads the data file into the database and I
have created a RestController that is used to trigger the import of data by going
to the url http://localhost:8080/refresh.

**Next Steps**

* Updating the source of closing prices to be the YahooFinanceApi
* Adding another call to YahooFinanceApi to retrieve company information
* Begin work on HTML page
* Integrate the graphing Javascript library
* Use AJAX to pull in data from RestController to be used in graphing library

Hours
-----
* Spring Boot Application - 2 hours
* Rest Controller - 2 hours
* Data Source - 2 hours
* Database Inserts - 3 hours
* Summary Including Documentation - 2 hours