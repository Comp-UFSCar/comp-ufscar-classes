function init() {
	alert("Teste script externo!");
	document.write("Script externo - o JS que escreveu essa linha");
}

window.onload = init();