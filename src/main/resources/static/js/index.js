var table = $('#dataTable').DataTable({
    "order": [
        [0, 'desc']
    ]
});

function sortDocTable(n) {
    table.column(2).search(n, true, false).draw();
}

$("#menu-toggle").click(function(e) {
    e.preventDefault();

    $("#wrapper").toggleClass("toggled");
});