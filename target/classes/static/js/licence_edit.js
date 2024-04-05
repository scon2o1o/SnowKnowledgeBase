$(document).ready(function() {
    $('select').selectize({
        sortField: 'text'
    });
});

$("#menu-toggle").click(function(e) {
    e.preventDefault();

    $("#wrapper").toggleClass("toggled");
});