/*
 * INIT.JS
 * 
 * DATA CRIACAO: 20/11/2012
 * 
 * DESCRICAO: declaracao das funcoes relacionadas a exibicoes graficas, utilizando
 * ou nao a API.
 * 
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * LISTA DE MODIFICACOES
 * 
 * 20/11/2012: -link.type, +link.cor
 * 22/11/2012: -link.cor, +PrintarMatriz()
 * 23/11/2012: +selecAresta(), +blurAresta(), +selecVertice(), +blurVertice()
 * 24/11/2012: refatoracao - agora os vertices e arestas sao entidades disjuntas,
 *                     e tem suas construcoes realizadas de forma separada
 * 25/11/2012: +line.weight, alteracao na exibicao da matriz de adjacencia
 * 01/12/2012: +efeito sobre selecao dos vertices
 * 28/12/2012: limite sobre crescimento de vertices imposto.
 * 19/01/2013: coloracao de vertices
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

(function() {
    
    //  Construtor dos elementos graficos do grafo (representacao grafica e matriz de adjacencia)
    //  Chamado nos eventos $(document).ready() e em $('#bt-atualiza-grafo').click()
    var tmpInit = function(pAlg) {
        // caso esta funcao seja chamada com o parametro @pAlg como verdadeiro, isso
        // significa que algum algoritmo foi aplicado sobre o grafo. Logo devemos desenhar
        // o grafo contido em Grafo.Algoritmos.MatrizAdj, exceto se tal algoritmo for o de
        // coloracao, onde a matriz de saida ee a original
        var tmpMatrizAdj = pAlg ? Grafo.Algoritmos.MatrizAdj : Grafo.MatrizAdj
        
        var i, j, tmpID = 0,
            $parente = $(Grafo.Grafico.Parente),
            nodes = new Array(), links = new Array()

        // DEFINE VERTICES
        for (i = 0; i < tmpMatrizAdj.length; i++) {
            nodes.push({
                name: '',
                cor: 0
            })

            if (pAlg == 'coloracao') nodes[i].cor = tmpMatrizAdj[i][i]
        }
        
        // DEFINE AS ARESTAS
        for (i = 0; i < tmpMatrizAdj.length; i++) { // para todos os vertices
            for (j = Grafo.Direcionado ? 0 : i; j < tmpMatrizAdj.length; j++) {
                
                if( i == j && pAlg == 'coloracao' )
                    continue // impede criacao de loops quando o algoritmo de coloracao foi aplicado, levando em consideracao
                             // que na pos. [i][i] da matriz de adjacencia esta representada a cor do vertice, e nao uma conexao

                if (tmpMatrizAdj[i][j] != 0)    // atraves da matriz de adjacencia, busca 
                    links.push({             // quais vertices sao ligadas por quais arestas
                        id: tmpID++,
                        source: i,
                        target: j,
                        weight: tmpMatrizAdj[i][j] // peso da aresta
                    })
            }
        }

        // se um grafo ja esta sendo mostrado, o remove.
        $parente.empty()
        
        var force = d3.layout.force()
        .nodes(nodes)
        .links(links)
        .size([Grafo.Grafico.width, Grafo.Grafico.height])
        .charge(Grafo.Grafico.MovimentacaoVertices)
        .linkDistance(Grafo.Grafico.TamArestas)
        .on('tick', tick)
        
        var svg = d3.select(Grafo.Grafico.Parente).append('svg')
        .attr('width', Grafo.Grafico.width)
        .attr('height', Grafo.Grafico.height)
        .style({
            'margin': 0, 
            'top': 0, 
            'left': 0
        })
        
        force.nodes(nodes)
        .links(links)
        .start()
        
        var link = svg.selectAll('.link')
        .data(force.links())
        .enter().append('line')
        .attr('class', 'link')
        .on('mouseover', SelecAresta)
        .on('mouseout', BlurAresta)
        
        var color = d3.scale.category20()

        var node = svg.selectAll('.node')
        .data(force.nodes())
        .enter()
        .append('g')
        .attr('class', 'node')
        .on('mouseover', SelecVertice)
        .on('mouseout', BlurVertice)
        
        .append('circle')
        .attr('r', Grafo.Grafico.NodeRadius)

        // colore grafo, caso o algoritmo de coloracao tenha sido aplicado.
        if (pAlg == 'coloracao') node.style("fill", function(d) { return color(d.cor); })
        
        node.call(force.drag)
                
        function tick() {
            link
            .attr('x1', function(d) { return d.source.x })
            .attr('y1', function(d) { return d.source.y })
            .attr('x2', function(d) { return d.target.x })
            .attr('y2', function(d) { return d.target.y })

             node
            .attr('cx', function(d) { return d.x })
			.attr('cy', function(d) { return d.y })
        }
        
        // definindo raio dos vertices proporcionalmente a seus graus
        var $vertices = $('.node'), $arestas = $('.link')
        
        $vertices.each(function(index, el) {
            $(el).attr('id', index) // adiciona IDs aos vertices
            var $circle = $(el).children().eq(0)   // seleciona o circulo dentro de vertice
            
            var tmpRaio = Grafo.Grafico.NodeRadius
            
            // verifica se media de graus = 0. Se sim, a conta abaixo nao ee executada
            // (ela retornaria 'infinity'). tmpRaio continua valendo 25 (valor minimo)
            if (Grafo.MediaDeGraus)
                tmpRaio = Grafo.Grau[index] *Grafo.Grafico.NodeRadius / Grafo.MediaDeGraus
            tmpRaio = parseInt(tmpRaio)
                        
            if (tmpRaio < Grafo.Grafico.NodeRadius)
                tmpRaio = Grafo.Grafico.NodeRadius
            if (tmpRaio > Grafo.Grafico.NodeRadius *3)
                tmpRaio = Grafo.Grafico.NodeRadius *3
            
            $circle.attr('r', tmpRaio)
        })
    
        // atribuindo IDs as arestas, a fim de manipula-las com a jQuery
        $arestas.each(function(index, el){
            $(el).attr('id', index)
        })
    
        // EXIBINDO A MATRIZ DE ADJACENCIA NA TABELA DA PAGINA
        var $matriz // objeto jQuery que contem tabela da matriz de adjacencia
        
        $parente = $('#modal-matriz .modal-body') // objeto jQuery que contem elemento pai da <table>
        $parente.children().remove() // remove elementos ja existentes
        
        $matriz = $('<table />', {
            'id': 'tabela-m-adj',
            'class': 'table table-bordered table-striped table-hover table-condensed'
        })
        
        $matriz.html($('<thead />'))       // <table><thead></thead></table>
        $matriz.children().html('<tr />')  // <table><thead><tr></tr></thead></table>
        
        $matriz.append($('<tbody />'))     // <table><thead><tr></tr></thead><tbody></tbody></table>
        
        for (i = 0; i < tmpMatrizAdj.length; i++) {
            $matriz.children().eq(0).children().append('<th>V'+i+'</th>')
            var $linha = $('<tr />')
            
            for (j = 0; j < tmpMatrizAdj.length; j++) {
                var $el = $('<td />')
                $el.html(tmpMatrizAdj[i][j])
                $linha.append($el)     // insere a coluna   
            
            }
            $matriz.children().eq(1).append($linha) // insere a linha em 
        }
        $parente.append($matriz) // insere matriz
        $parente.append('<p><strong>Média de graus:</strong> ' + Grafo.MediaDeGraus + '</p>')
        
        
        // informa fim da construcao grafica
        lnConsole.Inserir('Construção gráfica concluída.', 'Número total de vértices: ' +$('.node').length
                     + '<br />Número total de Arestas: ' +$('.link').length)
    
        return false
    }
    
    function ExibeLoading() {
        var $parente = $(Grafo.Grafico.Parente)
        
        $parente.empty()
        $parente.append('<center><br /><div style="margin-top:50px; height:60%; min-height:100px">' +
        '<h1><img src="img/loader.gif" /> <small>Carregando...</small></h1></div></center>')
        
        return false
    }
    
    // Funcoes que descrevem a movimentacao dos elementos no evento 'mouseover'
    function SelecVertice(d, el, key, inibeConsole) {
        var obj = $('#' +d.index +'.node')[0],
            D3Circle = d3.select(obj).select('circle'), i, tmpDesc = '<ul>'
        
        // se existe um link em que o source ou target sao iguais a d.index,
        // exibe a outra ponta dele (o outro vertice)
        $('.link').each(function(){
            if (this.__data__.source.index == d.index)
                tmpDesc += '<li>' +(this.__data__.target.index +1)  +'</li>'
            
            if (this.__data__.target.index == d.index)
                tmpDesc += '<li>' +(this.__data__.source.index +1)  +'</li>'
        })
        tmpDesc += '</ul>'
        
        D3Circle.transition()
        .duration(300)
        .attr({
			'r': function() { return D3Circle.attr('r') *0.9 },
		})
		
		d3.select(obj).attr('class', 'nodeSelected')
        
        if (!inibeConsole || inibeConsole === 'undefined')
            lnConsole.Inserir('Vértice ' +(d.index +1) + (d.name ? ': ' + d.name : ''), 'Grau: ' + Grafo.Grau[d.index]
                     + '<br />' +'Vértices adjacentes: ' +tmpDesc, 'console-vertice', obj)
    }
    function BlurVertice(v) {
        var obj = $('#' +v.index +'.nodeSelected')[0],
            tmpRaio = Grafo.Grafico.NodeRadius
            
        // verifica se media de graus = 0. Se sim, a conta abaixo nao ee executada
        // (ela retornaria 'infinity'). tmpRaio continua valendo 25 (valor minimo)
        if (Grafo.MediaDeGraus)
            tmpRaio = Grafo.Grau[v.index] *Grafo.Grafico.NodeRadius / Grafo.MediaDeGraus
        tmpRaio = parseInt(tmpRaio)
                    
        if (tmpRaio < Grafo.Grafico.NodeRadius)
            tmpRaio = Grafo.Grafico.NodeRadius
        if (tmpRaio > Grafo.Grafico.NodeRadius *3)
            tmpRaio = Grafo.Grafico.NodeRadius *3
        
        d3.select(obj).select('circle')
        .transition()
        .delay(500)
        .duration(300)
        .attr({
			'r': tmpRaio
		})
		
		d3.select(obj).attr('class', 'node')
    }
    function SelecAresta(d, key, el, inibeConsole) {
        var obj = $('#' +d.id +'.link')[0]
        
        d3.select(obj)
        .attr('class', 'linkSelected')
        
        if (!inibeConsole)
            lnConsole.Inserir('Aresta ' +(d.id +1), 'Peso: ' +d.weight +'<br />Conecta os vértices '
                             +(d.source.index +1) + '-' +(d.target.index +1), 'console-aresta', obj)
    }
    function BlurAresta(d) {
        var obj = $('#' +d.id +'.linkSelected')[0]
        d3.select(obj).attr('class', 'link')
    }
    
    
    // Retorna elementos públicos
    Grafo.Grafico.Init = tmpInit
    Grafo.Grafico.SelecVertice = SelecVertice
    Grafo.Grafico.SelecAresta = SelecAresta
    Grafo.Grafico.BlurVertice = BlurVertice
    Grafo.Grafico.BlurAresta = BlurAresta
    Grafo.Grafico.ExibeLoading = ExibeLoading
    
    return Grafo   // permite 'chained method'
}())
