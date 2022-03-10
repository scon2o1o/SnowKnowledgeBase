$(document).ready(function() {
    $('select').selectize({
        sortField: 'text'
    });
});

$(document).ready(function() {
    $('#dataTable').DataTable({
        "order": [
            [0, 'desc']
        ]
    });
});

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

function sortDocTable(n) {
  var input, filter, table, tr, td, i, txtValue;
  input = n;
  filter = n;
  table = document.getElementById("dataTable");
  tr = table.getElementsByTagName("tr");
  for (i = 0; i < tr.length; i++) {
    td = tr[i].getElementsByTagName("td")[2];
    if (td) {
      txtValue = td.textContent || td.innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    }
  }
}