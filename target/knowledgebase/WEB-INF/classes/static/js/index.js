var table = $('#dataTable').DataTable();

function sortDocTable(n) {
    table.column(2).search(n, true, false).draw();
}

$("#menu-toggle").click(function(e) {
    e.preventDefault();

    $("#wrapper").toggleClass("toggled");
});