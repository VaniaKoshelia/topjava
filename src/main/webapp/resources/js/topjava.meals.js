// $(document).ready(function () {
function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: "ajax/meals/filter",
        data: $("#filter").serialize()
    }).done(updateTableByData)
}
function clearFilter() {
    $("#filter")[0].reset();
    $.get("ajax/meals", updateTableByData);
}
$(function () {
    makeEditable({
            ajaxUrl: "ajax/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            }),
        updateTable:updateFilteredTable
        }
    );
});