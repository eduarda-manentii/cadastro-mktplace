package br.com.senai.core.service;

import java.util.List;

import com.google.common.base.Preconditions;

import br.com.senai.core.dao.DaoCategoria;
import br.com.senai.core.dao.DaoRestaurante;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.Categoria;

public class CategoriaService {
	
	private DaoCategoria daoCategoria;
	private DaoRestaurante daoRestaurante;
	
	public CategoriaService() {
		this.daoCategoria = FactoryDao.getInstance().getDaoCategoria();
		this.daoRestaurante = FactoryDao.getInstance().getDaoRestaurante();
	}
	
	public void salvar(Categoria categoria) {
		this.validar(categoria);
		boolean isPersistido = categoria.getId() > 0;
		if (isPersistido) {
			this.daoCategoria.alterar(categoria);
		} else {
			this.daoCategoria.inserir(categoria);
		}
	}
	
	public void validar(Categoria categoria) {
		Preconditions.checkNotNull(categoria, "A categoria nao pode ser nula.");
			boolean isNomeInvalido = categoria.getNome() == null
					|| categoria.getNome().isBlank()
					|| categoria.getNome().length() > 100
					|| categoria.getNome().length() < 3;
					
		Preconditions.checkArgument(!isNomeInvalido, "O nome da categoria deve possuir"
			+ " entre 3 a 100 caracteres.");
	}
	
	public void excluirPor(int idCategoria) {
		Preconditions.checkArgument(idCategoria > 0, "O id para remoção"
				+ " da categoria deve ser maior que 0.");
		
		boolean isRemocaoInvalida = daoRestaurante.validarRemocao(idCategoria);
		Preconditions.checkArgument(!isRemocaoInvalida, "Nao é possível remover uma"
				+ " categoria vinculada a um restaurante.");
		
		this.daoCategoria.excluirPor(idCategoria);
	}
	
	public List<Categoria> listarPor(String nome) {
		Boolean isFiltroValido = nome != null && !nome.isBlank() && nome.length() >= 3;
		Preconditions.checkArgument(isFiltroValido, "O filtro para listagem é obrigatório e deve ter mais que 2 caracteres.");
		String filtro = nome + "%";
		return daoCategoria.listarPor(filtro);
	}
	
	public List<Categoria> listarTodas() {
		return daoCategoria.listarTodas();
	}
}
