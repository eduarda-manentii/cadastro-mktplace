package main.java.br.com.senai.cadastrosmktplace.service;

import java.util.List;

import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.br.com.senai.cadastrosmktplace.entity.Categoria;
import main.java.br.com.senai.cadastrosmktplace.repository.CategoriasRepository;
import main.java.br.com.senai.cadastrosmktplace.repository.RestaurantesRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriasRepository categoriaRepository;
	
	@Autowired
	private RestaurantesRepository restauranteRepository;
	
	public CategoriaService() {}
	
	public void salvar(Categoria categoria) {
		if (categoria.isPersistido()) {
			this.categoriaRepository.atualizarPor(categoria.getId(), categoria.getNome());
		} else {
			this.categoriaRepository.save(categoria);
		}
	}
	
	public void excluirPor(int idCategoria) {
		Preconditions.checkArgument(idCategoria > 0, "O id para remoção"
				+ " da categoria deve ser maior que 0.");
		
		boolean isRemocaoInvalida = restauranteRepository.contarRestaurantesPor(idCategoria) > 0;
		Preconditions.checkArgument(!isRemocaoInvalida, "Nao é possível remover uma"
				+ " categoria vinculada a um restaurante.");
		
		this.categoriaRepository.deleteById(idCategoria);
	}
	
	public List<Categoria> listarPor(String nome) {
		Boolean isFiltroValido = nome != null && !nome.isBlank() && nome.length() >= 3;
		Preconditions.checkArgument(isFiltroValido, "O filtro para listagem é obrigatório e deve ter mais que 2 caracteres.");
		String filtro = nome + "%";
		return categoriaRepository.buscarPor(filtro);
	}
	

	public List<Categoria> listarTodas() {
		return categoriaRepository.listarTodas();
	}

}
