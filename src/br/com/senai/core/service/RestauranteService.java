package br.com.senai.core.service;

import java.util.List;

import br.com.senai.core.dao.DaoHorarioAtendimento;
import br.com.senai.core.dao.DaoRestaurante;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Restaurante;

public class RestauranteService {

	private DaoRestaurante daoRestaurante;
	private DaoHorarioAtendimento daoHorarioAtendimento;

	public RestauranteService() {
		this.daoRestaurante = FactoryDao.getInstance().getDaoRestaurante();
		this.daoHorarioAtendimento = FactoryDao.getInstance().getHorarioAtendimento();
	}

	public void salvar(Restaurante restaurante) {
		this.validar(restaurante);
		boolean isPersistido = restaurante.getId() > 0;
		if (isPersistido) {
			this.daoRestaurante.alterar(restaurante);
		} else {
			this.daoRestaurante.inserir(restaurante);
		}
	}

	public void validar(Restaurante restaurante) {
		if (restaurante != null) {
			boolean isNomeInvalido = restaurante.getNome() == null || restaurante.getNome().isBlank()
					|| restaurante.getNome().length() > 250 || restaurante.getNome() == null;
			if (isNomeInvalido) {
				throw new IllegalArgumentException("O nome não pode ser vazio" + " e aceita até 250 caracteres.");
			}

			boolean isCategoriaInvalida = (restaurante.getCategoria() == null)
					|| restaurante.getCategoria().getId() <= 0;
			if (isCategoriaInvalida) {
				throw new IllegalArgumentException("A categoria é obrigatória.");
			}

			boolean isDescricaoInvalida = restaurante.getDescricao() == null || restaurante.getDescricao().isBlank();
			if (isDescricaoInvalida) {
				throw new IllegalArgumentException("A descrição é obrigatória.");
			}

			boolean isLogradouroInvalido = restaurante.getEndereco().getLongradouro() == null
					|| restaurante.getEndereco().getLongradouro().isBlank()
					|| restaurante.getEndereco().getLongradouro().length() > 200;
			if (isLogradouroInvalido) {
				throw new IllegalArgumentException("O logradouro é obrigatório e deve conter" + " até 200 caracteres.");
			}

			boolean isCidadeInvalida = restaurante.getEndereco().getCidade() == null
					|| restaurante.getEndereco().getCidade().isBlank()
					|| restaurante.getEndereco().getCidade().length() > 80;
			if (isCidadeInvalida) {
				throw new IllegalArgumentException("A cidade é obrigatória e deve conter" + " até 80 caracteres.");
			}

			boolean isBairroInvalido = restaurante.getEndereco().getBairro() == null
					|| restaurante.getEndereco().getBairro().isBlank()
					|| restaurante.getEndereco().getBairro().length() > 50;
			if (isBairroInvalido) {
				throw new IllegalArgumentException("O bairro é obrigatório e deve conter" + " até 50 caracteres.");
			}

		} else {
			throw new NullPointerException("O restaurante não pode ser nulo.");
		}

	}

	public void excluirPor(int idRestaurante) {
		if (idRestaurante > 0) {
			boolean isRemocaoInvalida = daoHorarioAtendimento.validarRemocao(idRestaurante);
			if (isRemocaoInvalida) {
				throw new IllegalArgumentException(
						"Não é possível remover uma" + " restaurante vinculada a um horário de atendimento.");
			}
			this.daoRestaurante.excluirPor(idRestaurante);
		} else {
			throw new IllegalArgumentException("O id para remoção" + " da categoria deve ser maior que 0.");
		}
	}

	public List<Restaurante> listarPor(String nome, Categoria categoria) {
		if ((categoria.getId() > 0) && (!nome.isBlank())) {
			String filtroComCategoria = nome + "%";
			return daoRestaurante.listarPor(filtroComCategoria, categoria);

		} else if ((categoria.getId() > 0) && (nome.isBlank())) {
			return daoRestaurante.listarPor(null, categoria);
		}

		else if ((categoria.getId() <= 0) && (!nome.isBlank())) {
			String filtroSemCategoria = "%" + nome + "%";
			return daoRestaurante.listarPor(filtroSemCategoria, null);
		} else {
			throw new IllegalArgumentException("Selecione ao menos um filtro para listagem.");
		}
	}

	public List<Restaurante> listarTodas() {
		return daoRestaurante.listarTodas();
	}

}
