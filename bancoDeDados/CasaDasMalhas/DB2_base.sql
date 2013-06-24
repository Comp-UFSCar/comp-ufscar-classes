-- **COMANDOS DB2**

--create database casa;
connect to casa;

-- **CRIACAO DAS TABELAS**

-- Bloco de tabelas primitivas, dependencias de outras tabelas

-- tabela endereco
CREATE TABLE endereco (
    id      INTEGER     NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    rua     VARCHAR(30) NOT NULL,
    bairro  VARCHAR(15),
    numero  INTEGER,
    complem VARCHAR(15),
    cidade  VARCHAR(15),
    estado  CHAR   (2)
);

-- tabela estado_civil
CREATE TABLE estado_civil (
    id        INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    descricao VARCHAR(20)
);

-- tabela pessoa
CREATE TABLE pessoa (
    id          INTEGER   NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    nome      VARCHAR(50) NOT NULL,
    endereco INTEGER,

    FOREIGN KEY pessoa_endereco(endereco)
        REFERENCES endereco
        ON DELETE SET NULL
);

-- tabela telefone
-- dependencias: pessoa
CREATE TABLE telefone (
    pessoa_id INTEGER     NOT NULL,
    num_tel   VARCHAR(20) NOT NULL,

    PRIMARY KEY (pessoa_id, num_tel),
    FOREIGN KEY fk_pes(pessoa_id)
        REFERENCES pessoa
        ON DELETE CASCADE
);

-- tabela tipo_produto
CREATE TABLE tipo_produto (
    id           INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    descricao VARCHAR(40)
);

-- tabela tipo_funcionario
CREATE TABLE tipo_funcionario (
    id           INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    cargo       VARCHAR(10) NOT NULL,
    descricao VARCHAR(40) NOT NULL
);

-- fim do bloco de tabelas primitivas



-- bloco de cadastros

-- tabela cliente
-- dependencias: endereco => pessoa; estado_civil
CREATE TABLE cliente (
    pessoa_id    INTEGER NOT NULL UNIQUE,

    cpf          VARCHAR(20) NOT NULL PRIMARY KEY,
    rg           VARCHAR(20),
    estado_civil INTEGER,
    filiacao_pai VARCHAR(20),
    filiacao_mae VARCHAR(20),

    FOREIGN KEY fk_cli_pessoa(pessoa_id)
        REFERENCES pessoa
        ON DELETE CASCADE,

    FOREIGN KEY fk_cli_estado_civil(estado_civil)
        REFERENCES estado_civil
        ON DELETE SET NULL
);

-- tabela fornecedor
-- dependencias: endereco => pessoa
CREATE TABLE fornecedor (
    pessoa_id  INTEGER NOT NULL UNIQUE,

    CNPJ       VARCHAR(20) NOT NULL PRIMARY KEY,

    FOREIGN KEY fk_for_pessoa(pessoa_id)
        REFERENCES pessoa
        ON DELETE CASCADE
);

-- tabela funcionario
-- dependencias: endereco => pessoa
CREATE TABLE funcionario (
    pessoa_id        INTEGER NOT NULL UNIQUE,

    cpf              VARCHAR(20) NOT NULL PRIMARY KEY,
    data_nasc        DATE,
    data_inicio      DATE,
    salario_base     REAL,
    comissao         REAL,
    tipo             INTEGER NOT NULL,

    FOREIGN KEY fk_fun_pessoa(pessoa_id)
        REFERENCES pessoa
        ON DELETE CASCADE,
    FOREIGN KEY fk_fun_tipo(tipo)
        REFERENCES tipo_funcionario
        ON DELETE NO ACTION
);

-- tabela produto
-- dependencias: tipo_produto
CREATE TABLE produto (
    codigo         INTEGER      NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    descricao      VARCHAR(200) NOT NULL,
    tamanho        CHAR         NOT NULL,
    preco_unitario REAL         NOT NULL,
    qtd_estoque    INTEGER      NOT NULL,
    qtd_minima     INTEGER,
    tipo           INTEGER,
    
    FOREIGN KEY fktipoProd(tipo)
        REFERENCES tipo_produto
        ON DELETE NO ACTION
);

