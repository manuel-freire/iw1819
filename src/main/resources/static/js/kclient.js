/**
 * Main Karmometro script. Simplifies calling the server-side API.
 */


/**
 * Posts a simple message to the server. Purely for testing JS integration
 */
function testServerSideJs() {
	const headers = {
		"Content-Type": "application/json",				
		"X-CSRF-TOKEN": km.csrf.value
	};	
	const body = JSON.stringify({json: "hola mundo"});
	console.log("asking ", body);
	fetch("/api/e", {
		method: 'POST',
		headers: headers,
		body: body				
	}).then(response => {
		console.log(response)
	});
}

/**
 * Replaces dates with relative deltas to the current moment.
 * @param {Element} e, an element with a data-timestamp attribute
 * 		representing nanoseconds since the unich epoch
 */
function prettyDelta(e) {
	const now = Number(new Date()),
	 	  then = e.dataset.timestamp;
	let diff = now - then;
	const days =  Math.floor(diff / (24 * 60 * 60 * 1000)), 	
	 	  hours = Math.floor(diff / (60 * 60 * 1000)) % 24, 
	 	  mins =  Math.floor(diff / (60 * 1000)) % 60, 
	 	  secs =  Math.floor(diff / (1000)) % 60;
	let result = ["hace "];
	if (days > 0)  result.push("" + days + " d");
	if (hours > 0) result.push("" + hours + " h");
	if (mins > 0)  result.push("" + mins + " m");
	if (secs > 0)  result.push("" + secs+ " s");
	e.innerText = result.length > 1 ? result.join(" ") : "ahora";
}

/**
 * Listens to the specified form, and posts to the vote API when
 * its value changes.
 * 
 * @param {Element} e, a form to listen to  
 * @returns nothing 
 */
function addVoteListener(e) {
	const input = e.querySelector("input[type=range]");
	const numeric = e.querySelector(".numeric");
	const headers = {
		"Content-Type": "application/json",
		"X-CSRF-TOKEN": km.csrf.value
	};
	input.onchange = () => {
		numeric.innerText = input.value;
		fetch(e.action, {
			method: 'POST',
			headers: headers,
			body: JSON.stringify({value: input.value})
		}).then(response => console.log(response));
	};
	const button = e.querySelector(".delq");
	if (button) {
		button.onclick = () => {
			const target = e.action.replace(/\/v\//, '/d/')
			console.log("removing q: ", target);
			fetch(target, {
				method: 'POST',
				headers: headers,
			}).then(response => console.log(response));
			return false;
		}
	}
}

/**
 * Listens to the specified form, and posts new questions when they 
 * are submitted.
 * 
 * @param {Element} e, a form to listen to  
 * @returns nothing 
 */
function addQuestionListener(e) {
	const button = e.querySelector("button");
	const textarea = e.querySelector("textarea");
	const headers = {
		"Content-Type": "application/json",				
		"X-CSRF-TOKEN": km.csrf.value
	};
	e.onsubmit = () => {
		const body = JSON.stringify({
			text: textarea.value, 
			poll: document.getElementById('poll_t').checked ? 'true' : 'false' 
		});
		console.log("asking ", body);
		fetch(e.action, {
			method: 'POST',
			headers: headers,
			body: body				
		}).then(response => {
			e.reset();
			console.log(response)
		});
		return false;	
	}
}

/**
 * Creates a new voting form based on a question's data. Also
 * appends it wherever it should go.
 * 
 * @param data (as returned by the server after adding a question)
 * @returns nothing
 */
function addQuestion(data) {
	const questionDiv = document.createElement("div");
	const canDelete = (data.author.id == km.userId) || km.admin;
	const deleteButton = canDelete ?
		'	<button class="delq">🗑</button>' :
		'	<!-- cannot delete -->';
	console.log(deleteButton, canDelete, data.author.id, km.userId, km.admin);
	questionDiv.classList.add("question");
	questionDiv.id = "q_" + data.id;
	questionDiv.innerHTML = [
		'<form class="vote" action="' + km.voteApiUrl + data.id + '" method="post">',
		'	<div class="metadata">',
		'		<span class="delta" data-timestamp="' + Number(new Date(data.time)) + '">??</span>',
		'		<img class="userthumb" alt="' + data.author.login + '" ', 
		'			 src="/user/' + data.author.id + '/photo">',
		'	</div>',
		'	<div class="bars">',
		'	<div class="me">',
		'		<span class="barlabel">👤</span>',
		'		<input type="range" name="vote" min="0" max="100" value="0"/>',
		'		<span class="numeric">??</span>',
		'	</div>',
		'	<div class="others">',
		'		<span class="barlabel">👥 ×0</span>',
		'		<input disabled type="range" min="0" max="100" value="0"/>',
		'		<span class="numeric">??</span>',
		'	</div>',
		'	</div>',
		'	<span class="qtext">' + data.text + '</span>',
		deleteButton,
		'</form>'].join('\n');	
	document.querySelector(data.poll ? ".polls" : ".questions").append(questionDiv);
	addVoteListener(questionDiv.querySelector(".vote"));
}

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

/**
 * Actions to perform once the page is fully loaded
 */
window.addEventListener('load', () => {
	document.querySelectorAll(".vote").forEach(e => addVoteListener(e));
	document.querySelectorAll(".ask").forEach(e => addQuestionListener(e));
	if (km.socketUrl !== false) {
		ws.initialize(km.socketUrl);
	}
	window.setInterval(() => {
		document.querySelectorAll(".delta").forEach(e => prettyDelta(e));
	}, 5000);
});
