package br.com.luizcurado.provaconceito.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.luizcurado.provaconceito.modelo.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	Page<Produto> findByNomeContaining(String nomeCurso, Pageable paginacao);

}
