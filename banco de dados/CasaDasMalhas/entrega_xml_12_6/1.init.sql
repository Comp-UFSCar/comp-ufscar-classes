-- ATIVIDADE 12.06.13

-- Desc: implementação dos XML SCHEMAS no SGDB.
-- Autor: Lucas Oliveira David 407917
-- SGDB designado: DB2



-- INICIO DO BLOCO DE INICIALIZACAO DA BASE DE DADOS

-- Retire o comentario da linha 12 apos a primeira execucao
DROP DATABASE casaxml;
CREATE DATABASE casaxml USING CODESET UTF-8 TERRITORY US;
CONNECT TO casaxml;

-- Retire o comentario das linhas abaixo apos a primeira execucao
-- DROP TABLE tipo_funcionario;
-- DROP TABLE pagamento_funcionario;
-- DROP TABLE funcionario;

-- FIM DO BLOCO DE INICIALIZACAO DA BASE DE DADOS



-- INICIO DO BLOCO DE CRIACAO DA ESTRUTURA SQL

CREATE TABLE tipo_funcionario (
    id       INTEGER NOT NULL PRIMARY KEY,
    conteudo XML
);

CREATE TABLE funcionario (
    id       INTEGER NOT NULL PRIMARY KEY,
    tipo     INTEGER,
    conteudo XML,

    FOREIGN KEY fk_func_tipo(tipo)
        REFERENCES tipo_funcionario
);

CREATE TABLE pagamento_funcionario (
    funcionario  INTEGER NOT NULL,
    conteudo     XML,
    
    FOREIGN KEY fkPagFunc(funcionario)
        REFERENCES funcionario
);

-- FIM DO BLOCO DE CRIACAO DA ESTRUTURA SQ
--
-- FIM DO ARQUIVO