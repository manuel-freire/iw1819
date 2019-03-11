$(function() {

    $("#view-list").click(function() {
        $("#action-button-container").removeClass("d-none");
    });

    $("#view-files").click(function() {
        $("#action-button-container").addClass("d-none");
    });

    $('#fileTable').DataTable({
        select: true,
    });

    $("#edit-folder-view").click(function() {
        console.log("folderView "+(($("#folderView").is(":visible")) ? "visible" : "hidden"));
        if($("#folderView").is(":visible")) {
            console.log("action-button-container "+(($("#action-button-container").is(":visible")) ? "visible" : "hidden"));
            if(($("#action-button-container").is(":visible"))) {
                $("#action-button-container").addClass("d-none");
            }
            else {
                $("#action-button-container").removeClass("d-none");
            }
        }
    });

});