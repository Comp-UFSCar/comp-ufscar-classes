package t1;

public class EntradaTabelaDeSimbolos {
    private String lexema;
    private Tipo tipo;
    
    public EntradaTabelaDeSimbolos(String lexema, Tipo tipo) {
        this.lexema = lexema;
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.lexema != null ? this.lexema.hashCode() : 0);
        hash = 89 * hash + (this.tipo != null ? this.tipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntradaTabelaDeSimbolos other = (EntradaTabelaDeSimbolos) obj;
        if ((this.lexema == null) ? (other.lexema != null) : !this.lexema.equals(other.lexema)) {
            return false;
        }
        if (this.tipo != other.tipo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lexema=" + lexema + ", tipo=" + tipo;
    }
    
    
}