-- tabela fornecimento
-- dependencias: fornecedor, tipo_produto
--CREATE TABLE fornecimento (
--    fornecedor_cnpj VARCHAR(20) NOT NULL,
--    tipo_produto     INTEGER     NOT NULL,
--
--    PRIMARY KEY (fornecedor_cnpj, tipo_produto),
--    FOREIGN KEY fkfornecedor(fornecedor_cnpj)
--        REFERENCES fornecedor
--        ON DELETE CASCADE,
--    FOREIGN KEY fktipoProd(tipo_produto)
--        REFERENCES tipo_produto
--        ON DELETE CASCADE
--);

-- fim do bloco de cadastros



--- bloco de transacoes

-- tabela compra
-- dependencias: cliente, funcionario
CREATE TABLE compra (
    id              INTEGER     NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    cliente_cpf     VARCHAR(20) NOT NULL,
    funcionario_cpf VARCHAR(20),
    data            DATE        NOT NULL,

    FOREIGN KEY fk_funcionario(funcionario_cpf)
        REFERENCES funcionario
        ON DELETE SET NULL,
    FOREIGN KEY fk_compra_cliente(cliente_cpf)
        REFERENCES cliente
        ON DELETE NO ACTION
);

-- tabela reserva
-- dependencias: cliente, funcionario
CREATE TABLE reserva (
    id                 INTEGER     NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    cliente_cpf        VARCHAR(20) NOT NULL,
    funcionario_cpf    VARCHAR(20),
    data               DATE        NOT NULL,
    
    FOREIGN KEY fk_funcionario(funcionario_cpf)
        REFERENCES funcionario
        ON DELETE SET NULL,
    FOREIGN KEY fkreservaCl(cliente_cpf)
        REFERENCES cliente
        ON DELETE CASCADE
);

-- tabela compra_fornecedor
-- dependencias: fornecedor, funcionario
CREATE TABLE compra_fornecedor (
    id              INTEGER     NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    fornecedor_cnpj VARCHAR(20) NOT NULL,
    func_cpf        VARCHAR(20) NOT NULL,
    data            DATE        NOT NULL,

    FOREIGN KEY fk_ped_for(fornecedor_cnpj)
        REFERENCES fornecedor
        ON DELETE NO ACTION,
    FOREIGN KEY fk_ped_for_func(func_cpf)
        REFERENCES funcionario
        ON DELETE NO ACTION
);

-- tabela pagamento_compra
-- dependencias: compra
-- Nota: mais de um pagamento_compra pode ser associado a uma unica compra, possibilitando um pagamento misto
-- @tipo --> 0: cartao de credito 1: cheque 2: a vista
CREATE TABLE pagamento_compra (
    compra_id INTEGER NOT NULL,
    tipo      INTEGER NOT NULL DEFAULT 0,
    valor     REAL    NOT NULL,
    
    cartao_conta_bancaria VARCHAR(10),
    cartao_numero         VARCHAR(30),
    cartao_nome_portador  VARCHAR(50),
    cartao_data_validade  DATE,

    cheque_num_serial     VARCHAR(50),
    cheque_vencimento     DATE,

    a_vista_desconto      REAL DEFAULT 0,

    FOREIGN KEY fk_compra(compra_id)
        REFERENCES compra
        ON DELETE CASCADE
);

-- tabela pagamento_funcionario
CREATE TABLE pagamento_funcionario (
    funcionario VARCHAR(20) NOT NULL,
    data        DATE        NOT NULL,
    valor       REAL        NOT NULL,

    FOREIGN KEY fkPagFunc(funcionario)
        REFERENCES funcionario
);

-- tabela pagamento_fornecedor
CREATE TABLE pagamento_fornecedor (
    fornecedor_cnpj  VARCHAR(20) NOT NULL,
    data             DATE        NOT NULL,
    valor            REAL        NOT NULL,
    
    FOREIGN KEY fk_pag_forne(fornecedor_cnpj)
        REFERENCES fornecedor
);

-- fim do bloco de transacoes




-- bloco de composicoes

-- tabela item_compra
-- dependencias: compra, produto
CREATE TABLE item_compra (
    compra_id  INTEGER NOT NULL,
    produto    INTEGER NOT NULL,
    quantidade INTEGER,
    valor_unit REAL,
    
    FOREIGN KEY fkEhCompostaClCom(compra_id)
        REFERENCES compra
        ON DELETE CASCADE,
    FOREIGN KEY fkEhCompostaPr(produto)
        REFERENCES produto
        ON DELETE CASCADE
);

