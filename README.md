# Teste Digio Backend
Crie um microserviço com os seguintes endpoints após consumir os dados dos
mocks acima e retorne o que está sendo solicitado:

1. GET: /compras - Retornar uma lista das compras ordenadas de forma
crescente por valor, deve conter o nome dos clientes, cpf dos clientes,
dado dos produtos, quantidade das compras e valores totais de cada
compra.

2. GET: /maior-compra/ano - (Exemplo: /maior_compra/2016) - Retornar a
maior compra do ano informando os dados da compra disponibilizados,
deve ter o nome do cliente, cpf do cliente, dado do produto, quantidade
da compra e seu valor total.

3. GET: /clientes-fieis - Retornar o Top 3 clientes mais fieis, clientes que
possuem mais compras recorrentes com maiores valores.

4. GET: /recomendacao/cliente/tipo - Retornar uma recomendação de vinho
baseado nos tipos de vinho que o cliente mais compra.

