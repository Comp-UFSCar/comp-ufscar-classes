/*
 * APPKEYS.JS
 * 
 * DATA CRIACAO: 25/11/2012
 * 
 * DESCRICAO: objeto que interpreta as teclas de atalho pressionadas
 * 
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * LISTA DE MODIFICACOES
 * 
 * 25/11/2012:  criacao do arquivo, +privKeys, +Ativo, +PreAtalho, 
 *              +TeclaAtual, +TeclaAnterior, +EntrarTecla(), +Interpretar()
 * 
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

(function() {
    
    var privKeys = {
        Ativo: true,
        PreAtalho: false, // exige o pressionamento da tecla G antes de qualquer atalho
        TeclaAtual: null,
        TeclaAnterior: null,
        
        EntrarTecla: function(event) {
            event = window.event || event
            
            if(this.TeclaAtual)
                this.TeclaAnterior = this.TeclaAtual
            this.TeclaAtual = event.keyCode
            
            if(this.Ativo)
                this.Interpretar()
            return 0
        },
        Interpretar: function() {
            // Qualquer um dos atalhos so pode ser acessado se a tecla G tiver sido pressionada
            if(this.PreAtalho && this.TeclaAnterior != 71)  // G
                return 0
            
            if(this.TeclaAnterior == 65 && this.TeclaAtual != 65) {
                if(this.TeclaAtual == 68)
                    Grafo.Algoritmos.Dijkstra()
                if(this.TeclaAtual == 75)
                    Grafo.Algoritmos.Kruskal()
                if(this.TeclaAtual == 80)
                    Grafo.Algoritmos.Prim()
                if(this.TeclaAtual == 67)
                    Grafo.Algoritmos.Coloracao()
                
                return 0
            }
            
            if(this.TeclaAtual == 65) // A
                return $('#bt-algoritmos').click()
            if(this.TeclaAtual == 67) // C
                return $('#bt-console').click()
            if(this.TeclaAtual == 82) // R
                return $('#bt-atualiza-grafo').click()
            if(this.TeclaAtual == 79) // O
                return $('#modal-opcoes').modal()
            if(this.TeclaAtual == 77) // M
                return $('#modal-matriz').modal()
            if(this.TeclaAtual == 83) // S
                return $('#modal-atalhos').modal()
                   
            this.TeclaAtual = this.TeclaAnterior = null
            
            return 0
        }
    }
    
    window.AppKeys = privKeys // retornando objeto publico
}())
