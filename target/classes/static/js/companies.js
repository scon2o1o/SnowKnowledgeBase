var table = $('#dataTable').DataTable({
    "order": [
        [0, 'asc']
    ]
});

function sortCompanyTable(n) {
    if (n == '') {
        table.column(5).search(n, false, false, true).draw();
    } else if (n == 'null') {
        table.column(5).search("^" + '' + "$", true, false).draw();
    } else {
        table.column(5).search("^" + n + "$", true, false).draw();
    }
}

$("#menu-toggle").click(function(e) {
    e.preventDefault();

    $("#wrapper").toggleClass("toggled");
});