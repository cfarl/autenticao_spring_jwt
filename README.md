# autenticao_spring_jwt

Prova de Conceito para autenticação com Spring Security e JWT em endpoint REST

Como usar essa aplicação:

1) Inicialize a aplicação.

2) Acesse o endpoint "http://localhost:8080/auth" com o método http POST para fazer a autenticacao do usuário, usando login e senha.
   Informe no body da requisição o seguinte JSON:
   
   {
 	  "email" : "lrcurado@email.com",
 	  "senha" : "123456"
  }
  
  Será retornado um token JWT, como na resposta abaixo:
   {
  	 "token": "eyJhbGciOiJIUzI1NiJ9....",
   	 "tipo": "Bearer"
   } 
   
3) Faça a consulta dos produtos cadastrados usando o método http GET (essa chamada não requer autenticação):   

    http://localhost:8080/produtos
	  http://localhost:8080/produtos?page=1
	  http://localhost:8080/produtos?nomeProduto=Xbox 
    
4) Faça a consulta de um produto cadastrado usando o método http GET (essa chamada não requer autenticação):   

   http://localhost:8080/produtos/2 
   
5) Cadastre um novo produto usando o método http POST (essa chamada REQUER autenticação informando o token):  

- Endereço chamado: http://localhost:8080/produtos

- JSON enviado no body:
	 {
	  	"nome" : "Nintendo 3DS"
	 } 
   
- Campo enviado no header: 
	Authorization: Bearer eyJhbGciOiJIUzI1N... (token)

6) Altere um produto usando o método http PUT (essa chamada REQUER autenticação informando o token):  

- Endereço chamado: http://localhost:8080/produtos/4

- JSON enviado no body:
	 {
	  	"nome" : "Sega Genesis"
	 } 
   
- Campo enviado no header: 
	Authorization: Bearer eyJhbGciOiJIUzI1N... (token)
  
7) Exclua um produto usando o método http DELETE (essa chamada REQUER autenticação informando o token):  

- Endereço chamado: http://localhost:8080/produtos/2

- Campo enviado no header: 
	Authorization: Bearer eyJhbGciOiJIUzI1N... (token)  
