var table = $('#dataTable').DataTable({
    "order": [
        [0, 'desc']
    ]
});

$("#menu-toggle").click(function(e) {
    e.preventDefault();

    $("#wrapper").toggleClass("toggled");
});