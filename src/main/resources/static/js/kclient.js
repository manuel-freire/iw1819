/**
 * Main Karmometro script. Simplifies calling the server-side API.
 */


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
	console.log("patched", e);
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
	questionDiv.classList.add("question");
	questionDiv.id = "q_" + data.id;
	questionDiv.innerHTML = [
		'<form class="vote" action="' + km.voteApiUrl + data.id + '" method="post">',
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
		console.log("Connecting to WS '" + endpoint + "'...");
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
});
