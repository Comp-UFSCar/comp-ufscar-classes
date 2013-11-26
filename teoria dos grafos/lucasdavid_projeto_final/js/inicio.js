/*
 * INICIO.JS
 * 
 * DATA CRIACAO: 19/11/2012
 * 
 * DESCRICAO: ultimo arquivo que sera incluido no index.html, porem o primeiro a ser
 * executado. Descreve as acoes globais, como botoes, link, etc.
 * Tambem descreve as acoes tomadas quando o usuario entra no site.
 * 
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * LISTA DE MODIFICACOES
 * 
 * 20/11/2012: -link.type, +link.cor
 * 22/11/2012: -link.cor, +function PrintarMatriz
 * 23/11/2012: -link.cor, +initApp(), +$('#bt-atualiza-grafo').click()
 *                     +$('#bt-console').popover()
 * 25/11/2012: +@pAlg em initApp()                    
 * 28/11/2012: +on(keyup, func...)
 * 29/11/2012: +dijkstra
 * 30/11/2012: +profundidade, +largura
 * 01/12/2012: comentario sobre funcoes
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

/* funcion initApp(pAlg)
 * Description:
 * Inicia o reinicia estruturas graficas e logicas
 * trigger: $('document').ready() ou $('#bt-atualiza-grafo').click()
 * Params:
 *    @pAlg: booleaan - se verdadeiro, trabalha sobre a matriz de algum dos algoritmo aplicados
 *                      caso contrario, trabalha sobre a matriz de adjacencia do grafo original
 * return: false, a fim de anular o click do mouse
 */
function initApp(pAlg) {
    Grafo.Grafico.ExibeLoading()

    // o usuario acabou de entrar no site, inicializa o console e 
    if(!pAlg) {
        lnConsole.Init()
        Grafo.CarregarDeArquivo()
    }
    else {
        Grafo // os metodos abaixo sao exclusivos em relacao ao CarregarDeArquivo()
        .Init(pAlg)               // inicia estrutura logica do grafo
        .Grafico.Init(pAlg)       // inicia estrutura grafica do grafo
    }
    
    return false
}

/**
 * func $(document).ready()
 * description:
 * esta funcao e a primeira a ser executada, sendo
 * chamada logo apos o termino do carregamento dos elementos HTML.
 */
$(document).ready(function() {
    // Cria o console, responsavel por exibir informacoes a respeito do grafo
    $('#bt-console').popover({
        'html': true,
        'placement': 'bottom',
        'title': '<li class="nav-header">Mensagens</li>',
        'content': '<ul id="ul-console" class=""></ul>'
    })
    .popover('show')
    
    // define acoes a serem realizadas quando 
    // botoes sobre a tela sao pressionados.
    $('#bt-atualiza-grafo').click(function() {
        return initApp()
    })
    $('#algoritmo-kruskal').click(function() {
        return Grafo.Algoritmos.Kruskal()
    })
    $('#algoritmo-prim').click(function() {
        return Grafo.Algoritmos.Prim()
    })
    $('#algoritmo-dijkstra').click(function() {
        return Grafo.Algoritmos.Dijkstra()
    })
	$('#algoritmo-profundidade').click(function() {
        return Grafo.Algoritmos.Profundidade()
    })
	$('#algoritmo-largura').click(function() {
        return Grafo.Algoritmos.Largura()
    })
    $('#algoritmo-coloracao').click(function() {
        return Grafo.Algoritmos.Coloracao()
    })
    
    // Inicializa o interpretador de atalhos do teclado.
    $(document).on('keyup', function(event){
        AppKeys.EntrarTecla(event)
    })    
    
    // Inicializa a aplicacao pela primeira vez.
    initApp()
})
