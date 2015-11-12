/* Liste as informações dos empregados que trabalham para o departamento 4 e que
recebem salário maior do que R$25.000,00 ou que trabalham para o departamento
5 e que recebem salário maior do que R$30.000,00.*/
SELECT *
FROM empregado
WHERE (nro_departamento = 4 AND salário_emp > 25000) 
	OR (nro_departamento = 5 AND salário_emp > 30000);
	
/* Liste o primeiro nome, o último nome e o salário dos empregados que trabalham
para o departamento 4 e que recebem salário maior do que R$25.000,00. */
SELECT primeiro_nome_emp, último_nome_emp, salário_emp
FROM empregado
WHERE nro_departamento = 4 AND salário_emp > 25000;

/* Liste o código dos empregados que trabalham para o departamento 5 ou que
supervisionam um empregado que trabalha para o departamento 5 */
(SELECT cod_empregado
FROM empregado
WHERE nro_departamento = 5)
UNION
(SELECT cod_supervisor
FROM empregado
WHERE nro_departamento = 5)

/* Recupere, para cada empregado do sexo feminino, o seu nome completo e os
nomes dos seus dependentes. Use a operação de produto cartesiano */
SELECT primeiro_nome_emp, último_nome_emp, nome_dependente
FROM empregado, dependente
WHERE sexo_emp = "feminino" AND empregad.cod_empregado = dependente.cod_empregado

/* Recupere, para cada departamento, o seu nome e o nome completo de seu gerente */
SELECT nome_depto, primeiro_nome_emp, último_nome_emp
FROM departamento, empregado
WHERE departamento.cod_gerente = empregado.cod_empregado

/* Recupere os nomes completos dos empregados que não têm dependentes. */
(SELECT primeiro_nome_emp, último_nome_emp
FROM empregado)
MINUS
(SELECT primeiro_nome_emp, último_nome_emp
FROM empregado, dependente
WHERE empregado.cod_empregado = dependente.cod_empregado)

/* Recupere, para cada empregado do sexo feminino, o seu nome completo e os
nomes dos seus dependentes. Use a operação de junção natural */
SELECT primeiro_nome_emp, último_nome_emp, nome_dependente
FROM empregado, dependente
WHERE empregado.cod_empregrado = dependente.cod_empregado
	AND empregado.sexo_emp = "feminino"
	
/* Para cada projeto localizado no Bloco 19, liste: (i) o número do projeto; (ii) o nome
do departamento que controla o projeto; e (iii) o nome completo, o endereço e a
data de aniversário do gerente do projeto */
SELECT 	nro_projeto, nome_depto, primeiro_nome_emp, ultimo_nome_emp, end_emp, data_niver_emp
FROM 	projeto, departamento, empregado
WHERE	projeto.nro_departamento = departamento.nro_departamento AND
		departamento.cod_gerente = empregado.cod_empregado AND
		projeto.local_projeto = "Bloco 19"

/* Recupere o nome completo e o endereço de todos os empregados que trabalham
para o Departamento de Informática */
SELECT 	primeiro_nome_emp, último_nome_emp, end_emp
FROM 	empregado, departamento
WHERE 	nome_depto = "informatica" AND
	departamento.nro_departamento = empregado.nro_departamento

/* Encontre os nomes dos empregados que trabalham em todos os projetos controlados
pelo departamento número 5 */
SELECT primeiro_nome_emp, último_nome_emp
FROM empregado
WHERE cod_empregado IN (
	SELECT cod_empregado
	FROM projeto, trabalha_para
	WHERE projeto.nro_departamento = 5 AND
		trabalha_para.nro_projeto = projeto.nro_projeto)
		
/* Liste os números dos projetos nos quais que existe um empregado cujo último
nome é Silva que trabalha no projeto tanto como um funcionário quanto como um gerente
do departamento que controla o projeto */
(SELECT DISTINCT nro_projeto
FROM trabalha_para T, empregado E
WHERE T.cod_empregado = E.cod_empregado AND
último_nome_emp = “Silva” )
UNION
(SELECT DISTINCT nro_projeto
FROM projeto P, departamento D, empregado E
WHERE P.nro_departamento = D.nro_departamento AND
E.cod_empregado = D.cod_gerente AND
E.último_nome_emp = “Silva”)

/* Liste os nomes completos dos gerentes que tem pelo menos um dependente */
SELECT DISTINCT primeiro_nome_emp, último_nome_emp
FROM empregado, departamento
WHERE empregado.cod_empregado = departamento.cod_gerente AND
	cod_empregado IN
		(SELECT DISTINCT cod_empregado
			FROM dependente)

/* Liste a soma dos salários de todos os empregados que trabalham para o
Departamento de Informática, assim como o maior salário, o menor salário e a
média dos salários desses empregados */
SELECT 	SUM(salário_emp), MAX(salário_emp), MIN(salário_emp),
		AVG(salário_emp)
FROM 	empregado, departamento
WHERE 	empregado.nro_departamento = departamento.nro_departamento AND
		nome_depto = “Departamento de Informática”

/* Recupere o número total de empregados que trabalham para o Departamento de
Informática. */
SELECT COUNT (*)
FROM empregado, departamento
WHERE empregado.nro_departamento = departamento.nro_departamento AND
nome_depto = “Departamento de Informática”

/* Para cada projeto, liste o número do projeto, o nome do projeto e o número de
empregados que trabalham para o projeto */
SELECT nro_projeto, nome_projeto, COUNT(*)
FROM projeto, trabalha_para
WHERE projeto.nro_projeto = trabalha_para.nro_projeto
GROUP BY nro_projeto, nome_projeto