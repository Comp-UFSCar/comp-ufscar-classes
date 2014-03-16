CONNECT TO casaxml;

-- INICIO DO BLOCO DE REGISTRO DE SCHEMA

-- SCHEMA: funcionario
REGISTER XMLSCHEMA 'funcionario.xsd'
FROM 'E:/xml/xml_schemas/funcionario.xsd'
AS CASAXML.FUNCIONARIO COMPLETE;

-- REGISTRO DE SCHEMA
-- SCHEMA: tipo_funcionario
REGISTER XMLSCHEMA 'tipo_funcionario.xsd'
FROM 'E:/xml/xml_schemas/tipo_funcionario.xsd'
AS CASAXML.TIPO_FUNCIONARIO COMPLETE;

-- REGISTRO DE SCHEMA
-- SCHEMA: pagamento_funcionario
REGISTER XMLSCHEMA 'pagamento_funcionario.xsd'
FROM 'E:/xml/xml_schemas/pagamento_funcionario.xsd'
AS CASAXML.PAGAMENTO_FUNCIONARIO COMPLETE;

-- FIM DO BLOCO DE REGISTRO DE SCHEMA