Report 8
========

| Author: Andrya Carter
| Posted: 7/18/2022
| Report 8: Null Pointer
| Hours: 10

This report covers activities performed between 7/12/22 – 7/18/22. See Summary
at the bottom for high level overview of task performed.

.. contents::

Null Pointer
------------

There was a defect in the previous code where when attempting to read the
**assetProfile** information from the **quoteSummary** there would be an error.

.. code-block:: java

    List result = (List) quoteSummary.get("result");
    Map assetProfile = (Map) ((Map) result.get(0)).get("assetProfile");

The code assumed there would also be a result section and assetProfile section.
However, in some cases where the quoteSummary endpoint does not return data the
result is actually *null* i.e. in the case of Pet Smart (PETM).

.. code-block:: json

    {
      "quoteSummary": {
        "result": null,
        "error": {
          "code": "Not Found",
          "description": "No fundamentals data found for any of the summaryTypes=assetProfile"
        }
      }
    }


I added the below code which will allow the program to finish running even when
the sector information is missing for the stock symbol.  This code will initialize an
empty Map object when result is null.

.. code-block:: java

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

If the only thing the code change did was to handle the null, there would be a
downside of attempting to call the *quoteSummary* endpoint on every refresh and
that call is performed for each symbol without *sector* information.  Therefore,
I introduced a placeholder value "X" which is saved into the fields to prevent
repeat calls to retrieve information which is known to not exist for the particular
stock symbol.  This placeholder can be changed to some other value when displayed
on the webpage.

Summary
-------

The work done for this week and this report fixes the issue of the program
sending an error code when sector information is missing and instead allows the
program to finish.

**Next Steps**

* Have the Refresh Data button call the refresh endpoint
* Create RestController endpoint for retrieving SYMBOLS data
* Make AJAX call to get SYMBOLS data and update the webpage with the data
* Create RestController endpoint for retrieving DAILY_PRICE data
* Make AJAX call to get DAILY_PRICE data
* Add Javascript code to swap the graph when a new symbol is selected in the drop down

Hours
-----
* NullPointer - 2 hours
* Report - 6 hours
* Summary Including Documentation - 2 hours