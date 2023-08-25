package br.com.senai.core.service;

import java.util.List;

import com.google.common.base.Preconditions;

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
		Preconditions.checkNotNull(restaurante, "O restaurante não pode ser nulo.");
		
		boolean isNomeInvalido = restaurante.getNome() == null || restaurante.getNome().isBlank()
				|| restaurante.getNome().length() > 250 || restaurante.getNome() == null;
		Preconditions.checkArgument(!isNomeInvalido, "O nome não pode ser vazio e aceita até 250 caracteres.");
		
		boolean isCategoriaInvalida = (restaurante.getCategoria() == null)
				|| restaurante.getCategoria().getId() <= 0;
		Preconditions.checkArgument(!isCategoriaInvalida, "A categoria é obrigatória.");
		
		boolean isDescricaoInvalida = restaurante.getDescricao() == null || restaurante.getDescricao().isBlank();
		Preconditions.checkArgument(!isDescricaoInvalida, "A descrição é obrigatória.");
		
		boolean isLogradouroInvalido = restaurante.getEndereco().getLongradouro() == null
				|| restaurante.getEndereco().getLongradouro().isBlank()
				|| restaurante.getEndereco().getLongradouro().length() > 200;
		Preconditions.checkArgument(!isLogradouroInvalido, "O logradouro é obrigatório e deve conter até 200 caracteres.");
		
		boolean isCidadeInvalida = restaurante.getEndereco().getCidade() == null
				|| restaurante.getEndereco().getCidade().isBlank()
				|| restaurante.getEndereco().getCidade().length() > 80;
		Preconditions.checkArgument(!isCidadeInvalida, "A cidade é obrigatória e deve conter até 80 caracteres.");
		
		boolean isBairroInvalido = restaurante.getEndereco().getBairro() == null
				|| restaurante.getEndereco().getBairro().isBlank()
				|| restaurante.getEndereco().getBairro().length() > 50;
		Preconditions.checkArgument(!isBairroInvalido, "O bairro é obrigatório e deve conter até 50 caracteres.");
	}

	public void excluirPor(int idRestaurante) {
		Preconditions.checkArgument(idRestaurante > 0, "O id para remoção"
				+ " do restaurante deve ser maior que 0.");
		
		boolean isRemocaoInvalida = daoHorarioAtendimento.validarRemocao(idRestaurante);
		Preconditions.checkArgument(!isRemocaoInvalida, "Nao é possível remover um"
				+ " restaurante vinculada a um hórario de atendimento.");
		
		this.daoRestaurante.excluirPor(idRestaurante);
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
