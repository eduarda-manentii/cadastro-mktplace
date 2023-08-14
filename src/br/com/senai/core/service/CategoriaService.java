package br.com.senai.core.service;

import java.util.List;

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
		if(categoria != null) {
			boolean isNomeInvalido = categoria.getNome() == null
					|| categoria.getNome().isBlank()
					|| categoria.getNome().length() > 100
					|| categoria.getNome().length() < 3;
					
			if (isNomeInvalido) {
				throw new IllegalArgumentException("O nome da categoria deve possuir"
						+ " entre 3 a 100 caracteres.");
			}
			
		} else {
			throw new NullPointerException("A categoria não pode ser nula.");
		}
	}
	
	public void excluirPor(int idCategoria) {
		if (idCategoria > 0) {
			boolean isRemocaoInvalida = daoRestaurante.validarRemocao(idCategoria);
			if (isRemocaoInvalida) {
				throw new IllegalArgumentException("Não é possível remover uma"
						+ " categoria vinculada a um restaurante.");
			}
			this.daoCategoria.excluirPor(idCategoria);
		} else {
			throw new IllegalArgumentException("O id para remoção"
					+ " da categoria deve ser maior que 0.");
		}
	}
	
	public List<Categoria> listarPor(String nome) {
		if(nome != null && !nome.isBlank() && nome.length() >= 3) {
			String filtro = nome + "%";
			return daoCategoria.listarPor(filtro);
		} else {
			throw new IllegalArgumentException("O filtro para listagem é obrigatório e deve ter mais que 2 caracteres.");
		}
	}
	
	public List<Categoria> listarTodas() {
		return daoCategoria.listarTodas();
	}
}