-- tabela item_reserva
-- dependencias: reserva, produto
CREATE TABLE item_reserva (
    reserva_id INTEGER NOT NULL,
    produto    INTEGER NOT NULL,
    quantidade INTEGER,
    valor_unit REAL,
    
    FOREIGN KEY fkEhCompostaClCom(reserva_id)
        REFERENCES reserva
        ON DELETE CASCADE,
    FOREIGN KEY fkEhCompostaPr(produto)
        REFERENCES produto
        ON DELETE CASCADE
);

-- tabela item_fornecimento
-- dependencias: compra_fornecedor, produto
CREATE TABLE item_compra_fornecedor (
    compra_for_id INTEGER NOT NULL,
    produto       INTEGER NOT NULL,
    quantidade    INTEGER,
    valor_unit    REAL,
    
    PRIMARY KEY (compra_for_id, produto),
    FOREIGN KEY fkCompostaFor(compra_for_id)
        REFERENCES compra_fornecedor
        ON DELETE CASCADE,
    FOREIGN KEY fkCompostaPr(produto)
        REFERENCES produto
        ON DELETE CASCADE
);

-- fim do bloco de composicoes
-- **FIM DO BLOCO DE CRIACAO DAS TABELAS**




-- **BLOCO DE INSERCAO DE DADOS NO BANCO**

INSERT INTO endereco VALUES (DEFAULT, 'Alameda das Orquídeas',           'Cidade Jardim', 10,  '',  'São Carlos', 'SP');
INSERT INTO endereco VALUES (DEFAULT, 'Alameda das Azaleias',            'Cidade Jardim',  6, '21', 'São Carlos', 'SP');
INSERT INTO endereco VALUES (DEFAULT, 'Uma rua muito engraçada',        'Jardim Paulis', 45, '41', 'São Carlos', 'SP');
INSERT INTO endereco VALUES (DEFAULT, 'Rua das Camélias',                  'Cidade Jardim', 79, '23', 'São Carlos', 'SP');
INSERT INTO endereco VALUES (DEFAULT, 'Alameda das Orquídeas',           'Cidade Jardim', 98, '67', 'São Carlos', 'SP');
INSERT INTO endereco VALUES (DEFAULT, 'Rua dos Cravos',                  'Cidade Jardim', 45, '89', 'São Carlos', 'SP');
INSERT INTO endereco VALUES (DEFAULT, 'Alameda das Gardênhas',            'Cidade Jardim', 23,  '3', 'São Carlos', 'SP');
INSERT INTO endereco VALUES (DEFAULT, 'Rod. Washington Luis, km. 235',  'Monjolinho',        5, 'Mod 5 bloc 90', 'São Carlos', 'SP');
INSERT INTO endereco VALUES (DEFAULT, 'Luiz Vaz de Camões',                'Vila Marina',   18, '',   'São Carlos', 'SP');
INSERT INTO endereco VALUES (DEFAULT, 'Av. Salgado Filho',                'Vila Marina',      10, '',   'São Carlos', 'SP');

INSERT INTO estado_civil VALUES (DEFAULT, 'solteiro');
INSERT INTO estado_civil VALUES (DEFAULT, 'casado');
INSERT INTO estado_civil VALUES (DEFAULT, 'viúvo');
INSERT INTO estado_civil VALUES (DEFAULT, 'separado');
INSERT INTO estado_civil VALUES (DEFAULT, 'divorciado');
INSERT INTO estado_civil VALUES (DEFAULT, 'é complicado');

INSERT INTO pessoa VALUES (DEFAULT, 'João Paulo',            1);
INSERT INTO pessoa VALUES (DEFAULT, 'Ben Hur',               2);
INSERT INTO pessoa VALUES (DEFAULT, 'Maria Silvia',          1);
INSERT INTO pessoa VALUES (DEFAULT, 'Kaique Warsh',          3);
INSERT INTO pessoa VALUES (DEFAULT, 'Julio Fleury',          4);
INSERT INTO pessoa VALUES (DEFAULT, 'Ester 2-Optimal',       5);
INSERT INTO pessoa VALUES (DEFAULT, 'Paulo Kruskal',         6);
INSERT INTO pessoa VALUES (DEFAULT, 'Rosana Ascii',          7);
INSERT INTO pessoa VALUES (DEFAULT, 'Pedro Assembler',       8);
INSERT INTO pessoa VALUES (DEFAULT, 'Lucas',                 1);
INSERT INTO pessoa VALUES (DEFAULT, 'Cristiano',             2);
INSERT INTO pessoa VALUES (DEFAULT, 'João Paulo',            1);
INSERT INTO pessoa VALUES (DEFAULT, 'Thiago',                3);
INSERT INTO pessoa VALUES (DEFAULT, 'Kellen',                4);
INSERT INTO pessoa VALUES (DEFAULT, 'Hellen',                5);
INSERT INTO pessoa VALUES (DEFAULT, 'Suellen',               6);
INSERT INTO pessoa VALUES (DEFAULT, 'Kriskoff',              7);
INSERT INTO pessoa VALUES (DEFAULT, 'Yohan',                 8);
INSERT INTO pessoa VALUES (DEFAULT, 'Vladmir',               2);
INSERT INTO pessoa VALUES (DEFAULT, 'Poli',                  2);

