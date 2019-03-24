"use strict"
$(function() {
	
	if($("#msg").text() && $("#msg").text() != "") {
		$("#msgModal").modal("toggle");
	}
	
	$("#msg").change(function() {
		$("#msgModal").modal("toggle");
	});
	
	
});