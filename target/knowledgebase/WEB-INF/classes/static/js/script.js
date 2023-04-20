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
	plugins: 'preview importcss searchreplace autolink autosave save directionality code visualblocks visualchars fullscreen image link media template codesample table charmap pagebreak nonbreaking anchor insertdatetime advlist lists wordcount help charmap quickbars emoticons autolink lists media table anchor autolink autoresize autosave charmap code codesample directionality emoticons fullscreen help image importcss insertdatetime link lists media nonbreaking pagebreak preview quickbars save searchreplace template visualblocks visualchars wordcount',
	toolbar: 'undo redo | bold italic underline strikethrough | fontfamily fontsize blocks | alignleft aligncenter alignright alignjustify | outdent indent |  numlist bullist | forecolor backcolor removeformat | pagebreak | charmap emoticons | fullscreen  preview save print | insertfile image media template link anchor codesample | ltr rtl',
	toolbar_sticky: true,
	toolbar_mode: 'floating',
	tinycomments_mode: 'embedded',
	//tinycomments_author: 'Author name',
});