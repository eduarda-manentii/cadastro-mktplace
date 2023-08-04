package br.com.senai.core.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.senai.core.dao.DaoRestaurante;
import br.com.senai.core.dao.ManagerDb;
import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Endereco;
import br.com.senai.core.domain.Restaurante;

public class DaoPostgresqlRestaurante implements DaoRestaurante {
	
	private final String INSERT = "INSERT INTO restaurantes (nome, descricao, cidade, logradouro,"
			+ " bairro, complemento, id_categoria) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private final String UPDATE = "UPDATE restaurantes SET "
			+ "nome = ?, "
			+ "descricao = ?, "
			+ "cidade = ?, "
			+ "logradouro = ?, "
			+ "bairro = ?, "
			+ "complemento = ?, "
			+ "id_categoria = ? "
			+ "WHERE id = ?";
	
	private final String DELETE = "DELETE FROM restaurantes WHERE id = ?";
	
	private final String SELECT_BY_ID = "SELECT "
			+ "r.id id_restaurante, "
			+ "r.nome nome_restaurante, "
			+ "r.descricao, "
			+ "r.cidade, "
			+ "r.logradouro, "
			+ "r.bairro, "
			+ "r.complemento, "
			+ "c.id id_categoria, "
			+ "c.nome nome_categoria "
			+ "FROM restaurantes r, categorias c "
			+ "WHERE r.id_categoria = c.id AND "
			+ "r.id = ?";
	
	private final String SELECT_BY_NOME_CATEG = "SELECT "
			+ "r.id id_restaurante, "
			+ "r.nome nome_restaurante, "
			+ "r.descricao, r.cidade, r.logradouro, "
			+ "r.bairro, r.complemento, "
			+ "c.id id_categoria, c.nome nome_categoria "
			+ "FROM categorias c, "
			+ "restaurantes r "
			+ "WHERE r.id_categoria = c.id";
	
	private final String SELECT_TODES = "SELECT "
			+ "r.id id_restaurante, "
			+ "r.nome nome_restaurante, "
			+ "r.descricao, "
			+ "r.cidade, "
			+ "r.logradouro, "
			+ "r.bairro, "
			+ "r.complemento, "
			+ "c.id id_categoria, "
			+ "c.nome nome_categoria "
			+ "FROM restaurantes r, "
			+ "categorias c "
			+ "WHERE r.id_categoria = c.id "
			+ "ORDER BY LOWER(r.nome)"; 
	
	private final String SELECT_ID_EXISTENTE = "SELECT COUNT (restaurantes.id_categoria) as qtde "
			+ "FROM restaurantes "
			+ "WHERE restaurantes.id_categoria = ?";

	private Connection conexao;
	
	public DaoPostgresqlRestaurante() {
		this.conexao = ManagerDb.getInstance().getConexao();
	}

	@Override
	public void inserir(Restaurante restaurante) {
		PreparedStatement ps = null;
		try {
			ps = conexao.prepareStatement(INSERT);
			ps.setString(1, restaurante.getNome());
			ps.setString(2, restaurante.getDescricao());
			ps.setString(3, restaurante.getEndereco().getCidade());
			ps.setString(4, restaurante.getEndereco().getLongradouro());
			ps.setString(5, restaurante.getEndereco().getBairro());
			ps.setString(6, restaurante.getEndereco().getComplemento());
			ps.setInt(7, restaurante.getCategoria().getId());
			ps.execute();
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro na inserção do restaurante."
					+ " Motivo: " + ex.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
		}
	}

	@Override
	public void alterar(Restaurante restaurante) {
		PreparedStatement ps = null;
		try {
			ManagerDb.getInstance().configurarAutoCommitDa(conexao, false);
			ps = conexao.prepareStatement(UPDATE);
			ps.setString(1, restaurante.getNome());
			ps.setString(2, restaurante.getDescricao());
			ps.setString(3, restaurante.getEndereco().getCidade());
			ps.setString(4, restaurante.getEndereco().getLongradouro());
			ps.setString(5, restaurante.getEndereco().getBairro());
			ps.setString(6, restaurante.getEndereco().getComplemento());
			ps.setInt(7, restaurante.getCategoria().getId());
			ps.setInt(8, restaurante.getId());
			
			boolean isAlteracaoOK = ps.executeUpdate() == 1;
			if (isAlteracaoOK) {
				this.conexao.commit();
			} else {
				this.conexao.rollback();
			}
			ManagerDb.getInstance().configurarAutoCommitDa(conexao, true);
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro na alteração do "
					+ "restaurante. Motivo: " + ex.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
		}
		
	}

