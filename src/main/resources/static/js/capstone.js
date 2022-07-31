const labels = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
];

const data = {
    labels: labels,
    datasets: [{
        label: 'Hard-coded sample dataset',
        backgroundColor: 'rgb(255, 99, 132)',
        borderColor: 'rgb(255, 99, 132)',
        data: [0, 10, 5, 2, 20, 30, 45],
    }]
};

const config = {
    type: 'line',
    data: data,
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

$(document).ready(function () {
    myChart = new Chart(
        document.getElementById('myChart'),
        config
    );

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

    $.ajax({
        method: "GET",
        url: "http://localhost:8080/daily-prices",
    }).done(function (data) {
       dailyPrices = data;
    });

});

