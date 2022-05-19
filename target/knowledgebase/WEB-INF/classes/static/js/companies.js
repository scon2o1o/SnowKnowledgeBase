var table = $('#dataTable').DataTable({
    "order": [
        [1, 'asc']
    ]
});

$("#menu-toggle").click(function(e) {
    e.preventDefault();

    $("#wrapper").toggleClass("toggled");
});