	@Override
	public void excluirPor(int id) {
		PreparedStatement ps = null;
		try {
			ManagerDb.getInstance().configurarAutoCommitDa(conexao, false);
			ps = conexao.prepareStatement(DELETE);
			ps.setInt(1, id);
			boolean isExclusaoOk = ps.executeUpdate() == 1;
			if (isExclusaoOk) {
				this.conexao.commit();
			} else {
				this.conexao.rollback();
			}
			ManagerDb.getInstance().configurarAutoCommitDa(conexao, true);
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro ao excluir o"
					+ " restaurante. Motivo: " + ex.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
		}
		
	}

	@Override
	public Restaurante buscarPor(int id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(SELECT_BY_ID);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next()) {
				return extrairDo(rs);
			}
			return null;
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro ao buscar por"
					+ " id no restaurante. Motivo: " + ex.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}

	@Override
	public List<Restaurante> listarPor(String nome, Categoria categoria) {
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuilder consulta = new StringBuilder(SELECT_BY_NOME_CATEG);
			if(categoria != null) {
				consulta.append(" AND c.id = ? ");
			}
			
			if(nome != null && !nome.isBlank()) {
				consulta.append(" AND Upper(r.nome) LIKE Upper(?) ");
			}
			
			consulta.append(" ORDER BY r.nome ");
			
			
			ps = conexao.prepareStatement(consulta.toString());
			int indice = 1;
			
			if(categoria != null) {
				ps.setInt(indice, categoria.getId());
				indice++;
			}
			
			if(nome != null && !nome.isBlank()) {
				ps.setString(indice, nome);
			}
			
			rs = ps.executeQuery();
			while(rs.next()) {
				restaurantes.add(extrairDo(rs));
			}
			return restaurantes;
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro ao listar o "
					+ "nome do restaurante. Motivo: " + ex.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}

	@Override
	public List<Restaurante> listarTodas() {
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(SELECT_TODES);
			rs = ps.executeQuery();
			while(rs.next()) {
				restaurantes.add(extrairDo(rs));
			}
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro na listagem"
					+ " dos restaurantes. Motivo: " + ex.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
		return restaurantes;
	}

	@Override
	public boolean validarRemocao(int id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(SELECT_ID_EXISTENTE);
			ps.setInt(1, id);
			boolean isValidaoOk = false;
			rs = ps.executeQuery();
			if(rs.next()) {
				isValidaoOk = rs.getInt("qtde") > 0;
			}
			return isValidaoOk;
		} catch (Exception ex)  {
			throw new RuntimeException("Ocorreu um erro na validação "
					+ "de id para remoção da categoria. Motivo: " + ex.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}
	
	private Restaurante extrairDo(ResultSet rs) {
		try {
			int idRestaurante = rs.getInt("id_restaurante");
			String nomeRestaurante = rs.getString("nome_restaurante");
			String descricao = rs.getString("descricao");
			
			String cidade = rs.getString("cidade");
			String logradouro = rs.getString("logradouro");
			String bairro = rs.getString("bairro");
			String complemento = rs.getString("complemento");
			Endereco endereco = new Endereco(cidade, logradouro, bairro, complemento);
			
			int idCategoria = rs.getInt("id_categoria");
			String nomeCategoria = rs.getString("nome_categoria");
			Categoria categoria = new Categoria(idCategoria, nomeCategoria);
			
			return new Restaurante(idRestaurante, nomeRestaurante, descricao, endereco, categoria);
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro ao "
					+ "extrair o restaurante. Motivo: " + ex.getMessage());
		}
	}

}
