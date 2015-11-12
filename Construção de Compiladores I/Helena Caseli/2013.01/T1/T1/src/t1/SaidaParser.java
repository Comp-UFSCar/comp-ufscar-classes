/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package t1;

public class SaidaParser {

    StringBuffer conteudo;
    boolean modificado;

    public SaidaParser() {
        conteudo = new StringBuffer();
        modificado = false;
    }

    public void println(String texto) {
        if(!modificado) modificado = true;
        conteudo.append(texto);
        conteudo.append("\n");
    }
    
    public boolean isModificado() {
        return modificado;
    }

    @Override
    public String toString() {
        return conteudo.toString();
    }
}