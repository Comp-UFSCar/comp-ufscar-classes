/*
 * CONSOLE.JS
 * 
 * DATA CRIACAO: 22/11/2012
 * 
 * DESCRICAO: inicializacoa do objeto Grafo.Console, +MaxElementos, +$Content
 * 
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * LISTA DE MODIFICACOES
 * 
 * 22/11/2012: criacao do arquivo, +MaxElementos, +$Content, +Index, +Inserir()
 * 23/11/2012: tooltips, callback de selecao de elementos
 * 25/11/2012: exibicao diferenciada de badges a partir de classes
 * 26/11/2012: correcao bug de badge em elementos
 * 29/12/2012: ln 50~55 - impede que um mesmo elemento seja exibido duas vezes no console
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

(function() {
    var tmpConsole = {
        
        MaxElementos: 32, // maximo de itens exibidos no console
        $Content: '',     // objeto jQuery que armazena todos os itens atuais
        Index: 0,         // numero de elementos inseridos
        
        Init: function() {
            this.Index    = 0
            $('#ul-console').empty()
        },
        
        /* INSERE UMA MENSAGEM NA DIV POPOVER-INNER
         * Parametros:
         * @pMsg: mensagem a ser exibida
         * @pInforAdicional: mensagem exibida no tooltip (opcional)
         * @pClasse: adiciona uma classe especial sobre a mensagem (ocional)
         * @pObj: se a mensagem for a respeito de uma aresta ou vertice, define funcoes
         *        a fim de interagir com este objeto
         *
         * Retorna:
            true, se a mensagem foi inserida
            false, se a mensagem ee invalida 
         */
        Inserir: function(pMsg, pInforAdicional, pClasse, pObj) {
            if (!pMsg || pMsg == undefined) // Erro! Mensagem nao pode ser vazia ou indefinida
                return false
            
            this.Index++
            
            //  valida informacao
            if (pInforAdicional == undefined) pInforAdicional = false
            if (pClasse == undefined)         pClasse = ''
            
            //  verifica se o elemento ja existe, se sim, ele ee retirado
            var $ulConsole = $('#ul-console')
            $ulConsole.children().each(function() {
                if (this.dataset.msg == pMsg)
                    $(this).remove()
            })
             
            // Se o numero de elementos inserios ee maior do que o maximo, deleta o ultimo
            if ($ulConsole.children().length >= this.MaxElementos)
                $('#ul-console').children().last().remove()
            
            var $lsItem = $('<li />')
            .attr('data-msg', pMsg)
            .addClass(' console-item')
            .on('click', function() {
                return false
            });
            
            var tmpBadge = ''
            if (pClasse == 'console-aresta')  tmpBadge = 'badge-warning'
            if (pClasse == 'console-vertice') tmpBadge = 'badge-info'
            
            $lsItem
            .html('<a tabindex="-1" rel="" href="#"><span class="badge ' +tmpBadge +'">'
                +this.Index+'</span></a> <span class="pull-right ' + pClasse + '"><strong>' +pMsg +'</strong></span></li>');
            
            // se a mensagem refere-se a uma areta ou vertice, coloca sobre aquele elemento
            if (pObj && pObj != undefined) {
                if (pClasse == 'console-aresta') {
                    $lsItem.children().eq(0).on('mouseover', function() {
                        Grafo.Grafico.SelecAresta(pObj.__data__, 0, 0, true);
                    })
                    $lsItem.children().eq(0).on('mouseout', function() {
                        Grafo.Grafico.BlurAresta(pObj.__data__);
                    })
                }
                if (pClasse == 'console-vertice') {
                    $lsItem.children().eq(0).on('mouseover', function() {
                        Grafo.Grafico.SelecVertice(pObj.__data__, 0, 0, true);
                    })
                    $lsItem.children().eq(0).on('mouseout', function() {
                        Grafo.Grafico.BlurVertice(pObj.__data__);
                    })
                }
            }
            
            if (pInforAdicional) // se alguma mensagem adicional foi informada, cria um elemento tooltip para esta
                $lsItem.children().eq(0).tooltip({
                    'title': pInforAdicional,
                    'placement': 'right',
                    'html': true
                });

            $ulConsole.prepend($lsItem);            
            return true;
        }
    }

    window.lnConsole = tmpConsole; //  retorna objeto publico
}());
