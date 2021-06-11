// $('#checkAll').click(function(e){
//     const table = $(e.target).closest('table');
//     $('td input:checkbox',table).prop('checked',this.checked);
// });
    function toggle(source) {
    checkboxes = document.getElementsByName('my-checkbox');
    for (var i = 0, n = checkboxes.length; i < n; i++) {
    checkboxes[i].checked = source.checked;
}
}