INSERT INTO pessoa VALUES (DEFAULT, 'Stylist Place',         9);
INSERT INTO pessoa VALUES (DEFAULT, 'Confeccionado',         9);
INSERT INTO pessoa VALUES (DEFAULT, 'Curupira',              4);
INSERT INTO pessoa VALUES (DEFAULT, 'Bom Retiro confeccoes', 1);
INSERT INTO pessoa VALUES (DEFAULT, 'Saias Sob Medida',      2);
INSERT INTO pessoa VALUES (DEFAULT, 'Agasalhos Confeccoes',  1);
INSERT INTO pessoa VALUES (DEFAULT, 'KIJ Confeccoes',        5);
INSERT INTO pessoa VALUES (DEFAULT, 'Loja do Carlos',        5);
INSERT INTO pessoa VALUES (DEFAULT, 'GH Roupas',             6);
INSERT INTO pessoa VALUES (DEFAULT, 'Camisas SA',            6);

INSERT INTO telefone VALUES ( 1, '31412032');
INSERT INTO telefone VALUES ( 2, '9821-4123');
INSERT INTO telefone VALUES ( 3, '2123-1312');
INSERT INTO telefone VALUES ( 4, '55-1232-1221');
INSERT INTO telefone VALUES ( 5, '2111-2222');
INSERT INTO telefone VALUES ( 6, '1233-2222');
INSERT INTO telefone VALUES ( 7, '3141-2032');
INSERT INTO telefone VALUES ( 8, '3141-2032');
INSERT INTO telefone VALUES ( 9, '9876-2222');
INSERT INTO telefone VALUES (10, '3141-2032');

INSERT INTO tipo_produto VALUES (DEFAULT, 'Camiseta');
INSERT INTO tipo_produto VALUES (DEFAULT, 'Camisa');
INSERT INTO tipo_produto VALUES (DEFAULT, 'Calça jeans');
INSERT INTO tipo_produto VALUES (DEFAULT, 'Moletom');
INSERT INTO tipo_produto VALUES (DEFAULT, 'Regata');
INSERT INTO tipo_produto VALUES (DEFAULT, 'Camiseta sem manga');
INSERT INTO tipo_produto VALUES (DEFAULT, 'Pullover');
INSERT INTO tipo_produto VALUES (DEFAULT, 'Cachecol');
INSERT INTO tipo_produto VALUES (DEFAULT, 'Jaqueta');
INSERT INTO tipo_produto VALUES (DEFAULT, 'Short');

INSERT INTO tipo_funcionario VALUES (DEFAULT,   'Vendedor',                  'Vende roupas na loja');
INSERT INTO tipo_funcionario VALUES (DEFAULT,      'Gerente',        'Gerencia funcionários e vendas');
INSERT INTO tipo_funcionario VALUES (DEFAULT, 'Estoquista',                     'Organiza o estoque');
INSERT INTO tipo_funcionario VALUES (DEFAULT, 'Entregador', 'Entrega roupas nas casas dos clientes');

INSERT INTO cliente VALUES ( 1, '109.320.213-4', '20123', 1, 'Jenuino Filho', 'Amanda Ahr');
INSERT INTO cliente VALUES ( 2, '139.525.169-8', '13984', 2, 'Jenuino Filho', 'Amanda Ahr');
INSERT INTO cliente VALUES ( 3, '309.627.218-9', '58331', 1, 'Jenuino Filho', 'Amanda Ahr');
INSERT INTO cliente VALUES ( 4, '199.329.213-0', '12093', 3, 'Jenuino Filho', 'Amanda Ahr');
INSERT INTO cliente VALUES ( 5, '169.320.310-5', '58766', 3, 'Jenuino Filho', 'Amanda Ahr');
INSERT INTO cliente VALUES ( 6, '129.825.215-4', '10211', 4, 'Jenuino Filho', 'Amanda Ahr');
INSERT INTO cliente VALUES ( 7, '349.921.513-7', '12031', 2, 'Jenuino Filho', 'Amanda Ahr');
INSERT INTO cliente VALUES ( 8, '579.123.219-4', '85631', 1, 'Jenuino Filho', 'Amanda Ahr');
INSERT INTO cliente VALUES ( 9, '807.225.217-9', '10294', 3, 'Jenuino Filho', 'Amanda Ahr');
INSERT INTO cliente VALUES (10, '009.320.212-4', '12857', 2, 'Jenuino Filho', 'Amanda Ahr');

