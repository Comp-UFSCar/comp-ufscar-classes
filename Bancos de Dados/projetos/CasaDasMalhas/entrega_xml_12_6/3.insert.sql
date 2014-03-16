CONNECT TO casaxml;

-- INICIO DO BLOCO DE INSERCOES DE DADOS

--1 Insercao sobre tabela tipo_funcionario
IMPORT FROM 'E:/xml/xml_dels/tipo_funcionario.del' OF DEL XML
FROM 'E:/xml/xml_insertions'
INSERT INTO tipo_funcionario;

--2 Insercao sobre tabela funcionario
IMPORT FROM 'E:/xml/xml_dels/funcionario.del' OF DEL XML
FROM 'E:/xml/xml_insertions'
INSERT INTO funcionario;

--3 Insercao sobre tabela pagamento_funcionario
IMPORT FROM 'E:/xml/xml_dels/pagamento_funcionario.del' OF DEL XML
FROM 'E:/xml/xml_insertions'
INSERT INTO pagamento_funcionario;

-- FIM DO BLOCO DE INSERCOES DE DADOS