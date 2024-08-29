document.addEventListener("DOMContentLoaded", function() {
    var tbody = document.getElementById('jang');
    var rows = tbody.getElementsByTagName('tr');

    var dates = [];
    var counts = [];

    for (var i = 0; i < rows.length; i++) {
        var cells = rows[i].getElementsByTagName('td');
        dates.push(cells[0].textContent.trim());
        counts.push(parseInt(cells[1].textContent.trim()));
    }

    var ctx = document.getElementById('visitChart').getContext('2d');
    var chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: dates,
            datasets: [{
                label: 'Visitor Count',
                data: counts,
                borderColor: 'rgb(75, 192, 192)',
                fill: false
            }]
        },
        options: {
            responsive: true,
            scales: {
                x: {
                    beginAtZero: true
                },
                y: {
                    beginAtZero: true
                }
            }
        }
    });
});