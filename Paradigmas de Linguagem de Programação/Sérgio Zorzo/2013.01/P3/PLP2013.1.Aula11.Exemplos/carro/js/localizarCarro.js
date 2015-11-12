window.onload = function() {
	document.getElementById("calcPreco").onclick = calcularPreco;				
}

function calcularPreco() {				
	var qtdPortas = document.getElementById("qtdPortas").value;
	var diasDeUso = document.getElementById("diasDeUso").value;
	var diaria = 0;
	var acrescimo = 0;
				
	if (qtdPortas > 2) {
		acrescimo = 10.0;
	}
				
	var valor = parseInt(document.getElementById("tipoCarro").value);

	switch ( valor ){
		case 1:
			diaria = 30.00;
			break;
		case 2:
			diaria = 40.00;
			break;
		case 3:
			diaria = 50.00;
			break;
		case 4:
			diaria = 60.00;
			break;						
	}

	if ( diaria > 0) {
		document.getElementById("valorTotal").value = (diaria*diasDeUso)+acrescimo;					
	} else {
		document.getElementById("valorTotal").clear;
	}
}
