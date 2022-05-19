var table = $('#dataTable').DataTable({
    "order": [
        [0, 'asc']
    ]
});

$("#menu-toggle").click(function(e) {
    e.preventDefault();

    $("#wrapper").toggleClass("toggled");
});