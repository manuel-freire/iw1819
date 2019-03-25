"use strict"
$(function() {
    $('#userTable').DataTable({
        select: true,
    });
    
    $("#delete-selected").click(function() {
    	let userIdsToDelete = [];
    	let rows = $('#userTable').find("tr.selected");
    	for(let i = 0; i < rows.length; i++) {
    		userIdsToDelete.push($(rows[i]).attr("id"));
    	}
    	
    	if(userIdsToDelete && userIdsToDelete.length > 0) {
        	$.ajax({
    			type: "POST",
    			url: "/delete-users",
    			headers: {
    				"Content-Type": "application/json",				
    				"X-CSRF-TOKEN": km.csrf.value
    			},
    			data: JSON.stringify(userIdsToDelete),
    			success: function() {
    				console.log("users deleted");
    			},
    		});
    	}
    });
});