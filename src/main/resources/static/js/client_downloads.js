const numOfTables = document.getElementsByTagName("table").length;

function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}

function padTo2Digits(num) {
    return num.toString().padStart(2, '0');
}

function formatDate(date) {
    const d = new Date(date);
    return [
        padTo2Digits(d.getDate()),
        padTo2Digits(d.getMonth() + 1),
        d.getFullYear(),
    ].join('/') + ' ' + [padTo2Digits(d.getHours()),
        padTo2Digits(d.getMinutes()),
        padTo2Digits(d.getSeconds())
    ].join(':');;
}

for (let i = 1; i <= numOfTables; i++) {
  var oTable = document.getElementById('dataTable' + i);
  var rowLength = oTable.rows.length;
  for (k = 1; k < rowLength; k++) {
      var oCells = oTable.rows.item(k).cells;
      var cellLength = oCells.length;
      for (var j = 0; j < cellLength; j++) {
          if (j == 1) {
              oCells.item(j).innerHTML = formatBytes(oCells.item(j).innerHTML);
          }
          if (j == 2) {
              oCells.item(j).innerHTML = formatDate(oCells.item(j).innerHTML);
          }
      }
  }
}

$("#menu-toggle").click(function(e) {
    e.preventDefault();

    $("#wrapper").toggleClass("toggled");
});

$(document).ready(function() {
    $('[data-toggle="tooltip"]').tooltip();
});

function sortDownloadTable(n) {
    if (n==''){
        const collection = document.getElementsByTagName("table");
        for (let i = 0; i < collection.length; i++) {
          collection[i].style.display = "table";
        }
        const collection2 = document.getElementsByTagName("p");
        for (let i = 0; i < collection2.length; i++) {
                  collection2[i].style.display = "block";
                }
    } else {
        const collection = document.getElementsByTagName("table");
        for (let i = 0; i < collection.length; i++) {
            collection[i].style.display = "none";
        }
        collection[n - 1].style.display = "table";
        const collection2 = document.getElementsByTagName("p");
        for (let i = 0; i < collection2.length; i++) {
            collection2[i].style.display = "none";
        }
        collection2[n - 1].style.display = "block";
    }
}

