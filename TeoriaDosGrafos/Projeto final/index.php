<?php

define('AbsPath', '/~lucasdavid/');

// decriptando URL, metodos e atributos
$url = isset($_GET['url']) ? $_GET['url'] : 'index';
$url = explode('/', $url);

$allowedURLs = array('', 'index', 'documentacao', 'referencias', 'erro');

?>
<!DOCTYPE html>
<html>
    <head>
        <title>Teoria dos Grafos</title>
        <meta charset="ISO-8859-1">
        <link rel="shortcut icon" type="image/x-icon" href="img/page-icon.png" />

        <link href="css/libs/bootstrap.min.css" rel="stylesheet" media="screen">
        <link href="css/default.css" rel="stylesheet" media="screen">
        <link href="css/grafo.css" type="text/css" rel="stylesheet" />
        <link href="css/console.css" type="text/css" rel="stylesheet" />

        <script src="js/libs/jquery.js" type="text/javascript"></script>
        <script src="js/libs/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/modelos/appkeys.js" type="text/javascript"></script>
    </head> 
    <body>
		<?php
		include 'pag/includes/nav-bar.html';
	
		//	arquivo nao existe ou esta havendo uma tentativa de script injection
		if(!file_exists('pag/' . $url[0] . '.html') || !in_array($url[0], $allowedURLs))
			require 'pag/erro.html';
		else
			require 'pag/' . $url[0] . '.html';
			
		include 'pag/includes/footer.html';
		?>
    </body>
</html>