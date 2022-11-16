package br.com.luizcurado.provaconceito.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import br.com.luizcurado.provaconceito.modelo.Produto;
import br.com.luizcurado.provaconceito.repository.ProdutoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProdutoRepository produtoRepository;
	
	//--------------------------------------------------------------
	/** Retorna produto com id 10 e nome "Produto de Teste" */
	//--------------------------------------------------------------
	private Optional<Produto> getProduto() {
		Produto p = new Produto();
		p.setId(10L);
		p.setNome("Produto de Teste");
		return Optional.of(p) ;
	}

	//--------------------------------------------------------------
	/** Recupera produto com id informado */
	//--------------------------------------------------------------	
	@Test
	public void deveRecuperarJsonProdutoComIdNome() throws Exception {
		// Ao chamar o findById o repositorio retorna o produto com id 10 e nome "Produto de Teste"
		Mockito
			.when(produtoRepository.findById(any(Long.class))) 
			.thenReturn(getProduto()) ;			
		
		// Recupera json de produto com campos id e nome
		mockMvc
			.perform(get("/produtos/{id}", 10)
			.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()) // 200
		.andExpect(jsonPath("$.id").value(10))
		.andExpect(jsonPath("$.nome").value("Produto de Teste"));
	}

	//---------------------------------------------------------------------
	/** Retorna Page com dois produtos de id 10 e 20 */
	//---------------------------------------------------------------------	
	private Page<Produto> getPageProdutos() {
		Produto p1 = new Produto(10L, "Produto 1");
		Produto p2 = new Produto(20L, "Produto 2");
		return new PageImpl<Produto>(List.of(p1, p2));
	}	
	
	//--------------------------------------------------------------
	/** Recupera produto com id informado */
	//--------------------------------------------------------------	
	@Test
	public void deveRecuperarJsonTodosProdutos() throws Exception {
		// Ao chamar o findById o repositorio retorna o produto com id 10 e nome "Produto de Teste"
		Mockito
			.when(produtoRepository.findAll(any(Pageable.class))) 
			.thenReturn(getPageProdutos()) ;			
		
		// Recupera json com lista de produtos com campos id e nome
		mockMvc
			.perform(get("/produtos")
			.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()) // 200
		.andExpect(jsonPath("$.content").isArray())
		.andExpect(jsonPath("$.content", hasSize(2)))
		.andExpect(jsonPath("$.content[0].id").value(10))
		.andExpect(jsonPath("$.content[0].nome").value("Produto 1"))
		.andExpect(jsonPath("$.content[1].id").value(20))
		.andExpect(jsonPath("$.content[1].nome").value("Produto 2"));
	}

	 
	//--------------------------------------------------------------------------
	/** Tenta excluir produto. Usa um MockUser porque esse rest é protegido */
	//--------------------------------------------------------------------------	
	@Test
	@WithMockUser(username = "teste@email.com", password = "123")
	public void deveExcluirProduto() throws Exception {
		// Ao chamar o findById o repositorio retorna o produto com id 10 e nome "Produto de Teste"		
		Mockito
			.when(produtoRepository.findById(any(Long.class))) 
			.thenReturn(getProduto()) ;
		
		// Ao excluir o produto, deve ignorar a chamada
		Mockito
			.doNothing()
			.when(produtoRepository).deleteById(any(Long.class)) ;
		
		long idProduto = 10 ;
		
		// Chama o endpoint de exclusao
		mockMvc
			.perform(delete("/produtos/{id}", idProduto)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk()) ; // 200
		
		// Verifica se o método deleteById foi chamado pelo controller, com o id passado para ele
		Mockito
			.verify(produtoRepository, times(1)).deleteById(idProduto);
	}
	

}