INSERT INTO funcionario VALUES (11, '497.023.123-5', '03/05/1932', '05/06/2013',  1000, 10, 2);
INSERT INTO funcionario VALUES (12, '570.313.981-2', '01/06/1942', '09/05/2012',   500, 50, 2);
INSERT INTO funcionario VALUES (13, '142.236.954-6', '02/07/1923', '07/09/2011',  5000,  0, 3);
INSERT INTO funcionario VALUES (14, '117.703.092-8', '08/09/1910', '01/10/2011',  3000,  0, 1);
INSERT INTO funcionario VALUES (15, '179.423.560-9', '03/10/1932', '01/11/2011',  2000,  0, 2);
INSERT INTO funcionario VALUES (16, '157.093.143-0', '05/10/1992', '10/12/2012',  1450,  0, 2);
INSERT INTO funcionario VALUES (17, '204.123.612-3', '01/12/1982', '12/06/2013',   400,  0, 2);
INSERT INTO funcionario VALUES (18, '121.027.574-2', '02/12/1983', '22/07/2011',   200,  0, 1);
INSERT INTO funcionario VALUES (19, '500.422.267-5', '03/12/1985', '30/06/2011', 10000,  0, 1);
INSERT INTO funcionario VALUES (20, '900.521.413-1', '20/04/1987', '10/05/2012',   200,  0, 2);

INSERT INTO fornecedor VALUES (21, '5511371');
INSERT INTO fornecedor VALUES (22, '1551924');
INSERT INTO fornecedor VALUES (23, '5511998');
INSERT INTO fornecedor VALUES (24, '2535320');
INSERT INTO fornecedor VALUES (25, '7867820');
INSERT INTO fornecedor VALUES (26, '1898785');
INSERT INTO fornecedor VALUES (27, '0929030');
INSERT INTO fornecedor VALUES (28, '1987380');
INSERT INTO fornecedor VALUES (29, '5547832');
INSERT INTO fornecedor VALUES (30, '5598330');

INSERT INTO produto VALUES (DEFAULT, 'Adidas', 'M',          20.90, 10, 5,  1);
INSERT INTO produto VALUES (DEFAULT, 'Cavalera', 'P',        20.90, 10, 1,  2);
INSERT INTO produto VALUES (DEFAULT, 'Pollus', 'P',          10.90, 50, 50, 2);
INSERT INTO produto VALUES (DEFAULT, 'Converse', 'P',        20.90, 50, 40, 2);
INSERT INTO produto VALUES (DEFAULT, 'TNG #12', 'M',         10.90, 50, 40, 2);
INSERT INTO produto VALUES (DEFAULT, 'Quote', 'G',           20.90, 50, 30, 3);
INSERT INTO produto VALUES (DEFAULT, 'Yum', 'G',             10.90, 50, 25, 1);
INSERT INTO produto VALUES (DEFAULT, 'Sdyl', 'G',            30.90, 50, 20, 4);
INSERT INTO produto VALUES (DEFAULT, 'Healling Effect', 'G', 30.90, 20, 20, 4);
INSERT INTO produto VALUES (DEFAULT, 'Vivaldi t-shirt', 'M', 30.90, 41, 20, 1);

INSERT INTO compra VALUES (DEFAULT, '139.525.169-8', '117.703.092-8', '03/02/2012');
INSERT INTO item_compra VALUES (1, 1, 1, 10.90);
INSERT INTO item_compra VALUES (1, 2, 1, 10.00);
INSERT INTO pagamento_compra VALUES (1, 0, 10.90, '1.888-0', '1020.3121.2312.3123', 'Maria Silvia', '20/10/2013', NULL, NULL, NULL);
INSERT INTO pagamento_compra VALUES (1, 2, 10.00, NULL, NULL, NULL, NULL, NULL, NULL, DEFAULT);

