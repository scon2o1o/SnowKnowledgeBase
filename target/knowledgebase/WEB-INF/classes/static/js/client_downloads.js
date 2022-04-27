function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}

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
        if (j == 1) {
            oCells.item(j).innerHTML = formatBytes(oCells.item(j).innerHTML);
        }
        if (j == 2) {
            oCells.item(j).innerHTML = formatDate(oCells.item(j).innerHTML);
        }
    }
}

var table = $('#dataTable').DataTable({
    "order": [
        [2, 'desc']
    ]
});

$("#menu-toggle").click(function(e) {
    e.preventDefault();

    $("#wrapper").toggleClass("toggled");
});

$(document).ready(function() {
    $('[data-toggle="tooltip"]').tooltip();
});

function sortDownloadTable(n) {
    table.column(3).search(n, true, false).draw();
}