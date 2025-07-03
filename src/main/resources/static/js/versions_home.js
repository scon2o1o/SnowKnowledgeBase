$(document).ready(function() {
    $('#company').selectize({
        sortField: 'text'
    });
});

var table = $('#dataTable').DataTable({
    "order": [
        [0, 'desc']
    ]
});

$('#company').on('change', function() {
    var selectedCompany = $(this).val();

    table.search('').columns().search('').draw();

    if (selectedCompany) {
        table.column(1).search(selectedCompany).draw();
    }
});

$("#menu-toggle").click(function(e) {
    e.preventDefault();

    $("#wrapper").toggleClass("toggled");
});