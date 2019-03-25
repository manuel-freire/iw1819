"use strict"
$(function() {
	
	if($("#msg").text() && $("#msg").text() != "") {
		$("#msgModal").modal("toggle");
	}
	
	$("#msg").change(function() {
		$("#msgModal").modal("toggle");
	});
	
	/**
	 * WebSocket API, which only works once initialized
	 */
	const ws = {
			
		/**
		 * WebSocket, or null if none connected
		 */
		socket: null,
		
		/**
		 * Sends a string to the server via the websocket.
		 * @param {string} text to send 
		 * @returns nothing
		 */
		send: (text) => {
			if (ws.socket != null) {
				ws.socket.send(text);
			}
		},

		/**
		 * Default action when text is received. 
		 * @returns
		 */
		receive: (text) => {
			console.log(text);
		},
		
		/**
		 * Attempts to establish communication with the specified
		 * web-socket endpoint. If successfull, will call 
		 * @returns
		 */
		initialize: (endpoint) => {
			try {
				ws.socket = new WebSocket(endpoint);
				ws.socket.onmessage = (e) => ws.receive(e.data);
				console.log("Connected to WS '" + endpoint + "'")
			} catch (e) {
				console.log("Error, connection to WS '" + endpoint + "' FAILED: ", e);
			}
		}
	} 
	
	if (m3.socketUrl !== false) {
		ws.initialize(m3.socketUrl);
	}
	
});