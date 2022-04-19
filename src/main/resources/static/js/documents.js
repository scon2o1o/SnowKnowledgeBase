$("#menu-toggle").click(function(e) {
    e.preventDefault();

    $("#wrapper").toggleClass("toggled");
});

function padTo2Digits(num) {
    return num.toString().padStart(2, '0');
}

function formatDate(date) {
    const d = new Date(date);
    return [
        padTo2Digits(d.getDate()),
        padTo2Digits(d.getMonth() + 1),
        d.getFullYear(),
    ].join('/') + ' ' + [padTo2Digits(d.getHours()),
        padTo2Digits(d.getMinutes()),
        padTo2Digits(d.getSeconds())
    ].join(':');;
}

var oTable = document.getElementById('dataTable');
var rowLength = oTable.rows.length;
for (i = 1; i < rowLength; i++) {
    var oCells = oTable.rows.item(i).cells;
    var cellLength = oCells.length;
    for (var j = 0; j < cellLength; j++) {
        if (j == 5 || j == 6) {
            oCells.item(j).innerHTML = formatDate(oCells.item(j).innerHTML);
        }
    }
}

var table = $('#dataTable').DataTable({
    "order": [
        [0, 'desc']
    ]
});