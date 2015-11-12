package t1;

import java.util.BitSet;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

public class T1ErrorListener implements ANTLRErrorListener {
    SaidaParser sp;
    
    public T1ErrorListener(SaidaParser sp) {
        this.sp = sp;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> rcgnzr, Object o, int i, int i1, String string, RecognitionException re) {
        sp.println("Erro: linha " + i + ":" + i1 + " " + string);
    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, BitSet bitset, ATNConfigSet atncs) {
        sp.println("Ambiguidade: linha " + i + ":" + i1 + " " + dfa.toString());
    }

    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, ATNConfigSet atncs) {
//        sp.println("Attempting full context: linha " + i + ":" + i1 + " " + dfa.toString());
    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, ATNConfigSet atncs) {
//        sp.println("Context sensitivity: linha " + i + ":" + i1 + " " + dfa.toString());
    }
}
