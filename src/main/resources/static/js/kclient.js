console.log("this is a test");

function addVoteListener(e) {
	const input = e.querySelector("input[type=range]");
	const numeric = e.querySelector(".numeric");
	const headers = {
		"Content-Type": "application/json",
		"X-CSRF-TOKEN": csrf.value
	};
	input.onchange = () => {
		numeric.innerText = input.value;
		fetch(e.action, {
			method: 'POST',
			headers: headers,
			body: JSON.stringify({value: input.value})
		}).then(response => console.log(response));
	};
}

function addQuestionListener(e) {
	const button = e.querySelector("button");
	const textarea = e.querySelector("textarea");
	const headers = {
		"Content-Type": "application/json",				
		"X-CSRF-TOKEN": csrf.value
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
			console.log(response)
		});
		return false;	
	}
}

function addQuestion(data) {
	const questionDiv = document.createElement("div");
	questionDiv.classList.add("question");
	questionDiv.id = "q_" + data.id;
	questionDiv.innerHTML = [
		'<form class="vote" th:action="/api/v/' + data.id + '" method="post">',
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
	document.querySelector(".question").parentElement
		.append(questionDiv);
	addVoteListener(questionDiv.querySelector(".vote"));
}

const ws = {
	socket: null,
	// llama a esto para enviar
	send: (text) => {
		if (ws.socket != null) {
			ws.socket.send(text);
		}
	},
	// sobreescribe esto para modificar cómo recibes cosas
	receive: (text) => {
		console.log(text);
	}
} 

function addWebSocketListener() {
	// socketUrl must be set prior to loading this script
	if (socketUrl !== 'false') {
		console.log("Connecting to WS '" + socketUrl + "'");
		ws.socket = new WebSocket(socketUrl);
		ws.socket.onmessage = (e) => ws.receive(e.data);
	}
}

window.addEventListener('load', () => {
	document.querySelectorAll(".vote").forEach(e => addVoteListener(e));
	document.querySelectorAll(".ask").forEach(e => addQuestionListener(e));
	addWebSocketListener();
});
