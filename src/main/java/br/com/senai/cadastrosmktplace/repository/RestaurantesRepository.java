package main.java.br.com.senai.cadastrosmktplace.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import main.java.br.com.senai.cadastrosmktplace.entity.Categoria;
import main.java.br.com.senai.cadastrosmktplace.entity.Restaurante;

public interface RestaurantesRepository extends JpaRepository<Restaurante, Integer> {

	@Query(value = "SELECT r " + "FROM Restaurante r  " + "JOIN FETCH r.categoria " + "WHERE r.id = :id")
	public Restaurante buscarPor(Integer id);

	@Query(value = "SELECT r " + "FROM Restaurante r " + "WHERE Upper(r.nome) = Upper(:nome)")
	public Restaurante buscarPor(String nome);

	@Query(value = "SELECT r " + "FROM Restaurante r " + "JOIN FETCH r.categoria "
			+ "WHERE Upper(r.nome) LIKE Upper(:nome) " + "AND r.categoria = :categoria "
			+ "ORDER BY r.nome ", countQuery = "SELECT Count(r) FROM Restaurante r WHERE Upper(r.nome) LIKE Upper(:nome) AND r.categoria = :categoria ")
	public Page<Restaurante> listarPor(String nome, Categoria categoria, Pageable paginacao);

	@Query("SELECT r FROM Restaurante r JOIN FETCH r.categoria c ORDER BY LOWER(r.nome)")
	public List<Restaurante> listarTodos();

	@Query("SELECT COUNT(r) FROM Restaurante r WHERE r.categoria.id = :categoria")
	public Integer contarRestaurantesPor(Integer categoria);

}
