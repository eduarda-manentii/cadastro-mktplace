package main.java.br.com.senai.cadastrosmktplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import main.java.br.com.senai.cadastrosmktplace.entity.Categoria;

@Repository
public interface CategoriasRepository extends JpaRepository<Categoria, Integer> {
	
	@Transactional
	@Modifying
	@Query("UPDATE Categoria c SET c.nome = :nome WHERE c.id = :id")
	public void atualizarPor(@Param("id") Integer id, @Param("nome") String nome);

    @Query("SELECT c FROM Categoria c WHERE c.id = :id")
    public Categoria buscarPor(@Param("id") Integer id); 

    @Query("SELECT c FROM Categoria c WHERE Upper(c.nome) LIKE Upper(:nome) ORDER BY c.nome")
    public List<Categoria> buscarPor(@Param("nome") String nome);
    
    @Query("SELECT c FROM Categoria c")
    public List<Categoria> listarTodas();

}