package br.com.luizcurado.provaconceito.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.luizcurado.provaconceito.controller.dto.ProdutoDto;
import br.com.luizcurado.provaconceito.modelo.Produto;
import br.com.luizcurado.provaconceito.repository.ProdutoRepository;

//--------------------------------------------------------
/** Controler para pesquisa e cadastro de produtos */
//--------------------------------------------------------
@RestController
@RequestMapping("/produtos")
public class ProdutosController {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	//---------------------------------------------------------------------------
	/** Recupera todos produtos, podendo filtrar pelo nome
	 *  - Exemplo: http://localhost:8080/produtos
	 *  - Exemplo: http://localhost:8080/produtos?page=1
	 *  - Exemplo: http://localhost:8080/produtos?nomeProduto=Xbox 
	 */
	//---------------------------------------------------------------------------		
	@GetMapping
	@Cacheable(value = "listaDeProdutos") // Faz cache da consulta
	public Page<Produto> pesquisarTodos(@RequestParam(required = false) String nomeProduto, 
			@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable paginacao) {
		
		Page<Produto> produtos = (nomeProduto == null) 
				? produtoRepository.findAll(paginacao)
				: produtoRepository.findByNomeContaining(nomeProduto, paginacao);
		return produtos;		
	}

	//---------------------------------------------------------------------------
	/** Recupera dados de um produto
	 *  Exemplo: http://localhost:8080/produtos/2 
	 */
	//---------------------------------------------------------------------------	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> detalhar(@PathVariable Long id) {
		Optional<Produto> optional = produtoRepository.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}		
		return ResponseEntity.notFound().build();
	}
	
	//---------------------------------------------------------------------------
	/** Cadastra um novo produto 	 
	 *  - Exemplo: http://localhost:8080/produtos
	 *  - Body:
	 *  {
	 *		"nome" : "Nintendo 3DS"
	 *	} 
	 *  - Header: 
	 *  Authorization: Bearer eyJhbGciOiJIUzI1N... (token)
	 */	
	//---------------------------------------------------------------------------		
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaDeProdutos", allEntries = true) // Invalida o cache da consulta de produtos
	public ResponseEntity<Produto> cadastrar(@RequestBody @Valid ProdutoDto produtoDTO, UriComponentsBuilder uriBuilder) {
		Produto produto = new Produto();
		produto.setNome(produtoDTO.getNome());
		produtoRepository.save(produto);

		// Adiciona no header da requisicao o endpoint para consulta do produto criado
		URI uri = uriBuilder.path("/produtos/{id}").buildAndExpand(produto.getId()).toUri();
		return ResponseEntity.created(uri).body(produto);
	}
	
	//---------------------------------------------------------------------------
	/** Atualiza um produto 	
	 *  - Exemplo: http://localhost:8080/produtos/4
	 *  - Body:
	 *  {
	 *		"nome" : "Sega Genesis"
	 *	} 
	 *  - Header: 
	 *  Authorization: Bearer eyJhbGciOiJIUzI1N... (token)
	 */	
	//---------------------------------------------------------------------------	
	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeProdutos", allEntries = true) // Invalida o cache da consulta de produtos
	public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoDto produtoDTO) {
		Optional<Produto> optional = produtoRepository.findById(id);
		if (optional.isPresent()) {
			Produto produto = optional.get() ;			
			produto.setNome(produtoDTO.getNome());
			return ResponseEntity.ok(produto);
		}		
		return ResponseEntity.notFound().build();
	}
	
	//---------------------------------------------------------------------------
	/** Exclui um produto 
	 *	- Exemplo: http://localhost:8080/produtos/2
	 *  - Header: 
	 *  Authorization: Bearer eyJhbGciOiJIUzI1N... (token)
	 */	
	//---------------------------------------------------------------------------
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeProdutos", allEntries = true) // Invalida o cache da consulta de produtos
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Produto> optional = produtoRepository.findById(id);
		if (optional.isPresent()) {
			produtoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}

}