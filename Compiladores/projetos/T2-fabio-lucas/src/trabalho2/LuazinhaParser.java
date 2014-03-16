// Generated from /home/david/NetBeansProjects/T2/src/trabalho2/Luazinha.g4 by ANTLR 4.0
package trabalho2;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LuazinhaParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__46=1, T__45=2, T__44=3, T__43=4, T__42=5, T__41=6, T__40=7, T__39=8, 
		T__38=9, T__37=10, T__36=11, T__35=12, T__34=13, T__33=14, T__32=15, T__31=16, 
		T__30=17, T__29=18, T__28=19, T__27=20, T__26=21, T__25=22, T__24=23, 
		T__23=24, T__22=25, T__21=26, T__20=27, T__19=28, T__18=29, T__17=30, 
		T__16=31, T__15=32, T__14=33, T__13=34, T__12=35, T__11=36, T__10=37, 
		T__9=38, T__8=39, T__7=40, T__6=41, T__5=42, T__4=43, T__3=44, T__2=45, 
		T__1=46, T__0=47, NOME=48, CADEIA=49, NUMERO=50, COMENTARIO=51, WS=52;
	public static final String[] tokenNames = {
		"<INVALID>", "']'", "'in'", "'local'", "','", "'['", "'while'", "'-'", 
		"'*'", "'or'", "':'", "'('", "'not'", "'if'", "'<'", "'false'", "'<='", 
		"'until'", "'nil'", "'~='", "'...'", "'{'", "'break'", "'and'", "'else'", 
		"'}'", "'true'", "'elseif'", "'do'", "'%'", "'^'", "'.'", "'function'", 
		"')'", "'+'", "'for'", "'return'", "'='", "';'", "'repeat'", "'>'", "'then'", 
		"'/'", "'=='", "'>='", "'#'", "'end'", "'..'", "NOME", "CADEIA", "NUMERO", 
		"COMENTARIO", "WS"
	};
	public static final int
		RULE_programa = 0, RULE_trecho = 1, RULE_bloco = 2, RULE_comando = 3, 
		RULE_ultimocomando = 4, RULE_nomedafuncao = 5, RULE_listavar = 6, RULE_var = 7, 
		RULE_listadenomes = 8, RULE_listaexp = 9, RULE_exp = 10, RULE_expprefixo = 11, 
		RULE_expprefixo2 = 12, RULE_chamadadefuncao = 13, RULE_args = 14, RULE_funcao = 15, 
		RULE_corpodafuncao = 16, RULE_listapar = 17, RULE_construtortabela = 18, 
		RULE_listadecampos = 19, RULE_campo = 20, RULE_separadordecampos = 21, 
		RULE_opbin = 22, RULE_opunaria = 23;
	public static final String[] ruleNames = {
		"programa", "trecho", "bloco", "comando", "ultimocomando", "nomedafuncao", 
		"listavar", "var", "listadenomes", "listaexp", "exp", "expprefixo", "expprefixo2", 
		"chamadadefuncao", "args", "funcao", "corpodafuncao", "listapar", "construtortabela", 
		"listadecampos", "campo", "separadordecampos", "opbin", "opunaria"
	};

	@Override
	public String getGrammarFileName() { return "Luazinha.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }


	    PilhaDeTabelas pilhaDeTabelas = new PilhaDeTabelas();

	public LuazinhaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramaContext extends ParserRuleContext {
		public TrechoContext trecho() {
			return getRuleContext(TrechoContext.class,0);
		}
		public ProgramaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_programa; }
	}

	public final ProgramaContext programa() throws RecognitionException {
		ProgramaContext _localctx = new ProgramaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_programa);
		try {
			enterOuterAlt(_localctx, 1);
			{
			 pilhaDeTabelas.empilhar(new TabelaDeSimbolos("global")); 
			setState(49); trecho();
			 pilhaDeTabelas.desempilhar(); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TrechoContext extends ParserRuleContext {
		public List<ComandoContext> comando() {
			return getRuleContexts(ComandoContext.class);
		}
		public UltimocomandoContext ultimocomando() {
			return getRuleContext(UltimocomandoContext.class,0);
		}
		public ComandoContext comando(int i) {
			return getRuleContext(ComandoContext.class,i);
		}
		public TrechoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trecho; }
	}

	public final TrechoContext trecho() throws RecognitionException {
		TrechoContext _localctx = new TrechoContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_trecho);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 3) | (1L << 6) | (1L << 13) | (1L << 28) | (1L << 32) | (1L << 35) | (1L << 39) | (1L << NOME))) != 0)) {
				{
				{
				setState(52); comando();
				setState(54);
				_la = _input.LA(1);
				if (_la==38) {
					{
					setState(53); match(38);
					}
				}

				}
				}
				setState(60);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(65);
			_la = _input.LA(1);
			if (_la==22 || _la==36) {
				{
				setState(61); ultimocomando();
				setState(63);
				_la = _input.LA(1);
				if (_la==38) {
					{
					setState(62); match(38);
					}
				}

				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlocoContext extends ParserRuleContext {
		public TrechoContext trecho() {
			return getRuleContext(TrechoContext.class,0);
		}
		public BlocoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bloco; }
	}

	public final BlocoContext bloco() throws RecognitionException {
		BlocoContext _localctx = new BlocoContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_bloco);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67); trecho();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComandoContext extends ParserRuleContext {
		public ListavarContext listavar;
		public Token NOME;
		public ListadenomesContext listadenomes;
		public NomedafuncaoContext nomedafuncao;
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public BlocoContext bloco(int i) {
			return getRuleContext(BlocoContext.class,i);
		}
		public List<BlocoContext> bloco() {
			return getRuleContexts(BlocoContext.class);
		}
		public ListadenomesContext listadenomes() {
			return getRuleContext(ListadenomesContext.class,0);
		}
		public CorpodafuncaoContext corpodafuncao() {
			return getRuleContext(CorpodafuncaoContext.class,0);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public NomedafuncaoContext nomedafuncao() {
			return getRuleContext(NomedafuncaoContext.class,0);
		}
		public ChamadadefuncaoContext chamadadefuncao() {
			return getRuleContext(ChamadadefuncaoContext.class,0);
		}
		public ListavarContext listavar() {
			return getRuleContext(ListavarContext.class,0);
		}
		public TerminalNode NOME() { return getToken(LuazinhaParser.NOME, 0); }
		public ListaexpContext listaexp() {
			return getRuleContext(ListaexpContext.class,0);
		}
		public ComandoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comando; }
	}

	public final ComandoContext comando() throws RecognitionException {
		ComandoContext _localctx = new ComandoContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_comando);
		int _la;
		try {
			setState(159);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(69); ((ComandoContext)_localctx).listavar = listavar();
				setState(70); match(37);
				setState(71); listaexp();
				 for (String nome : ((ComandoContext)_localctx).listavar.nomes)
				                if (! pilhaDeTabelas.existeSimbolo(nome))
				                   pilhaDeTabelas.topo().adicionarSimbolo(nome, "variavel"); 
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(74); chamadadefuncao();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(75); match(28);
				setState(76); bloco();
				setState(77); match(46);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(79); match(6);
				setState(80); exp(0);
				setState(81); match(28);
				setState(82); bloco();
				setState(83); match(46);
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(85); match(39);
				setState(86); bloco();
				setState(87); match(17);
				setState(88); exp(0);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(90); match(13);
				setState(91); exp(0);
				setState(92); match(41);
				setState(93); bloco();
				setState(101);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==27) {
					{
					{
					setState(94); match(27);
					setState(95); exp(0);
					setState(96); match(41);
					setState(97); bloco();
					}
					}
					setState(103);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(106);
				_la = _input.LA(1);
				if (_la==24) {
					{
					setState(104); match(24);
					setState(105); bloco();
					}
				}

				setState(108); match(46);
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(110); match(35);
				 pilhaDeTabelas.empilhar(new TabelaDeSimbolos("for")); 
				setState(112); ((ComandoContext)_localctx).NOME = match(NOME);
				 if (! pilhaDeTabelas.existeSimbolo(((ComandoContext)_localctx).NOME.getText()))
				                pilhaDeTabelas.topo().adicionarSimbolo(((ComandoContext)_localctx).NOME.getText(), "variavel"); 
				setState(114); match(37);
				setState(115); exp(0);
				setState(116); match(4);
				setState(117); exp(0);
				setState(120);
				_la = _input.LA(1);
				if (_la==4) {
					{
					setState(118); match(4);
					setState(119); exp(0);
					}
				}

				setState(122); match(28);
				setState(123); bloco();
				 pilhaDeTabelas.desempilhar(); 
				setState(125); match(46);
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(127); match(35);
				 pilhaDeTabelas.empilhar(new TabelaDeSimbolos("for")); 
				setState(129); ((ComandoContext)_localctx).listadenomes = listadenomes();
				setState(130); match(2);
				setState(131); listaexp();
				 pilhaDeTabelas.topo().adicionarSimbolos(((ComandoContext)_localctx).listadenomes.nomes, "variavel"); 
				setState(133); match(28);
				setState(134); bloco();
				 pilhaDeTabelas.desempilhar(); 
				setState(136); match(46);
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(138); match(32);
				setState(139); ((ComandoContext)_localctx).nomedafuncao = nomedafuncao();
				 pilhaDeTabelas.empilhar(new TabelaDeSimbolos(((ComandoContext)_localctx).nomedafuncao.nome)); 
				            
				             if (((ComandoContext)_localctx).nomedafuncao.metodo)
				                pilhaDeTabelas.topo().adicionarSimbolo("self", "parametro"); 
				setState(141); corpodafuncao();
				 pilhaDeTabelas.desempilhar(); 
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(144); match(3);
				setState(145); match(32);
				setState(146); ((ComandoContext)_localctx).NOME = match(NOME);
				 pilhaDeTabelas.empilhar(new TabelaDeSimbolos(((ComandoContext)_localctx).NOME.getText())); 
				setState(148); corpodafuncao();
				 pilhaDeTabelas.desempilhar(); 
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(151); match(3);
				setState(152); ((ComandoContext)_localctx).listadenomes = listadenomes();
				setState(155);
				_la = _input.LA(1);
				if (_la==37) {
					{
					setState(153); match(37);
					setState(154); listaexp();
					}
				}

				 pilhaDeTabelas.topo().adicionarSimbolos(((ComandoContext)_localctx).listadenomes.nomes, "variavel"); 
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UltimocomandoContext extends ParserRuleContext {
		public ListaexpContext listaexp() {
			return getRuleContext(ListaexpContext.class,0);
		}
		public UltimocomandoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ultimocomando; }
	}

	public final UltimocomandoContext ultimocomando() throws RecognitionException {
		UltimocomandoContext _localctx = new UltimocomandoContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_ultimocomando);
		int _la;
		try {
			setState(166);
			switch (_input.LA(1)) {
			case 36:
				enterOuterAlt(_localctx, 1);
				{
				setState(161); match(36);
				setState(163);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 7) | (1L << 11) | (1L << 12) | (1L << 15) | (1L << 18) | (1L << 20) | (1L << 21) | (1L << 26) | (1L << 32) | (1L << 45) | (1L << NOME) | (1L << CADEIA) | (1L << NUMERO))) != 0)) {
					{
					setState(162); listaexp();
					}
				}

				}
				break;
			case 22:
				enterOuterAlt(_localctx, 2);
				{
				setState(165); match(22);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NomedafuncaoContext extends ParserRuleContext {
		public String nome;
		public boolean metodo;
		public Token n1;
		public Token n2;
		public Token n3;
		public TerminalNode NOME(int i) {
			return getToken(LuazinhaParser.NOME, i);
		}
		public List<TerminalNode> NOME() { return getTokens(LuazinhaParser.NOME); }
		public NomedafuncaoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nomedafuncao; }
	}

	public final NomedafuncaoContext nomedafuncao() throws RecognitionException {
		NomedafuncaoContext _localctx = new NomedafuncaoContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_nomedafuncao);
		 ((NomedafuncaoContext)_localctx).metodo =  false; 
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168); ((NomedafuncaoContext)_localctx).n1 = match(NOME);
			 ((NomedafuncaoContext)_localctx).nome =  ((NomedafuncaoContext)_localctx).n1.getText(); 
			setState(175);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==31) {
				{
				{
				setState(170); match(31);
				setState(171); ((NomedafuncaoContext)_localctx).n2 = match(NOME);
				 _localctx.nome += "." + ((NomedafuncaoContext)_localctx).n2.getText(); 
				}
				}
				setState(177);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(181);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(178); match(10);
				setState(179); ((NomedafuncaoContext)_localctx).n3 = match(NOME);
				 ((NomedafuncaoContext)_localctx).metodo =  true; _localctx.nome += "." + ((NomedafuncaoContext)_localctx).n3.getText(); 
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListavarContext extends ParserRuleContext {
		public List<String> nomes;
		public VarContext v1;
		public VarContext v2;
		public List<VarContext> var() {
			return getRuleContexts(VarContext.class);
		}
		public VarContext var(int i) {
			return getRuleContext(VarContext.class,i);
		}
		public ListavarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listavar; }
	}

	public final ListavarContext listavar() throws RecognitionException {
		ListavarContext _localctx = new ListavarContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_listavar);
		 ((ListavarContext)_localctx).nomes =  new ArrayList<String>(); 
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183); ((ListavarContext)_localctx).v1 = var();
			 _localctx.nomes.add(((ListavarContext)_localctx).v1.nome); 
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==4) {
				{
				{
				setState(185); match(4);
				setState(186); ((ListavarContext)_localctx).v2 = var();
				 _localctx.nomes.add(((ListavarContext)_localctx).v2.nome); 
				}
				}
				setState(193);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarContext extends ParserRuleContext {
		public String nome;
		public int linha;
		public int coluna;
		public Token NOME;
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode NOME() { return getToken(LuazinhaParser.NOME, 0); }
		public ExpprefixoContext expprefixo() {
			return getRuleContext(ExpprefixoContext.class,0);
		}
		public VarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var; }
	}

	public final VarContext var() throws RecognitionException {
		VarContext _localctx = new VarContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_var);
		try {
			setState(205);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(194); ((VarContext)_localctx).NOME = match(NOME);
				 ((VarContext)_localctx).nome =  ((VarContext)_localctx).NOME.getText(); ((VarContext)_localctx).linha =  (((VarContext)_localctx).NOME!=null?((VarContext)_localctx).NOME.getLine():0); ((VarContext)_localctx).coluna =  (((VarContext)_localctx).NOME!=null?((VarContext)_localctx).NOME.getCharPositionInLine():0); 
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(196); expprefixo();
				setState(197); match(5);
				setState(198); exp(0);
				setState(199); match(1);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(201); expprefixo();
				setState(202); match(31);
				setState(203); match(NOME);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListadenomesContext extends ParserRuleContext {
		public List<String> nomes;
		public Token n1;
		public Token n2;
		public TerminalNode NOME(int i) {
			return getToken(LuazinhaParser.NOME, i);
		}
		public List<TerminalNode> NOME() { return getTokens(LuazinhaParser.NOME); }
		public ListadenomesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listadenomes; }
	}

	public final ListadenomesContext listadenomes() throws RecognitionException {
		ListadenomesContext _localctx = new ListadenomesContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_listadenomes);
		 ((ListadenomesContext)_localctx).nomes =  new ArrayList<String>(); 
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(207); ((ListadenomesContext)_localctx).n1 = match(NOME);
			 _localctx.nomes.add(((ListadenomesContext)_localctx).n1.getText()); 
			setState(214);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(209); match(4);
					setState(210); ((ListadenomesContext)_localctx).n2 = match(NOME);
					 _localctx.nomes.add(((ListadenomesContext)_localctx).n2.getText()); 
					}
					} 
				}
				setState(216);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListaexpContext extends ParserRuleContext {
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public ListaexpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listaexp; }
	}

	public final ListaexpContext listaexp() throws RecognitionException {
		ListaexpContext _localctx = new ListaexpContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_listaexp);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(222);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(217); exp(0);
					setState(218); match(4);
					}
					} 
				}
				setState(224);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			setState(225); exp(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpContext extends ParserRuleContext {
		public int _p;
		public OpbinContext opbin() {
			return getRuleContext(OpbinContext.class,0);
		}
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public Expprefixo2Context expprefixo2() {
			return getRuleContext(Expprefixo2Context.class,0);
		}
		public OpunariaContext opunaria() {
			return getRuleContext(OpunariaContext.class,0);
		}
		public ConstrutortabelaContext construtortabela() {
			return getRuleContext(ConstrutortabelaContext.class,0);
		}
		public FuncaoContext funcao() {
			return getRuleContext(FuncaoContext.class,0);
		}
		public TerminalNode NUMERO() { return getToken(LuazinhaParser.NUMERO, 0); }
		public TerminalNode CADEIA() { return getToken(LuazinhaParser.CADEIA, 0); }
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public ExpContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public ExpContext(ParserRuleContext parent, int invokingState, int _p) {
			super(parent, invokingState);
			this._p = _p;
		}
		@Override public int getRuleIndex() { return RULE_exp; }
	}

	public final ExpContext exp(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpContext _localctx = new ExpContext(_ctx, _parentState, _p);
		ExpContext _prevctx = _localctx;
		int _startState = 20;
		enterRecursionRule(_localctx, RULE_exp);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(240);
			switch (_input.LA(1)) {
			case 7:
			case 12:
			case 45:
				{
				setState(228); opunaria();
				setState(229); exp(1);
				}
				break;
			case 18:
				{
				setState(231); match(18);
				}
				break;
			case 15:
				{
				setState(232); match(15);
				}
				break;
			case 26:
				{
				setState(233); match(26);
				}
				break;
			case NUMERO:
				{
				setState(234); match(NUMERO);
				}
				break;
			case CADEIA:
				{
				setState(235); match(CADEIA);
				}
				break;
			case 20:
				{
				setState(236); match(20);
				}
				break;
			case 32:
				{
				setState(237); funcao();
				}
				break;
			case 11:
			case NOME:
				{
				setState(238); expprefixo2();
				}
				break;
			case 21:
				{
				setState(239); construtortabela();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(248);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExpContext(_parentctx, _parentState, _p);
					pushNewRecursionContext(_localctx, _startState, RULE_exp);
					setState(242);
					if (!(2 >= _localctx._p)) throw new FailedPredicateException(this, "2 >= $_p");
					setState(243); opbin();
					setState(244); exp(0);
					}
					} 
				}
				setState(250);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ExpprefixoContext extends ParserRuleContext {
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public TerminalNode NOME(int i) {
			return getToken(LuazinhaParser.NOME, i);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public List<TerminalNode> NOME() { return getTokens(LuazinhaParser.NOME); }
		public ExpprefixoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expprefixo; }
	}

	public final ExpprefixoContext expprefixo() throws RecognitionException {
		ExpprefixoContext _localctx = new ExpprefixoContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_expprefixo);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(251); match(NOME);
			setState(260);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					setState(258);
					switch (_input.LA(1)) {
					case 5:
						{
						setState(252); match(5);
						setState(253); exp(0);
						setState(254); match(1);
						}
						break;
					case 31:
						{
						setState(256); match(31);
						setState(257); match(NOME);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(262);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Expprefixo2Context extends ParserRuleContext {
		public VarContext var;
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public VarContext var() {
			return getRuleContext(VarContext.class,0);
		}
		public ChamadadefuncaoContext chamadadefuncao() {
			return getRuleContext(ChamadadefuncaoContext.class,0);
		}
		public Expprefixo2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expprefixo2; }
	}

	public final Expprefixo2Context expprefixo2() throws RecognitionException {
		Expprefixo2Context _localctx = new Expprefixo2Context(_ctx, getState());
		enterRule(_localctx, 24, RULE_expprefixo2);
		try {
			setState(271);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(263); ((Expprefixo2Context)_localctx).var = var();
				 if (! pilhaDeTabelas.existeSimbolo(((Expprefixo2Context)_localctx).var.nome))
				                Mensagens.erroVariavelNaoExiste(((Expprefixo2Context)_localctx).var.linha, ((Expprefixo2Context)_localctx).var.coluna, ((Expprefixo2Context)_localctx).var.nome); 
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(266); chamadadefuncao();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(267); match(11);
				setState(268); exp(0);
				setState(269); match(33);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ChamadadefuncaoContext extends ParserRuleContext {
		public ArgsContext args() {
			return getRuleContext(ArgsContext.class,0);
		}
		public TerminalNode NOME() { return getToken(LuazinhaParser.NOME, 0); }
		public ExpprefixoContext expprefixo() {
			return getRuleContext(ExpprefixoContext.class,0);
		}
		public ChamadadefuncaoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_chamadadefuncao; }
	}

	public final ChamadadefuncaoContext chamadadefuncao() throws RecognitionException {
		ChamadadefuncaoContext _localctx = new ChamadadefuncaoContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_chamadadefuncao);
		try {
			setState(281);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(273); expprefixo();
				setState(274); args();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(276); expprefixo();
				setState(277); match(10);
				setState(278); match(NOME);
				setState(279); args();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgsContext extends ParserRuleContext {
		public ConstrutortabelaContext construtortabela() {
			return getRuleContext(ConstrutortabelaContext.class,0);
		}
		public TerminalNode CADEIA() { return getToken(LuazinhaParser.CADEIA, 0); }
		public ListaexpContext listaexp() {
			return getRuleContext(ListaexpContext.class,0);
		}
		public ArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_args; }
	}

	public final ArgsContext args() throws RecognitionException {
		ArgsContext _localctx = new ArgsContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_args);
		int _la;
		try {
			setState(290);
			switch (_input.LA(1)) {
			case 11:
				enterOuterAlt(_localctx, 1);
				{
				setState(283); match(11);
				setState(285);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 7) | (1L << 11) | (1L << 12) | (1L << 15) | (1L << 18) | (1L << 20) | (1L << 21) | (1L << 26) | (1L << 32) | (1L << 45) | (1L << NOME) | (1L << CADEIA) | (1L << NUMERO))) != 0)) {
					{
					setState(284); listaexp();
					}
				}

				setState(287); match(33);
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 2);
				{
				setState(288); construtortabela();
				}
				break;
			case CADEIA:
				enterOuterAlt(_localctx, 3);
				{
				setState(289); match(CADEIA);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FuncaoContext extends ParserRuleContext {
		public CorpodafuncaoContext corpodafuncao() {
			return getRuleContext(CorpodafuncaoContext.class,0);
		}
		public FuncaoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcao; }
	}

	public final FuncaoContext funcao() throws RecognitionException {
		FuncaoContext _localctx = new FuncaoContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_funcao);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(292); match(32);
			setState(293); corpodafuncao();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CorpodafuncaoContext extends ParserRuleContext {
		public ListaparContext listapar() {
			return getRuleContext(ListaparContext.class,0);
		}
		public BlocoContext bloco() {
			return getRuleContext(BlocoContext.class,0);
		}
		public CorpodafuncaoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_corpodafuncao; }
	}

	public final CorpodafuncaoContext corpodafuncao() throws RecognitionException {
		CorpodafuncaoContext _localctx = new CorpodafuncaoContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_corpodafuncao);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295); match(11);
			setState(297);
			_la = _input.LA(1);
			if (_la==20 || _la==NOME) {
				{
				setState(296); listapar();
				}
			}

			setState(299); match(33);
			setState(300); bloco();
			setState(301); match(46);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListaparContext extends ParserRuleContext {
		public ListadenomesContext listadenomes;
		public ListadenomesContext listadenomes() {
			return getRuleContext(ListadenomesContext.class,0);
		}
		public ListaparContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listapar; }
	}

	public final ListaparContext listapar() throws RecognitionException {
		ListaparContext _localctx = new ListaparContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_listapar);
		int _la;
		try {
			setState(311);
			switch (_input.LA(1)) {
			case NOME:
				enterOuterAlt(_localctx, 1);
				{
				setState(303); ((ListaparContext)_localctx).listadenomes = listadenomes();
				setState(306);
				_la = _input.LA(1);
				if (_la==4) {
					{
					setState(304); match(4);
					setState(305); match(20);
					}
				}

				 pilhaDeTabelas.topo().adicionarSimbolos(((ListaparContext)_localctx).listadenomes.nomes, "parametro"); 
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 2);
				{
				setState(310); match(20);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstrutortabelaContext extends ParserRuleContext {
		public ListadecamposContext listadecampos() {
			return getRuleContext(ListadecamposContext.class,0);
		}
		public ConstrutortabelaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_construtortabela; }
	}

	public final ConstrutortabelaContext construtortabela() throws RecognitionException {
		ConstrutortabelaContext _localctx = new ConstrutortabelaContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_construtortabela);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(313); match(21);
			setState(315);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 5) | (1L << 7) | (1L << 11) | (1L << 12) | (1L << 15) | (1L << 18) | (1L << 20) | (1L << 21) | (1L << 26) | (1L << 32) | (1L << 45) | (1L << NOME) | (1L << CADEIA) | (1L << NUMERO))) != 0)) {
				{
				setState(314); listadecampos();
				}
			}

			setState(317); match(25);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListadecamposContext extends ParserRuleContext {
		public List<CampoContext> campo() {
			return getRuleContexts(CampoContext.class);
		}
		public CampoContext campo(int i) {
			return getRuleContext(CampoContext.class,i);
		}
		public List<SeparadordecamposContext> separadordecampos() {
			return getRuleContexts(SeparadordecamposContext.class);
		}
		public SeparadordecamposContext separadordecampos(int i) {
			return getRuleContext(SeparadordecamposContext.class,i);
		}
		public ListadecamposContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listadecampos; }
	}

	public final ListadecamposContext listadecampos() throws RecognitionException {
		ListadecamposContext _localctx = new ListadecamposContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_listadecampos);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(319); campo();
			setState(325);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(320); separadordecampos();
					setState(321); campo();
					}
					} 
				}
				setState(327);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			}
			setState(329);
			_la = _input.LA(1);
			if (_la==4 || _la==38) {
				{
				setState(328); separadordecampos();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CampoContext extends ParserRuleContext {
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode NOME() { return getToken(LuazinhaParser.NOME, 0); }
		public CampoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_campo; }
	}

	public final CampoContext campo() throws RecognitionException {
		CampoContext _localctx = new CampoContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_campo);
		try {
			setState(341);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(331); match(5);
				setState(332); exp(0);
				setState(333); match(1);
				setState(334); match(37);
				setState(335); exp(0);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(337); match(NOME);
				setState(338); match(37);
				setState(339); exp(0);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(340); exp(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SeparadordecamposContext extends ParserRuleContext {
		public SeparadordecamposContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_separadordecampos; }
	}

	public final SeparadordecamposContext separadordecampos() throws RecognitionException {
		SeparadordecamposContext _localctx = new SeparadordecamposContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_separadordecampos);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(343);
			_la = _input.LA(1);
			if ( !(_la==4 || _la==38) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OpbinContext extends ParserRuleContext {
		public OpbinContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_opbin; }
	}

	public final OpbinContext opbin() throws RecognitionException {
		OpbinContext _localctx = new OpbinContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_opbin);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(345);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 7) | (1L << 8) | (1L << 9) | (1L << 14) | (1L << 16) | (1L << 19) | (1L << 23) | (1L << 29) | (1L << 30) | (1L << 34) | (1L << 40) | (1L << 42) | (1L << 43) | (1L << 44) | (1L << 47))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OpunariaContext extends ParserRuleContext {
		public OpunariaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_opunaria; }
	}

	public final OpunariaContext opunaria() throws RecognitionException {
		OpunariaContext _localctx = new OpunariaContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_opunaria);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 7) | (1L << 12) | (1L << 45))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 10: return exp_sempred((ExpContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean exp_sempred(ExpContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return 2 >= _localctx._p;
		}
		return true;
	}

	public static final String _serializedATN =
		"\2\3\66\u0160\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b"+
		"\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t"+
		"\20\4\21\t\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t"+
		"\27\4\30\t\30\4\31\t\31\3\2\3\2\3\2\3\2\3\3\3\3\5\39\n\3\7\3;\n\3\f\3"+
		"\16\3>\13\3\3\3\3\3\5\3B\n\3\5\3D\n\3\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\7\5f\n\5\f\5\16\5i\13\5\3\5\3\5\5\5m\n\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5{\n\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5\u009e\n\5\3\5\3\5"+
		"\5\5\u00a2\n\5\3\6\3\6\5\6\u00a6\n\6\3\6\5\6\u00a9\n\6\3\7\3\7\3\7\3\7"+
		"\3\7\7\7\u00b0\n\7\f\7\16\7\u00b3\13\7\3\7\3\7\3\7\5\7\u00b8\n\7\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\7\b\u00c0\n\b\f\b\16\b\u00c3\13\b\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\5\t\u00d0\n\t\3\n\3\n\3\n\3\n\3\n\7\n\u00d7"+
		"\n\n\f\n\16\n\u00da\13\n\3\13\3\13\3\13\7\13\u00df\n\13\f\13\16\13\u00e2"+
		"\13\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5"+
		"\f\u00f3\n\f\3\f\3\f\3\f\3\f\7\f\u00f9\n\f\f\f\16\f\u00fc\13\f\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\7\r\u0105\n\r\f\r\16\r\u0108\13\r\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\5\16\u0112\n\16\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\5\17\u011c\n\17\3\20\3\20\5\20\u0120\n\20\3\20\3\20\3\20\5"+
		"\20\u0125\n\20\3\21\3\21\3\21\3\22\3\22\5\22\u012c\n\22\3\22\3\22\3\22"+
		"\3\22\3\23\3\23\3\23\5\23\u0135\n\23\3\23\3\23\3\23\5\23\u013a\n\23\3"+
		"\24\3\24\5\24\u013e\n\24\3\24\3\24\3\25\3\25\3\25\3\25\7\25\u0146\n\25"+
		"\f\25\16\25\u0149\13\25\3\25\5\25\u014c\n\25\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\5\26\u0158\n\26\3\27\3\27\3\30\3\30\3\31\3\31"+
		"\3\31\2\32\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\2\5\4\6"+
		"\6((\f\t\13\20\20\22\22\25\25\31\31\37 $$**,.\61\61\5\t\t\16\16//\u017c"+
		"\2\62\3\2\2\2\4<\3\2\2\2\6E\3\2\2\2\b\u00a1\3\2\2\2\n\u00a8\3\2\2\2\f"+
		"\u00aa\3\2\2\2\16\u00b9\3\2\2\2\20\u00cf\3\2\2\2\22\u00d1\3\2\2\2\24\u00e0"+
		"\3\2\2\2\26\u00f2\3\2\2\2\30\u00fd\3\2\2\2\32\u0111\3\2\2\2\34\u011b\3"+
		"\2\2\2\36\u0124\3\2\2\2 \u0126\3\2\2\2\"\u0129\3\2\2\2$\u0139\3\2\2\2"+
		"&\u013b\3\2\2\2(\u0141\3\2\2\2*\u0157\3\2\2\2,\u0159\3\2\2\2.\u015b\3"+
		"\2\2\2\60\u015d\3\2\2\2\62\63\b\2\1\2\63\64\5\4\3\2\64\65\b\2\1\2\65\3"+
		"\3\2\2\2\668\5\b\5\2\679\7(\2\28\67\3\2\2\289\3\2\2\29;\3\2\2\2:\66\3"+
		"\2\2\2;>\3\2\2\2<:\3\2\2\2<=\3\2\2\2=C\3\2\2\2><\3\2\2\2?A\5\n\6\2@B\7"+
		"(\2\2A@\3\2\2\2AB\3\2\2\2BD\3\2\2\2C?\3\2\2\2CD\3\2\2\2D\5\3\2\2\2EF\5"+
		"\4\3\2F\7\3\2\2\2GH\5\16\b\2HI\7\'\2\2IJ\5\24\13\2JK\b\5\1\2K\u00a2\3"+
		"\2\2\2L\u00a2\5\34\17\2MN\7\36\2\2NO\5\6\4\2OP\7\60\2\2P\u00a2\3\2\2\2"+
		"QR\7\b\2\2RS\5\26\f\2ST\7\36\2\2TU\5\6\4\2UV\7\60\2\2V\u00a2\3\2\2\2W"+
		"X\7)\2\2XY\5\6\4\2YZ\7\23\2\2Z[\5\26\f\2[\u00a2\3\2\2\2\\]\7\17\2\2]^"+
		"\5\26\f\2^_\7+\2\2_g\5\6\4\2`a\7\35\2\2ab\5\26\f\2bc\7+\2\2cd\5\6\4\2"+
		"df\3\2\2\2e`\3\2\2\2fi\3\2\2\2ge\3\2\2\2gh\3\2\2\2hl\3\2\2\2ig\3\2\2\2"+
		"jk\7\32\2\2km\5\6\4\2lj\3\2\2\2lm\3\2\2\2mn\3\2\2\2no\7\60\2\2o\u00a2"+
		"\3\2\2\2pq\7%\2\2qr\b\5\1\2rs\7\62\2\2st\b\5\1\2tu\7\'\2\2uv\5\26\f\2"+
		"vw\7\6\2\2wz\5\26\f\2xy\7\6\2\2y{\5\26\f\2zx\3\2\2\2z{\3\2\2\2{|\3\2\2"+
		"\2|}\7\36\2\2}~\5\6\4\2~\177\b\5\1\2\177\u0080\7\60\2\2\u0080\u00a2\3"+
		"\2\2\2\u0081\u0082\7%\2\2\u0082\u0083\b\5\1\2\u0083\u0084\5\22\n\2\u0084"+
		"\u0085\7\4\2\2\u0085\u0086\5\24\13\2\u0086\u0087\b\5\1\2\u0087\u0088\7"+
		"\36\2\2\u0088\u0089\5\6\4\2\u0089\u008a\b\5\1\2\u008a\u008b\7\60\2\2\u008b"+
		"\u00a2\3\2\2\2\u008c\u008d\7\"\2\2\u008d\u008e\5\f\7\2\u008e\u008f\b\5"+
		"\1\2\u008f\u0090\5\"\22\2\u0090\u0091\b\5\1\2\u0091\u00a2\3\2\2\2\u0092"+
		"\u0093\7\5\2\2\u0093\u0094\7\"\2\2\u0094\u0095\7\62\2\2\u0095\u0096\b"+
		"\5\1\2\u0096\u0097\5\"\22\2\u0097\u0098\b\5\1\2\u0098\u00a2\3\2\2\2\u0099"+
		"\u009a\7\5\2\2\u009a\u009d\5\22\n\2\u009b\u009c\7\'\2\2\u009c\u009e\5"+
		"\24\13\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u009f\3\2\2\2\u009f"+
		"\u00a0\b\5\1\2\u00a0\u00a2\3\2\2\2\u00a1G\3\2\2\2\u00a1L\3\2\2\2\u00a1"+
		"M\3\2\2\2\u00a1Q\3\2\2\2\u00a1W\3\2\2\2\u00a1\\\3\2\2\2\u00a1p\3\2\2\2"+
		"\u00a1\u0081\3\2\2\2\u00a1\u008c\3\2\2\2\u00a1\u0092\3\2\2\2\u00a1\u0099"+
		"\3\2\2\2\u00a2\t\3\2\2\2\u00a3\u00a5\7&\2\2\u00a4\u00a6\5\24\13\2\u00a5"+
		"\u00a4\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a9\3\2\2\2\u00a7\u00a9\7\30"+
		"\2\2\u00a8\u00a3\3\2\2\2\u00a8\u00a7\3\2\2\2\u00a9\13\3\2\2\2\u00aa\u00ab"+
		"\7\62\2\2\u00ab\u00b1\b\7\1\2\u00ac\u00ad\7!\2\2\u00ad\u00ae\7\62\2\2"+
		"\u00ae\u00b0\b\7\1\2\u00af\u00ac\3\2\2\2\u00b0\u00b3\3\2\2\2\u00b1\u00af"+
		"\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b7\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b4"+
		"\u00b5\7\f\2\2\u00b5\u00b6\7\62\2\2\u00b6\u00b8\b\7\1\2\u00b7\u00b4\3"+
		"\2\2\2\u00b7\u00b8\3\2\2\2\u00b8\r\3\2\2\2\u00b9\u00ba\5\20\t\2\u00ba"+
		"\u00c1\b\b\1\2\u00bb\u00bc\7\6\2\2\u00bc\u00bd\5\20\t\2\u00bd\u00be\b"+
		"\b\1\2\u00be\u00c0\3\2\2\2\u00bf\u00bb\3\2\2\2\u00c0\u00c3\3\2\2\2\u00c1"+
		"\u00bf\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\17\3\2\2\2\u00c3\u00c1\3\2\2"+
		"\2\u00c4\u00c5\7\62\2\2\u00c5\u00d0\b\t\1\2\u00c6\u00c7\5\30\r\2\u00c7"+
		"\u00c8\7\7\2\2\u00c8\u00c9\5\26\f\2\u00c9\u00ca\7\3\2\2\u00ca\u00d0\3"+
		"\2\2\2\u00cb\u00cc\5\30\r\2\u00cc\u00cd\7!\2\2\u00cd\u00ce\7\62\2\2\u00ce"+
		"\u00d0\3\2\2\2\u00cf\u00c4\3\2\2\2\u00cf\u00c6\3\2\2\2\u00cf\u00cb\3\2"+
		"\2\2\u00d0\21\3\2\2\2\u00d1\u00d2\7\62\2\2\u00d2\u00d8\b\n\1\2\u00d3\u00d4"+
		"\7\6\2\2\u00d4\u00d5\7\62\2\2\u00d5\u00d7\b\n\1\2\u00d6\u00d3\3\2\2\2"+
		"\u00d7\u00da\3\2\2\2\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\23"+
		"\3\2\2\2\u00da\u00d8\3\2\2\2\u00db\u00dc\5\26\f\2\u00dc\u00dd\7\6\2\2"+
		"\u00dd\u00df\3\2\2\2\u00de\u00db\3\2\2\2\u00df\u00e2\3\2\2\2\u00e0\u00de"+
		"\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00e3\3\2\2\2\u00e2\u00e0\3\2\2\2\u00e3"+
		"\u00e4\5\26\f\2\u00e4\25\3\2\2\2\u00e5\u00e6\b\f\1\2\u00e6\u00e7\5\60"+
		"\31\2\u00e7\u00e8\5\26\f\2\u00e8\u00f3\3\2\2\2\u00e9\u00f3\7\24\2\2\u00ea"+
		"\u00f3\7\21\2\2\u00eb\u00f3\7\34\2\2\u00ec\u00f3\7\64\2\2\u00ed\u00f3"+
		"\7\63\2\2\u00ee\u00f3\7\26\2\2\u00ef\u00f3\5 \21\2\u00f0\u00f3\5\32\16"+
		"\2\u00f1\u00f3\5&\24\2\u00f2\u00e5\3\2\2\2\u00f2\u00e9\3\2\2\2\u00f2\u00ea"+
		"\3\2\2\2\u00f2\u00eb\3\2\2\2\u00f2\u00ec\3\2\2\2\u00f2\u00ed\3\2\2\2\u00f2"+
		"\u00ee\3\2\2\2\u00f2\u00ef\3\2\2\2\u00f2\u00f0\3\2\2\2\u00f2\u00f1\3\2"+
		"\2\2\u00f3\u00fa\3\2\2\2\u00f4\u00f5\6\f\2\3\u00f5\u00f6\5.\30\2\u00f6"+
		"\u00f7\5\26\f\2\u00f7\u00f9\3\2\2\2\u00f8\u00f4\3\2\2\2\u00f9\u00fc\3"+
		"\2\2\2\u00fa\u00f8\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\27\3\2\2\2\u00fc"+
		"\u00fa\3\2\2\2\u00fd\u0106\7\62\2\2\u00fe\u00ff\7\7\2\2\u00ff\u0100\5"+
		"\26\f\2\u0100\u0101\7\3\2\2\u0101\u0105\3\2\2\2\u0102\u0103\7!\2\2\u0103"+
		"\u0105\7\62\2\2\u0104\u00fe\3\2\2\2\u0104\u0102\3\2\2\2\u0105\u0108\3"+
		"\2\2\2\u0106\u0104\3\2\2\2\u0106\u0107\3\2\2\2\u0107\31\3\2\2\2\u0108"+
		"\u0106\3\2\2\2\u0109\u010a\5\20\t\2\u010a\u010b\b\16\1\2\u010b\u0112\3"+
		"\2\2\2\u010c\u0112\5\34\17\2\u010d\u010e\7\r\2\2\u010e\u010f\5\26\f\2"+
		"\u010f\u0110\7#\2\2\u0110\u0112\3\2\2\2\u0111\u0109\3\2\2\2\u0111\u010c"+
		"\3\2\2\2\u0111\u010d\3\2\2\2\u0112\33\3\2\2\2\u0113\u0114\5\30\r\2\u0114"+
		"\u0115\5\36\20\2\u0115\u011c\3\2\2\2\u0116\u0117\5\30\r\2\u0117\u0118"+
		"\7\f\2\2\u0118\u0119\7\62\2\2\u0119\u011a\5\36\20\2\u011a\u011c\3\2\2"+
		"\2\u011b\u0113\3\2\2\2\u011b\u0116\3\2\2\2\u011c\35\3\2\2\2\u011d\u011f"+
		"\7\r\2\2\u011e\u0120\5\24\13\2\u011f\u011e\3\2\2\2\u011f\u0120\3\2\2\2"+
		"\u0120\u0121\3\2\2\2\u0121\u0125\7#\2\2\u0122\u0125\5&\24\2\u0123\u0125"+
		"\7\63\2\2\u0124\u011d\3\2\2\2\u0124\u0122\3\2\2\2\u0124\u0123\3\2\2\2"+
		"\u0125\37\3\2\2\2\u0126\u0127\7\"\2\2\u0127\u0128\5\"\22\2\u0128!\3\2"+
		"\2\2\u0129\u012b\7\r\2\2\u012a\u012c\5$\23\2\u012b\u012a\3\2\2\2\u012b"+
		"\u012c\3\2\2\2\u012c\u012d\3\2\2\2\u012d\u012e\7#\2\2\u012e\u012f\5\6"+
		"\4\2\u012f\u0130\7\60\2\2\u0130#\3\2\2\2\u0131\u0134\5\22\n\2\u0132\u0133"+
		"\7\6\2\2\u0133\u0135\7\26\2\2\u0134\u0132\3\2\2\2\u0134\u0135\3\2\2\2"+
		"\u0135\u0136\3\2\2\2\u0136\u0137\b\23\1\2\u0137\u013a\3\2\2\2\u0138\u013a"+
		"\7\26\2\2\u0139\u0131\3\2\2\2\u0139\u0138\3\2\2\2\u013a%\3\2\2\2\u013b"+
		"\u013d\7\27\2\2\u013c\u013e\5(\25\2\u013d\u013c\3\2\2\2\u013d\u013e\3"+
		"\2\2\2\u013e\u013f\3\2\2\2\u013f\u0140\7\33\2\2\u0140\'\3\2\2\2\u0141"+
		"\u0147\5*\26\2\u0142\u0143\5,\27\2\u0143\u0144\5*\26\2\u0144\u0146\3\2"+
		"\2\2\u0145\u0142\3\2\2\2\u0146\u0149\3\2\2\2\u0147\u0145\3\2\2\2\u0147"+
		"\u0148\3\2\2\2\u0148\u014b\3\2\2\2\u0149\u0147\3\2\2\2\u014a\u014c\5,"+
		"\27\2\u014b\u014a\3\2\2\2\u014b\u014c\3\2\2\2\u014c)\3\2\2\2\u014d\u014e"+
		"\7\7\2\2\u014e\u014f\5\26\f\2\u014f\u0150\7\3\2\2\u0150\u0151\7\'\2\2"+
		"\u0151\u0152\5\26\f\2\u0152\u0158\3\2\2\2\u0153\u0154\7\62\2\2\u0154\u0155"+
		"\7\'\2\2\u0155\u0158\5\26\f\2\u0156\u0158\5\26\f\2\u0157\u014d\3\2\2\2"+
		"\u0157\u0153\3\2\2\2\u0157\u0156\3\2\2\2\u0158+\3\2\2\2\u0159\u015a\t"+
		"\2\2\2\u015a-\3\2\2\2\u015b\u015c\t\3\2\2\u015c/\3\2\2\2\u015d\u015e\t"+
		"\4\2\2\u015e\61\3\2\2\2\"8<ACglz\u009d\u00a1\u00a5\u00a8\u00b1\u00b7\u00c1"+
		"\u00cf\u00d8\u00e0\u00f2\u00fa\u0104\u0106\u0111\u011b\u011f\u0124\u012b"+
		"\u0134\u0139\u013d\u0147\u014b\u0157";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}