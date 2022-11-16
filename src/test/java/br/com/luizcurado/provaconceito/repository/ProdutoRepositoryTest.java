package br.com.luizcurado.provaconceito.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.luizcurado.provaconceito.modelo.Produto;

//------------------------------------------------------
/** Teste unitário do repositório */
//------------------------------------------------------
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = { 
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.url=jdbc:h2:mem:banco-teste",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.datasource.initialization-mode=never",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"spring.jpa.hibernate.ddl-auto=update",
		"spring.datasource.driverClassName=org.h2.Driver" })
//----------------------------------------------------------------------------------------------------------------------
// Outra alternativa: 
// @ActiveProfiles("teste")
// Nesse caso o @TestPropertySource deve ser comentado e suas propriedades colocadas no application-teste.properties
//----------------------------------------------------------------------------------------------------------------------
public class ProdutoRepositoryTest {

	@Autowired
	private ProdutoRepository repository ;

	//------------------------------------------------------------------
	/** Verifica se o repositório foi injetado pelo spring */
	//------------------------------------------------------------------
	@Test
	public void deveInjetarRepository() {
		assertNotNull(repository);
	}	

	//-------------------------------------------------------------------------------
	/** Verifica se o arquivo sql foi carregado, e existem 3 produtos cadastrados */
	//-------------------------------------------------------------------------------	
	@Test
	@Sql("classpath:sql/Produtos.sql")
	public void deveRecuperarTresProdutos() {
		List<Produto> listaRetorno = repository.findAll() ;
		assertTrue(listaRetorno.size() == 3);
	}
	
	//-------------------------------------------------------------------------------
	/** Verifica se um produto que consta no arquivo sql está de fato cadastrado. */
	//-------------------------------------------------------------------------------	
	@Test
	@Sql("classpath:sql/Produtos.sql")
	public void deveRecuperarProdutoPeloNome() {
		Page<Produto> page = repository.findByNomeContaining("Atari", null) ;
		assertTrue(page.getNumberOfElements() == 1);
	}

	//-------------------------------------------------------------------------------
	/** Verifica se um produto que NÃO consta no arquivo sql está cadastrado. */
	//-------------------------------------------------------------------------------	
	@Test
	@Sql("classpath:sql/Produtos.sql")
	public void naoDeveRecuperarProdutoPeloNome() {
		Page<Produto> page = repository.findByNomeContaining("3DO", null) ;
		assertTrue(page.getNumberOfElements() == 0);
	}

}
