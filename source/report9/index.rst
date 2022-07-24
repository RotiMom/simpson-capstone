Report 9
========

| Author: Andrya Carter
| Posted: 7/25/2022
| Report 9: Dynamic Data
| Hours: 10

This report covers activities performed between 7/19/22 – 7/25/22. See Summary
at the bottom for high level overview of task performed.

.. contents::

Model
-----
The goal for this week was to expose the symbols data and populate the webpage with
the data from the database. The first step was to create a model Java object
which would hold the data from the symbols table.

.. code-block:: java

    public class Symbol {
        private Long id;
        private String symbol;
        private String sector;
        private String industry;
        private String city;
        private String state;
        private String zip;
    }

Rest Controller
---------------
After the model was created, I could add another method to the **RestController**
which would return all of the symbol data. My research showed me that when using
the **jdbcTemplate** the developer needs to specify a **RowMapper** class which will
take the data returned from the SQL call and populates the Java object.  Further
research showed that there is a **BeanPropertyMapper** which will accomplish the
mapping if the Java model has the same field names as what is in the database.
https://mkyong.com/spring/spring-jdbctemplate-querying-examples/

.. code-block:: java

    @GetMapping(value = "/symbols")
    public List<Symbol> getSymbols() {
        List<Symbol> query = jdbcTemplate.query("SELECT * FROM SYMBOLS", new BeanPropertyRowMapper(Symbol.class));
        return query;
    }

AJAX
----
After the data was available from the RestController, the next step was to add the Javascript code to call the new method
and populate a couple HTML elements.  The first element is the table at the bottom of the page and the next is the select
box near the chart which will be used to swap the data shown in the chart.

This ended up being harder than I had expected.  I found several examples of how to make the AJAX call using JQuery but
I was unable to get the examples for updating the HTML using JQuery to work.  In my CIS 320 - Advanced Web Development & Security
course we used regular Javascript to update the HTML, I decided to use that code to perform this task.

.. code-block:: javascript

 $.ajax({
        method: "GET",
        url: "http://localhost:8080/symbols",
    }).done(function (data) {
        let tbody = document.getElementById("tbody");
        let select = document.getElementById("symbolList");
        data.forEach(function(item) {
           let row = document.createElement("tr");

            let symbolColumn = document.createElement("td");
            symbolColumn.innerText = item.symbol;
            row.append(symbolColumn);

            let sectorColumn = document.createElement("td");
            if(item.sector === 'X') {
                sectorColumn.innerText = "N/A"
            } else {
                sectorColumn.innerText = item.sector;
            }

            row.append(sectorColumn);

            let industryColumn = document.createElement("td");
            if(item.industry === 'X') {
                industryColumn.innerText = "N/A"
            } else {
                industryColumn.innerText = item.industry;
            }

            row.append(industryColumn);

            let cityColumn = document.createElement("td");
            if(item.city === 'X') {
                cityColumn.innerText = "N/A";
            } else {
                cityColumn.innerText = item.city;
            }

            row.append(cityColumn);

            let stateColumn = document.createElement("td");
            if(item.state === 'X') {
                stateColumn.innerText = "N/A";
            } else {
                stateColumn.innerText = item.state;
            }

            row.append(stateColumn);

            let zipColumn = document.createElement("td");
            if(item.zip === 'X') {
                zipColumn.innerText = "N/A";
            } else {
                zipColumn.innerText = item.zip;
            }

            row.append(zipColumn);
            tbody.append(row);

            let option = document.createElement("option");
            option.value = item.id;
            option.innerText = item.symbol;
            select.append(option);
        });

    });

Summary
-------
The work done for this week includes return the data from the database using a RestController
and then using Javascript to display that data in the HTML.

**Next Steps**

* Have the Refresh Data button call the refresh endpoint
* Create RestController endpoint for retrieving DAILY_PRICE data
* Make AJAX call to get DAILY_PRICE data
* Add Javascript code to swap the graph when a new symbol is selected in the drop down

Hours
-----
* RestController - 1 hours
* AJAX - 5 hours
* Report - 2 hours
* Summary Including Documentation - 2 hours