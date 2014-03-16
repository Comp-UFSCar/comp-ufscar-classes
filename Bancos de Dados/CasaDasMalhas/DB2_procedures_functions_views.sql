-- Trabalho individual 22/5/2013
-- Lucas Oliveira David - 407917

-- O "@" foi utilizado como caractere delimitador, pois ";" esta sendo utilizado 
-- no fim dos comandos SQL (exigencia do DB2).
--
-- este arquivo deve ser executado com o seguinte comando:
--		db2 -td@ -f <CAMINHO>\407917.sql

-- comandos necessarios ao DB2

connect to casa

-- VIEW vw_compra_cliente
-- lista todos os clientes, os valores gastos por estes em compras
CREATE VIEW "LUCAS"."vw_compra_cliente"
AS 
	SELECT PES.nome AS nome, sum( IC.quantidade * P.valor_unit ) AS total_compras
	FROM pessoa PES, cliente CLI, compra COM, item_compra IC
	WHERE
		PES.id = CLI.pessoa_id AND
	    CLI.cpf = COM.cliente_cpf AND
	    COM.id = IC.compra_id
	GROUP BY CLI.nome@


-- VIEW pag_func_deste_mes
-- lista todos os pagamentos de funcionarios realizados no mes atual
CREATE VIEW "LUCAS"."vw_tbl_pag_func"
AS
	SELECT PES.nome AS nome, PAG.valor AS pagamento
	FROM pessoa PES, funcionario FUN, pagamento_funcionario PAG
	WHERE
		PES.id = FUN.pessoa_id AND
		FUN.cnpj = PAG.funcionario AND
		MONTH (PAG.data) = MONTH (CURRENT DATE)@

-- TRIGGER incremento_comissao_apos_compra
-- incrementa a comissao de um funcionario apos este realizar uma venda
-- tal incremento ee de 10 porcendo sobre a venda realizada
CREATE TRIGGER "LUCAS"."tr_inc_comissao_apos_compra"
AFTER INSERT
ON "ITEM_COMPRA"
REFERENCING NEW AS N
FOR EACH ROW MODE DB2SQL
BEGIN
    DECLARE comissao_venda REAL;
    -- retorna o valor total daquela compra
    DECLARE cur_comissao CURSOR FOR
		SELECT SUM ( P.valor_unit * IC.quantidade ) AS valor_total
		FROM item_compra IC
        WHERE N.id = IC.compra_id;

    OPEN cur_comissao;
    FETCH cur_comissao INTO comissao_venda;

    -- adiciona 10 porcento da venda realizada a
    -- comissao a ser paga ao funcionaio.
    UPDATE	funcionario
 	SET 	comissao = comissao +comissao_venda*0.1
 	WHERE	codigo = N.funcionario_codigo;

    CLOSE cur_comissao;
END@

-- CURSOR cur_comissao
-- altera a comissao de um vendedor para salario base + comissao - pagamento quando 
-- um pagamento é realizado (esse campo nao assume valores menor que zero)
CREATE TRIGGER "LUCAS"."tr_reset_comissao_apos_pag"
AFTER INSERT ON pagamento_funcionario
REFERENCING NEW AS N
FOR EACH ROW MODE DB2SQL
BEGIN
	DECLARE comissao_atual REAL;
	DECLARE salario_base   REAL;

	DECLARE cur_comissao CURSOR FOR
		SELECT comissao, salario_base
		FROM funcionario
		WHERE codigo = N.funcionario;

	OPEN cur_comissao;
    FETCH cur_comissao INTO comissao_atual, salario_base;

    IF ( N.valor < comissao_atual + salario_base ) THEN
    	UPDATE funcionario
		SET   comissao = comissao_atual + salario_base -N.valor
	 	WHERE codigo = N.funcionario;
	ELSE
		UPDATE funcionario
		SET   comissao = 0
	 	WHERE codigo = N.funcionario;
	END IF;

	CLOSE cur_comissao;
END@

-- calcula automaticamente atributo qtd_minimo de um produto,
-- utilizando como referencia as vendas realizadas naquele periodo, no ano anterior.
CREATE OR REPLACE PROCEDURE "LUCAS"."pr_deter_qtd_min"
( IN cod_produto INTEGER )
LANGUAGE SQL
BEGIN
    DECLARE media_vendas INTEGER;  
	DECLARE cur_num_vendas CURSOR FOR
		-- media das quantidades vendidas
        SELECT AVG ( qtd_vendida )
        FROM (
        	-- lista de quantidades vendidas do produto cod_produto 
        	-- no mes CURRENT MONTH dos anos anteriores
            SELECT SUM( CCP.quantidade ) AS qtd_vendida
            FROM compra C, compra_composta_produto CCP
            WHERE
                MONTH( C.data ) = MONTH( CURRENT DATE ) AND
                C.codigo = CCP.compra_codigo AND
                CCP.produto = cod_produto
            GROUP BY YEAR( C.data )
        );
	
	OPEN cur_num_vendas;
	FETCH cur_num_vendas INTO media_vendas;
   
    -- define qtd_minimo como a quantidade media encontrada
    UPDATE produto
    SET qtd_minimo = media_vendas
    WHERE codigo = cod_produto;

	CLOSE cur_num_vendas;
END@