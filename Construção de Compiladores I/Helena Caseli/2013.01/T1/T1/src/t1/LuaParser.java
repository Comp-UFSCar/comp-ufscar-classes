// Generated from /mnt/Windows/Users/Thales/Dropbox/Faculdade/5o Semestre/Compiladores I/T1/T1/src/t1/Lua.g4 by ANTLR 4.0
package t1;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LuaParser extends Parser {
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
		T__1=46, T__0=47, PALAVRAS_RESERVADAS=48, SIMBOLOS_RESERVADOS=49, NOME=50, 
		NUMERO=51, CADEIA=52, COMENTARIO=53, WS=54;
	public static final String[] tokenNames = {
		"<INVALID>", "']'", "'in'", "'local'", "','", "'['", "'while'", "'-'", 
		"'*'", "'or'", "':'", "'('", "'not'", "'if'", "'true '", "'<'", "'false'", 
		"'<='", "'until'", "'nil'", "'~='", "'...'", "'{'", "'break'", "'and'", 
		"'else'", "'}'", "'elseif'", "'do'", "'%'", "'^'", "'.'", "'function'", 
		"')'", "'+'", "'for'", "'return'", "'='", "';'", "'repeat'", "'>'", "'then'", 
		"'/'", "'=='", "'>='", "'#'", "'end'", "'..'", "PALAVRAS_RESERVADAS", 
		"SIMBOLOS_RESERVADOS", "NOME", "NUMERO", "CADEIA", "COMENTARIO", "WS"
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
	public String getGrammarFileName() { return "Lua.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public LuaParser(TokenStream input) {
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
			setState(48); trecho();
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
			setState(56);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 3) | (1L << 6) | (1L << 13) | (1L << 28) | (1L << 32) | (1L << 35) | (1L << 39) | (1L << NOME))) != 0)) {
				{
				{
				setState(50); comando();
				setState(52);
				_la = _input.LA(1);
				if (_la==38) {
					{
					setState(51); match(38);
					}
				}

				}
				}
				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(63);
			_la = _input.LA(1);
			if (_la==23 || _la==36) {
				{
				setState(59); ultimocomando();
				setState(61);
				_la = _input.LA(1);
				if (_la==38) {
					{
					setState(60); match(38);
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
			setState(65); trecho();
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
		public TerminalNode NOME() { return getToken(LuaParser.NOME, 0); }
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
			setState(143);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(67); listavar();
				setState(68); match(37);
				setState(69); listaexp();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(71); chamadadefuncao();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(72); match(28);
				setState(73); bloco();
				setState(74); match(46);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(76); match(6);
				setState(77); exp(0);
				setState(78); match(28);
				setState(79); bloco();
				setState(80); match(46);
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(82); match(39);
				setState(83); bloco();
				setState(84); match(18);
				setState(85); exp(0);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(87); match(13);
				setState(88); exp(0);
				setState(89); match(41);
				setState(90); bloco();
				setState(98);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==27) {
					{
					{
					setState(91); match(27);
					setState(92); exp(0);
					setState(93); match(41);
					setState(94); bloco();
					}
					}
					setState(100);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(103);
				_la = _input.LA(1);
				if (_la==25) {
					{
					setState(101); match(25);
					setState(102); bloco();
					}
				}

				setState(105); match(46);
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(107); match(35);
				setState(108); match(NOME);
				setState(109); match(37);
				setState(110); exp(0);
				setState(111); match(4);
				setState(112); exp(0);
				setState(115);
				_la = _input.LA(1);
				if (_la==4) {
					{
					setState(113); match(4);
					setState(114); exp(0);
					}
				}

				setState(117); match(28);
				setState(118); bloco();
				setState(119); match(46);
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(121); match(35);
				setState(122); listadenomes();
				setState(123); match(2);
				setState(124); listaexp();
				setState(125); match(28);
				setState(126); bloco();
				setState(127); match(46);
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(129); match(32);
				setState(130); nomedafuncao();
				setState(131); corpodafuncao();
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(133); match(3);
				setState(134); match(32);
				setState(135); match(NOME);
				setState(136); corpodafuncao();
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(137); match(3);
				setState(138); listadenomes();
				setState(141);
				_la = _input.LA(1);
				if (_la==37) {
					{
					setState(139); match(37);
					setState(140); listaexp();
					}
				}

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
			setState(150);
			switch (_input.LA(1)) {
			case 36:
				enterOuterAlt(_localctx, 1);
				{
				setState(145); match(36);
				setState(147);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 7) | (1L << 11) | (1L << 12) | (1L << 14) | (1L << 16) | (1L << 19) | (1L << 21) | (1L << 22) | (1L << 32) | (1L << 45) | (1L << NOME) | (1L << NUMERO) | (1L << CADEIA))) != 0)) {
					{
					setState(146); listaexp();
					}
				}

				}
				break;
			case 23:
				enterOuterAlt(_localctx, 2);
				{
				setState(149); match(23);
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
		public Token NOME;
		public TerminalNode NOME(int i) {
			return getToken(LuaParser.NOME, i);
		}
		public List<TerminalNode> NOME() { return getTokens(LuaParser.NOME); }
		public NomedafuncaoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nomedafuncao; }
	}

	public final NomedafuncaoContext nomedafuncao() throws RecognitionException {
		NomedafuncaoContext _localctx = new NomedafuncaoContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_nomedafuncao);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152); ((NomedafuncaoContext)_localctx).NOME = match(NOME);
			TabelaDeSimbolos.adicionarSimbolo((((NomedafuncaoContext)_localctx).NOME!=null?((NomedafuncaoContext)_localctx).NOME.getText():null), Tipo.FUNCAO);
			setState(158);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==31) {
				{
				{
				setState(154); match(31);
				setState(155); ((NomedafuncaoContext)_localctx).NOME = match(NOME);
				}
				}
				setState(160);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(163);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(161); match(10);
				setState(162); ((NomedafuncaoContext)_localctx).NOME = match(NOME);
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
		public VarContext var;
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(165); ((ListavarContext)_localctx).var = var();
			TabelaDeSimbolos.adicionarSimbolo((((ListavarContext)_localctx).var!=null?_input.getText(((ListavarContext)_localctx).var.start,((ListavarContext)_localctx).var.stop):null), Tipo.VARIAVEL);
			setState(173);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==4) {
				{
				{
				setState(167); match(4);
				setState(168); ((ListavarContext)_localctx).var = var();
				TabelaDeSimbolos.adicionarSimbolo((((ListavarContext)_localctx).var!=null?_input.getText(((ListavarContext)_localctx).var.start,((ListavarContext)_localctx).var.stop):null), Tipo.VARIAVEL);
				}
				}
				setState(175);
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
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode NOME() { return getToken(LuaParser.NOME, 0); }
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
			setState(186);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(176); match(NOME);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(177); expprefixo();
				setState(178); match(5);
				setState(179); exp(0);
				setState(180); match(1);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(182); expprefixo();
				setState(183); match(31);
				setState(184); match(NOME);
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
		public TerminalNode NOME(int i) {
			return getToken(LuaParser.NOME, i);
		}
		public List<TerminalNode> NOME() { return getTokens(LuaParser.NOME); }
		public ListadenomesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listadenomes; }
	}

	public final ListadenomesContext listadenomes() throws RecognitionException {
		ListadenomesContext _localctx = new ListadenomesContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_listadenomes);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(188); match(NOME);
			setState(193);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(189); match(4);
					setState(190); match(NOME);
					}
					} 
				}
				setState(195);
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
			setState(201);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(196); exp(0);
					setState(197); match(4);
					}
					} 
				}
				setState(203);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			setState(204); exp(0);
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
		public TerminalNode NUMERO() { return getToken(LuaParser.NUMERO, 0); }
		public TerminalNode CADEIA() { return getToken(LuaParser.CADEIA, 0); }
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public ExpprefixoContext expprefixo() {
			return getRuleContext(ExpprefixoContext.class,0);
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
			setState(220);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				setState(207); opunaria();
				setState(208); exp(1);
				}
				break;

			case 2:
				{
				setState(210); match(19);
				}
				break;

			case 3:
				{
				setState(211); match(16);
				}
				break;

			case 4:
				{
				setState(212); match(14);
				}
				break;

			case 5:
				{
				setState(213); match(NUMERO);
				}
				break;

			case 6:
				{
				setState(214); match(CADEIA);
				}
				break;

			case 7:
				{
				setState(215); match(21);
				}
				break;

			case 8:
				{
				setState(216); funcao();
				}
				break;

			case 9:
				{
				setState(217); expprefixo();
				}
				break;

			case 10:
				{
				setState(218); expprefixo2();
				}
				break;

			case 11:
				{
				setState(219); construtortabela();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(228);
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
					setState(222);
					if (!(2 >= _localctx._p)) throw new FailedPredicateException(this, "2 >= $_p");
					setState(223); opbin();
					setState(224); exp(0);
					}
					} 
				}
				setState(230);
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
			return getToken(LuaParser.NOME, i);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public List<TerminalNode> NOME() { return getTokens(LuaParser.NOME); }
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
			setState(231); match(NOME);
			setState(240);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					setState(238);
					switch (_input.LA(1)) {
					case 5:
						{
						setState(232); match(5);
						setState(233); exp(0);
						setState(234); match(1);
						}
						break;
					case 31:
						{
						setState(236); match(31);
						setState(237); match(NOME);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(242);
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
			setState(251);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(243); ((Expprefixo2Context)_localctx).var = var();
				TabelaDeSimbolos.adicionarSimbolo((((Expprefixo2Context)_localctx).var!=null?_input.getText(((Expprefixo2Context)_localctx).var.start,((Expprefixo2Context)_localctx).var.stop):null), Tipo.VARIAVEL);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(246); chamadadefuncao();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(247); match(11);
				setState(248); exp(0);
				setState(249); match(33);
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
		public TerminalNode NOME() { return getToken(LuaParser.NOME, 0); }
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
			setState(261);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(253); expprefixo();
				setState(254); args();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(256); expprefixo();
				setState(257); match(10);
				setState(258); match(NOME);
				setState(259); args();
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
		public TerminalNode CADEIA() { return getToken(LuaParser.CADEIA, 0); }
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
			setState(270);
			switch (_input.LA(1)) {
			case 11:
				enterOuterAlt(_localctx, 1);
				{
				setState(263); match(11);
				setState(265);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 7) | (1L << 11) | (1L << 12) | (1L << 14) | (1L << 16) | (1L << 19) | (1L << 21) | (1L << 22) | (1L << 32) | (1L << 45) | (1L << NOME) | (1L << NUMERO) | (1L << CADEIA))) != 0)) {
					{
					setState(264); listaexp();
					}
				}

				setState(267); match(33);
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 2);
				{
				setState(268); construtortabela();
				}
				break;
			case CADEIA:
				enterOuterAlt(_localctx, 3);
				{
				setState(269); match(CADEIA);
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
			setState(272); match(32);
			setState(273); corpodafuncao();
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
			setState(275); match(11);
			setState(277);
			_la = _input.LA(1);
			if (_la==21 || _la==NOME) {
				{
				setState(276); listapar();
				}
			}

			setState(279); match(33);
			setState(280); bloco();
			setState(281); match(46);
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
			setState(289);
			switch (_input.LA(1)) {
			case NOME:
				enterOuterAlt(_localctx, 1);
				{
				setState(283); listadenomes();
				setState(286);
				_la = _input.LA(1);
				if (_la==4) {
					{
					setState(284); match(4);
					setState(285); match(21);
					}
				}

				}
				break;
			case 21:
				enterOuterAlt(_localctx, 2);
				{
				setState(288); match(21);
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
			setState(291); match(22);
			setState(293);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 5) | (1L << 7) | (1L << 11) | (1L << 12) | (1L << 14) | (1L << 16) | (1L << 19) | (1L << 21) | (1L << 22) | (1L << 32) | (1L << 45) | (1L << NOME) | (1L << NUMERO) | (1L << CADEIA))) != 0)) {
				{
				setState(292); listadecampos();
				}
			}

			setState(295); match(26);
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
			setState(297); campo();
			setState(303);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(298); separadordecampos();
					setState(299); campo();
					}
					} 
				}
				setState(305);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			}
			setState(307);
			_la = _input.LA(1);
			if (_la==4 || _la==38) {
				{
				setState(306); separadordecampos();
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
		public TerminalNode NOME() { return getToken(LuaParser.NOME, 0); }
		public CampoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_campo; }
	}

	public final CampoContext campo() throws RecognitionException {
		CampoContext _localctx = new CampoContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_campo);
		try {
			setState(319);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(309); match(5);
				setState(310); exp(0);
				setState(311); match(1);
				setState(312); match(37);
				setState(313); exp(0);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(315); match(NOME);
				setState(316); match(37);
				setState(317); exp(0);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(318); exp(0);
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
			setState(321);
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
			setState(323);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 7) | (1L << 8) | (1L << 9) | (1L << 15) | (1L << 17) | (1L << 20) | (1L << 24) | (1L << 29) | (1L << 30) | (1L << 34) | (1L << 40) | (1L << 42) | (1L << 43) | (1L << 44) | (1L << 47))) != 0)) ) {
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
			setState(325);
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
		"\2\38\u014a\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4"+
		"\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20"+
		"\4\21\t\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27"+
		"\4\30\t\30\4\31\t\31\3\2\3\2\3\3\3\3\5\3\67\n\3\7\39\n\3\f\3\16\3<\13"+
		"\3\3\3\3\3\5\3@\n\3\5\3B\n\3\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\7\5c\n\5\f\5\16\5f\13\5\3\5\3\5\5\5j\n\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\5\5v\n\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5\u0090"+
		"\n\5\5\5\u0092\n\5\3\6\3\6\5\6\u0096\n\6\3\6\5\6\u0099\n\6\3\7\3\7\3\7"+
		"\3\7\7\7\u009f\n\7\f\7\16\7\u00a2\13\7\3\7\3\7\5\7\u00a6\n\7\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\7\b\u00ae\n\b\f\b\16\b\u00b1\13\b\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\5\t\u00bd\n\t\3\n\3\n\3\n\7\n\u00c2\n\n\f\n\16\n\u00c5"+
		"\13\n\3\13\3\13\3\13\7\13\u00ca\n\13\f\13\16\13\u00cd\13\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00df\n\f"+
		"\3\f\3\f\3\f\3\f\7\f\u00e5\n\f\f\f\16\f\u00e8\13\f\3\r\3\r\3\r\3\r\3\r"+
		"\3\r\3\r\7\r\u00f1\n\r\f\r\16\r\u00f4\13\r\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\5\16\u00fe\n\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\5\17\u0108\n\17\3\20\3\20\5\20\u010c\n\20\3\20\3\20\3\20\5\20\u0111\n"+
		"\20\3\21\3\21\3\21\3\22\3\22\5\22\u0118\n\22\3\22\3\22\3\22\3\22\3\23"+
		"\3\23\3\23\5\23\u0121\n\23\3\23\5\23\u0124\n\23\3\24\3\24\5\24\u0128\n"+
		"\24\3\24\3\24\3\25\3\25\3\25\3\25\7\25\u0130\n\25\f\25\16\25\u0133\13"+
		"\25\3\25\5\25\u0136\n\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\5\26\u0142\n\26\3\27\3\27\3\30\3\30\3\31\3\31\3\31\2\32\2\4\6\b"+
		"\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\2\5\4\6\6((\f\t\13\21\21\23"+
		"\23\26\26\32\32\37 $$**,.\61\61\5\t\t\16\16//\u0167\2\62\3\2\2\2\4:\3"+
		"\2\2\2\6C\3\2\2\2\b\u0091\3\2\2\2\n\u0098\3\2\2\2\f\u009a\3\2\2\2\16\u00a7"+
		"\3\2\2\2\20\u00bc\3\2\2\2\22\u00be\3\2\2\2\24\u00cb\3\2\2\2\26\u00de\3"+
		"\2\2\2\30\u00e9\3\2\2\2\32\u00fd\3\2\2\2\34\u0107\3\2\2\2\36\u0110\3\2"+
		"\2\2 \u0112\3\2\2\2\"\u0115\3\2\2\2$\u0123\3\2\2\2&\u0125\3\2\2\2(\u012b"+
		"\3\2\2\2*\u0141\3\2\2\2,\u0143\3\2\2\2.\u0145\3\2\2\2\60\u0147\3\2\2\2"+
		"\62\63\5\4\3\2\63\3\3\2\2\2\64\66\5\b\5\2\65\67\7(\2\2\66\65\3\2\2\2\66"+
		"\67\3\2\2\2\679\3\2\2\28\64\3\2\2\29<\3\2\2\2:8\3\2\2\2:;\3\2\2\2;A\3"+
		"\2\2\2<:\3\2\2\2=?\5\n\6\2>@\7(\2\2?>\3\2\2\2?@\3\2\2\2@B\3\2\2\2A=\3"+
		"\2\2\2AB\3\2\2\2B\5\3\2\2\2CD\5\4\3\2D\7\3\2\2\2EF\5\16\b\2FG\7\'\2\2"+
		"GH\5\24\13\2H\u0092\3\2\2\2I\u0092\5\34\17\2JK\7\36\2\2KL\5\6\4\2LM\7"+
		"\60\2\2M\u0092\3\2\2\2NO\7\b\2\2OP\5\26\f\2PQ\7\36\2\2QR\5\6\4\2RS\7\60"+
		"\2\2S\u0092\3\2\2\2TU\7)\2\2UV\5\6\4\2VW\7\24\2\2WX\5\26\f\2X\u0092\3"+
		"\2\2\2YZ\7\17\2\2Z[\5\26\f\2[\\\7+\2\2\\d\5\6\4\2]^\7\35\2\2^_\5\26\f"+
		"\2_`\7+\2\2`a\5\6\4\2ac\3\2\2\2b]\3\2\2\2cf\3\2\2\2db\3\2\2\2de\3\2\2"+
		"\2ei\3\2\2\2fd\3\2\2\2gh\7\33\2\2hj\5\6\4\2ig\3\2\2\2ij\3\2\2\2jk\3\2"+
		"\2\2kl\7\60\2\2l\u0092\3\2\2\2mn\7%\2\2no\7\64\2\2op\7\'\2\2pq\5\26\f"+
		"\2qr\7\6\2\2ru\5\26\f\2st\7\6\2\2tv\5\26\f\2us\3\2\2\2uv\3\2\2\2vw\3\2"+
		"\2\2wx\7\36\2\2xy\5\6\4\2yz\7\60\2\2z\u0092\3\2\2\2{|\7%\2\2|}\5\22\n"+
		"\2}~\7\4\2\2~\177\5\24\13\2\177\u0080\7\36\2\2\u0080\u0081\5\6\4\2\u0081"+
		"\u0082\7\60\2\2\u0082\u0092\3\2\2\2\u0083\u0084\7\"\2\2\u0084\u0085\5"+
		"\f\7\2\u0085\u0086\5\"\22\2\u0086\u0092\3\2\2\2\u0087\u0088\7\5\2\2\u0088"+
		"\u0089\7\"\2\2\u0089\u008a\7\64\2\2\u008a\u0092\5\"\22\2\u008b\u008c\7"+
		"\5\2\2\u008c\u008f\5\22\n\2\u008d\u008e\7\'\2\2\u008e\u0090\5\24\13\2"+
		"\u008f\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0092\3\2\2\2\u0091E\3"+
		"\2\2\2\u0091I\3\2\2\2\u0091J\3\2\2\2\u0091N\3\2\2\2\u0091T\3\2\2\2\u0091"+
		"Y\3\2\2\2\u0091m\3\2\2\2\u0091{\3\2\2\2\u0091\u0083\3\2\2\2\u0091\u0087"+
		"\3\2\2\2\u0091\u008b\3\2\2\2\u0092\t\3\2\2\2\u0093\u0095\7&\2\2\u0094"+
		"\u0096\5\24\13\2\u0095\u0094\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0099\3"+
		"\2\2\2\u0097\u0099\7\31\2\2\u0098\u0093\3\2\2\2\u0098\u0097\3\2\2\2\u0099"+
		"\13\3\2\2\2\u009a\u009b\7\64\2\2\u009b\u00a0\b\7\1\2\u009c\u009d\7!\2"+
		"\2\u009d\u009f\7\64\2\2\u009e\u009c\3\2\2\2\u009f\u00a2\3\2\2\2\u00a0"+
		"\u009e\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00a5\3\2\2\2\u00a2\u00a0\3\2"+
		"\2\2\u00a3\u00a4\7\f\2\2\u00a4\u00a6\7\64\2\2\u00a5\u00a3\3\2\2\2\u00a5"+
		"\u00a6\3\2\2\2\u00a6\r\3\2\2\2\u00a7\u00a8\5\20\t\2\u00a8\u00af\b\b\1"+
		"\2\u00a9\u00aa\7\6\2\2\u00aa\u00ab\5\20\t\2\u00ab\u00ac\b\b\1\2\u00ac"+
		"\u00ae\3\2\2\2\u00ad\u00a9\3\2\2\2\u00ae\u00b1\3\2\2\2\u00af\u00ad\3\2"+
		"\2\2\u00af\u00b0\3\2\2\2\u00b0\17\3\2\2\2\u00b1\u00af\3\2\2\2\u00b2\u00bd"+
		"\7\64\2\2\u00b3\u00b4\5\30\r\2\u00b4\u00b5\7\7\2\2\u00b5\u00b6\5\26\f"+
		"\2\u00b6\u00b7\7\3\2\2\u00b7\u00bd\3\2\2\2\u00b8\u00b9\5\30\r\2\u00b9"+
		"\u00ba\7!\2\2\u00ba\u00bb\7\64\2\2\u00bb\u00bd\3\2\2\2\u00bc\u00b2\3\2"+
		"\2\2\u00bc\u00b3\3\2\2\2\u00bc\u00b8\3\2\2\2\u00bd\21\3\2\2\2\u00be\u00c3"+
		"\7\64\2\2\u00bf\u00c0\7\6\2\2\u00c0\u00c2\7\64\2\2\u00c1\u00bf\3\2\2\2"+
		"\u00c2\u00c5\3\2\2\2\u00c3\u00c1\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\23"+
		"\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c6\u00c7\5\26\f\2\u00c7\u00c8\7\6\2\2"+
		"\u00c8\u00ca\3\2\2\2\u00c9\u00c6\3\2\2\2\u00ca\u00cd\3\2\2\2\u00cb\u00c9"+
		"\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc\u00ce\3\2\2\2\u00cd\u00cb\3\2\2\2\u00ce"+
		"\u00cf\5\26\f\2\u00cf\25\3\2\2\2\u00d0\u00d1\b\f\1\2\u00d1\u00d2\5\60"+
		"\31\2\u00d2\u00d3\5\26\f\2\u00d3\u00df\3\2\2\2\u00d4\u00df\7\25\2\2\u00d5"+
		"\u00df\7\22\2\2\u00d6\u00df\7\20\2\2\u00d7\u00df\7\65\2\2\u00d8\u00df"+
		"\7\66\2\2\u00d9\u00df\7\27\2\2\u00da\u00df\5 \21\2\u00db\u00df\5\30\r"+
		"\2\u00dc\u00df\5\32\16\2\u00dd\u00df\5&\24\2\u00de\u00d0\3\2\2\2\u00de"+
		"\u00d4\3\2\2\2\u00de\u00d5\3\2\2\2\u00de\u00d6\3\2\2\2\u00de\u00d7\3\2"+
		"\2\2\u00de\u00d8\3\2\2\2\u00de\u00d9\3\2\2\2\u00de\u00da\3\2\2\2\u00de"+
		"\u00db\3\2\2\2\u00de\u00dc\3\2\2\2\u00de\u00dd\3\2\2\2\u00df\u00e6\3\2"+
		"\2\2\u00e0\u00e1\6\f\2\3\u00e1\u00e2\5.\30\2\u00e2\u00e3\5\26\f\2\u00e3"+
		"\u00e5\3\2\2\2\u00e4\u00e0\3\2\2\2\u00e5\u00e8\3\2\2\2\u00e6\u00e4\3\2"+
		"\2\2\u00e6\u00e7\3\2\2\2\u00e7\27\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e9\u00f2"+
		"\7\64\2\2\u00ea\u00eb\7\7\2\2\u00eb\u00ec\5\26\f\2\u00ec\u00ed\7\3\2\2"+
		"\u00ed\u00f1\3\2\2\2\u00ee\u00ef\7!\2\2\u00ef\u00f1\7\64\2\2\u00f0\u00ea"+
		"\3\2\2\2\u00f0\u00ee\3\2\2\2\u00f1\u00f4\3\2\2\2\u00f2\u00f0\3\2\2\2\u00f2"+
		"\u00f3\3\2\2\2\u00f3\31\3\2\2\2\u00f4\u00f2\3\2\2\2\u00f5\u00f6\5\20\t"+
		"\2\u00f6\u00f7\b\16\1\2\u00f7\u00fe\3\2\2\2\u00f8\u00fe\5\34\17\2\u00f9"+
		"\u00fa\7\r\2\2\u00fa\u00fb\5\26\f\2\u00fb\u00fc\7#\2\2\u00fc\u00fe\3\2"+
		"\2\2\u00fd\u00f5\3\2\2\2\u00fd\u00f8\3\2\2\2\u00fd\u00f9\3\2\2\2\u00fe"+
		"\33\3\2\2\2\u00ff\u0100\5\30\r\2\u0100\u0101\5\36\20\2\u0101\u0108\3\2"+
		"\2\2\u0102\u0103\5\30\r\2\u0103\u0104\7\f\2\2\u0104\u0105\7\64\2\2\u0105"+
		"\u0106\5\36\20\2\u0106\u0108\3\2\2\2\u0107\u00ff\3\2\2\2\u0107\u0102\3"+
		"\2\2\2\u0108\35\3\2\2\2\u0109\u010b\7\r\2\2\u010a\u010c\5\24\13\2\u010b"+
		"\u010a\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u010d\3\2\2\2\u010d\u0111\7#"+
		"\2\2\u010e\u0111\5&\24\2\u010f\u0111\7\66\2\2\u0110\u0109\3\2\2\2\u0110"+
		"\u010e\3\2\2\2\u0110\u010f\3\2\2\2\u0111\37\3\2\2\2\u0112\u0113\7\"\2"+
		"\2\u0113\u0114\5\"\22\2\u0114!\3\2\2\2\u0115\u0117\7\r\2\2\u0116\u0118"+
		"\5$\23\2\u0117\u0116\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u0119\3\2\2\2\u0119"+
		"\u011a\7#\2\2\u011a\u011b\5\6\4\2\u011b\u011c\7\60\2\2\u011c#\3\2\2\2"+
		"\u011d\u0120\5\22\n\2\u011e\u011f\7\6\2\2\u011f\u0121\7\27\2\2\u0120\u011e"+
		"\3\2\2\2\u0120\u0121\3\2\2\2\u0121\u0124\3\2\2\2\u0122\u0124\7\27\2\2"+
		"\u0123\u011d\3\2\2\2\u0123\u0122\3\2\2\2\u0124%\3\2\2\2\u0125\u0127\7"+
		"\30\2\2\u0126\u0128\5(\25\2\u0127\u0126\3\2\2\2\u0127\u0128\3\2\2\2\u0128"+
		"\u0129\3\2\2\2\u0129\u012a\7\34\2\2\u012a\'\3\2\2\2\u012b\u0131\5*\26"+
		"\2\u012c\u012d\5,\27\2\u012d\u012e\5*\26\2\u012e\u0130\3\2\2\2\u012f\u012c"+
		"\3\2\2\2\u0130\u0133\3\2\2\2\u0131\u012f\3\2\2\2\u0131\u0132\3\2\2\2\u0132"+
		"\u0135\3\2\2\2\u0133\u0131\3\2\2\2\u0134\u0136\5,\27\2\u0135\u0134\3\2"+
		"\2\2\u0135\u0136\3\2\2\2\u0136)\3\2\2\2\u0137\u0138\7\7\2\2\u0138\u0139"+
		"\5\26\f\2\u0139\u013a\7\3\2\2\u013a\u013b\7\'\2\2\u013b\u013c\5\26\f\2"+
		"\u013c\u0142\3\2\2\2\u013d\u013e\7\64\2\2\u013e\u013f\7\'\2\2\u013f\u0142"+
		"\5\26\f\2\u0140\u0142\5\26\f\2\u0141\u0137\3\2\2\2\u0141\u013d\3\2\2\2"+
		"\u0141\u0140\3\2\2\2\u0142+\3\2\2\2\u0143\u0144\t\2\2\2\u0144-\3\2\2\2"+
		"\u0145\u0146\t\3\2\2\u0146/\3\2\2\2\u0147\u0148\t\4\2\2\u0148\61\3\2\2"+
		"\2\"\66:?Adiu\u008f\u0091\u0095\u0098\u00a0\u00a5\u00af\u00bc\u00c3\u00cb"+
		"\u00de\u00e6\u00f0\u00f2\u00fd\u0107\u010b\u0110\u0117\u0120\u0123\u0127"+
		"\u0131\u0135\u0141";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}