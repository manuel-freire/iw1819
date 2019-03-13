console.log("this is a test");

function valueChanged(action, headers, input, numeric) {
	return () => {
		numeric.innerText = input.value;
		fetch(action, {
			method: 'POST',
			headers: headers,
			body: JSON.stringify({value: input.value})
		}).then(response => console.log(response));
	};
}

function addVoteListener() {
	document.querySelectorAll(".vote").forEach(e => {
		const input = e.querySelector("input[type=range]");
		const numeric = e.querySelector(".numeric");
		const csrf = e.querySelector(".csrf");
		const headers = {
			"Content-Type": "application/json",
			"X-CSRF-TOKEN": csrf.value
		};
		input.onchange = valueChanged(e.action, headers, input, numeric)
	});
}

window.addEventListener('load', addVoteListener);