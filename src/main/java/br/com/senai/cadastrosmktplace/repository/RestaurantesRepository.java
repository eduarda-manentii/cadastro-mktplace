package main.java.br.com.senai.cadastrosmktplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import main.java.br.com.senai.cadastrosmktplace.entity.Categoria;
import main.java.br.com.senai.cadastrosmktplace.entity.Restaurante;

@Repository
public interface RestaurantesRepository extends JpaRepository<Restaurante, Integer> {

	@Query(value = "SELECT r " + "FROM Restaurante r  " + "JOIN FETCH r.categoria " + "WHERE r.id = :id")
	public Restaurante buscarPor(@Param("id") Integer id);

	@Query(value = "SELECT r " + "FROM Restaurante r " + "WHERE Upper(r.nome) = Upper(:nome)")
	public List<Restaurante> buscarPor(@Param("nome") String nome);

	@Query(value = "SELECT r " + "FROM Restaurante r " + "JOIN FETCH r.categoria "
			+ "WHERE Upper(r.nome) LIKE Upper(:nome) " + "AND r.categoria = :categoria " + "ORDER BY r.nome")
	public List<Restaurante> listarPor(@Param("nome") String nome, @Param("categoria") Categoria categoria);

	@Query(value = "SELECT r FROM Restaurante r JOIN FETCH r.categoria c ORDER BY LOWER(r.nome)")
	public List<Restaurante> listarTodos();

	@Query(value = "SELECT COUNT(r) FROM Restaurante r WHERE r.categoria.id = :categoria")
	public Integer contarRestaurantesPor(@Param("categoria") Integer categoria);

}
