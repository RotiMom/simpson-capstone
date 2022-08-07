const config = {
    type: 'line',
    data: {
        labels: [],
        datasets: []
    },
    options: {}
};

let dailyPrices = [];
let myChart = {};

function refreshData() {
    $('.refresh-btn').addClass('visually-hidden');
    $('.refresh-btn-spinner').removeClass('visually-hidden');

    $.ajax({
        method: "GET",
        url: "http://localhost:8080/refresh",
    }).always(function () {
        $('.refresh-btn').removeClass('visually-hidden');
        $('.refresh-btn-spinner').addClass('visually-hidden');
    });
}

function selectStock() {
    let selectedSymbol = $('#symbolList').find(':selected').text();
    let selectedSymbolId = $('#symbolList').find(':selected').prop('value');

     removeDataset(myChart);
     addDataset(myChart, selectedSymbol);

    dailyPrices.forEach(function(dailyPrice) {
        if(dailyPrice.symbolId == selectedSymbolId) {
            let dt = new Date(dailyPrice.closingDate);
            let dtDisplay = dt.toLocaleDateString('en-us', {year:"numeric", month:"short", day:"numeric"}) ;
             addData(myChart, dtDisplay, dailyPrice.price);
             myChart.update();
        }
    });
}

function addData(chart, label, data) {
    myChart.data.labels.push(label);
    myChart.data.datasets[0].data.push(data);
}

function addDataset(chart, symbol) {
    let newDataset = {
        label: symbol,
        backgroundColor: 'rgb(255, 99, 132)',
        borderColor: 'rgb(255, 99, 132)',
        data: [],
    };
    chart.data.datasets.push(newDataset);
}

function removeDataset(chart) {
    chart.data.labels = [];
    chart.data.datasets.pop();
    chart.update();
}

$(document).ready(function () {
    myChart = new Chart(document.getElementById('myChart'), config);

    $.ajax({
        method: "GET",
        url: "http://localhost:8080/symbols",
    }).done(function (data) {
        let tbody = document.getElementById("tbody");
        let select = document.getElementById("symbolList");
        data.forEach(function (item) {
            let row = document.createElement("tr");

            let symbolColumn = document.createElement("td");
            symbolColumn.innerText = item.symbol;
            row.append(symbolColumn);

            let sectorColumn = document.createElement("td");
            if (item.sector === 'X') {
                sectorColumn.innerText = "N/A"
            } else {
                sectorColumn.innerText = item.sector;
            }

            row.append(sectorColumn);

            let industryColumn = document.createElement("td");
            if (item.industry === 'X') {
                industryColumn.innerText = "N/A"
            } else {
                industryColumn.innerText = item.industry;
            }

            row.append(industryColumn);

            let cityColumn = document.createElement("td");
            if (item.city === 'X') {
                cityColumn.innerText = "N/A";
            } else {
                cityColumn.innerText = item.city;
            }

            row.append(cityColumn);

            let stateColumn = document.createElement("td");
            if (item.state === 'X') {
                stateColumn.innerText = "N/A";
            } else {
                stateColumn.innerText = item.state;
            }

            row.append(stateColumn);

            let zipColumn = document.createElement("td");
            if (item.zip === 'X') {
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

    $.ajax({
        method: "GET",
        url: "http://localhost:8080/daily-prices",
    }).done(function (data) {
        dailyPrices = data;
        $('#symbolList').trigger('change');
    });

});

