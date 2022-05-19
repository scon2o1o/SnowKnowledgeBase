var table = $('#dataTable').DataTable({
    "order": [
        [2, 'asc']
    ]
});

$("#menu-toggle").click(function(e) {
    e.preventDefault();

    $("#wrapper").toggleClass("toggled");
});