INSERT INTO compra VALUES (DEFAULT, '169.320.310-5', '117.703.092-8', '05/01/2012');
INSERT INTO item_compra VALUES (2, 5, 2, 20.00);
INSERT INTO item_compra VALUES (2, 1, 1, 10.90);
INSERT INTO pagamento_compra VALUES (2, 1, 30.90, NULL, NULL, NULL, NULL, '210390', '05/01/2014', NULL);

INSERT INTO reserva VALUES (DEFAULT, '309.627.218-9', '204.123.612-3', '30/05/2013');
INSERT INTO item_reserva VALUES (1, 1, 1, 10.90);
INSERT INTO item_reserva VALUES (1, 2, 1, 10.90);

INSERT INTO reserva VALUES (DEFAULT, '109.320.213-4', '900.521.413-1', '30/05/2013');
INSERT INTO item_reserva VALUES (2, 1, 1, 10.90);
INSERT INTO item_reserva VALUES (2, 2, 1, 10.90);

INSERT INTO compra_fornecedor VALUES (DEFAULT, '5511371', '497.023.123-5', '30/05/2013');
INSERT INTO item_compra_fornecedor VALUES (1, 1, 1, 10.90);
INSERT INTO item_compra_fornecedor VALUES (1, 2, 1, 10.90);
INSERT INTO pagamento_fornecedor VALUES ('5511371', '30/05/2013', 21.8);

INSERT INTO pagamento_funcionario VALUES ('497.023.123-5', '12/4/2013', 100000);
INSERT INTO pagamento_funcionario VALUES ('570.313.981-2', '12/4/2013', 1700);
INSERT INTO pagamento_funcionario VALUES ('142.236.954-6', '11/4/2013', 1000);
INSERT INTO pagamento_funcionario VALUES ('117.703.092-8', '12/4/2013', 900);
INSERT INTO pagamento_funcionario VALUES ('179.423.560-9', '11/4/2013', 1200);
INSERT INTO pagamento_funcionario VALUES ('157.093.143-0', '12/4/2013', 1200);
INSERT INTO pagamento_funcionario VALUES ('204.123.612-3', '13/4/2013', 900);
INSERT INTO pagamento_funcionario VALUES ('121.027.574-2', '11/4/2013', 1500);
INSERT INTO pagamento_funcionario VALUES ('500.422.267-5', '11/4/2013', 90000);
INSERT INTO pagamento_funcionario VALUES ('900.521.413-1', '12/4/2013', 1200);
INSERT INTO pagamento_funcionario VALUES ('497.023.123-5', '13/5/2013', 100000);
INSERT INTO pagamento_funcionario VALUES ('570.313.981-2', '12/5/2013', 1700);
INSERT INTO pagamento_funcionario VALUES ('142.236.954-6', '11/5/2013', 1000);
INSERT INTO pagamento_funcionario VALUES ('117.703.092-8', '11/5/2013', 900);
INSERT INTO pagamento_funcionario VALUES ('179.423.560-9', '11/5/2013', 1200);
INSERT INTO pagamento_funcionario VALUES ('157.093.143-0', '12/5/2013', 1200);
INSERT INTO pagamento_funcionario VALUES ('204.123.612-3', '11/5/2013', 900);
INSERT INTO pagamento_funcionario VALUES ('121.027.574-2', '12/5/2013', 1500);
INSERT INTO pagamento_funcionario VALUES ('500.422.267-5', '13/5/2013', 90000);
INSERT INTO pagamento_funcionario VALUES ('900.521.413-1', '11/5/2013', 1200);

--TODO: esta tabela pode ser inferida a partir de
--item_comp_for. Decidir se ela deve ser mantida.
--INSERT INTO fornecimento VALUES (2227102, 1);
--INSERT INTO fornecimento VALUES (4223001, 1);
--INSERT INTO fornecimento VALUES (0460806, 2);
--INSERT INTO fornecimento VALUES (2227102, 2);
--INSERT INTO fornecimento VALUES (4745439, 4);
--INSERT INTO fornecimento VALUES (2227102, 3);
--INSERT INTO fornecimento VALUES (4745439, 3);
--INSERT INTO fornecimento VALUES (2227102, 5);
--INSERT INTO fornecimento VALUES (8452748, 5);
--INSERT INTO fornecimento VALUES (404713, 6);


-- **FIM DO BLOCO DE INSERCAO DE DADOS NO BANCO**
--
-- FIM DE ARQUIVO