$(document).ready(function() {
    $('select').selectize({
        sortField: 'text'
    });
});

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

$(document).ready(function() {
    $('[data-toggle="tooltip"]').tooltip();
});

document.getElementById('dateAdded').value = moment().format('DD/MM/YYYY');

document.getElementById('lastModified').value = moment().format('DD/MM/YYYY');

$(document).ready(function() {
    $('[data-toggle="tooltip"]').tooltip();
});

tinymce.init({
    selector: 'textarea',
    plugins: 'a11ychecker advcode casechange export formatpainter linkchecker autolink lists checklist media mediaembed pageembed permanentpen powerpaste table advtable tinycomments tinymcespellchecker',
    toolbar: 'a11ycheck addcomment showcomments casechange checklist code export formatpainter pageembed permanentpen table',
    toolbar_mode: 'floating',
    tinycomments_mode: 'embedded',
    tinycomments_author: 'Author name',
});