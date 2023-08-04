package br.com.senai.core.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.com.senai.core.dao.DaoHorarioAtendimento;
import br.com.senai.core.dao.ManagerDb;
import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.DiasDaSemana;
import br.com.senai.core.domain.Endereco;
import br.com.senai.core.domain.HorarioAtendimento;
import br.com.senai.core.domain.Restaurante;

public class DaoPostegresqlHorarioAtendimento implements DaoHorarioAtendimento {

	private final String INSERT = "INSERT INTO horarios_atendimento ("
			+ "dia_semana, hora_abertura, hora_fechamento, id_restaurante) "
			+ "VALUES (?, ?, ?, ?)";
	
	private final String UPDATE = "UPDATE horarios_atendimento SET "
			+ "dia_semana = ?, "
			+ "hora_abertura = ?, "
			+ "hora_fechamento = ?, "
			+ "id_restaurante = ? "
			+ "WHERE id = ?";
	
	private final String DELETE = "DELETE FROM horarios_atendimento WHERE id = ?";
	
	private final String SELECT_BY_NOME_REST = "SELECT "
			+ "ah.id id_atendimento, "
			+ "ah.dia_semana, "
			+ "ah.hora_abertura, "
			+ "ah.hora_fechamento, "
			+ "r.id id_restaurante, "
			+ "r.nome nome_restaurante, "
			+ "r.descricao, "
			+ "r.cidade, "
			+ "r.bairro, "
			+ "r.logradouro, "
			+ "r.complemento, "
			+ "c.id id_categoria, "
			+ "c.nome nome_categoria "
			+ "FROM horarios_atendimento ah, "
			+ "restaurantes r, categorias c "
			+ "WHERE ah.id_restaurante = r.id AND "
			+ "r.id_categoria = c.id AND "
			+ "r.nome = ? "
			+ "ORDER BY CASE "
			+ " WHEN dia_semana = 'DOMINGO' THEN 1 "
			+ " WHEN dia_semana = 'SEGUNDA' THEN 2 "
			+ " WHEN dia_semana = 'TERCA' THEN 3 "
			+ " WHEN dia_semana = 'QUARTA' THEN 4 "
			+ " WHEN dia_semana = 'QUINTA' THEN 5 "
			+ " WHEN dia_semana = 'SEXTA' THEN 6 "
			+ " WHEN dia_semana = 'SABADO' THEN 7 "
			+ "END";	
	
	private final String SELECT_BY_REST_AND_DAY = "SELECT "
			+ "ah.id id_atendimento, "
			+ "ah.dia_semana, "
			+ "ah.hora_abertura, "
			+ "ah.hora_fechamento, "
			+ "r.id id_restaurante, "
			+ "r.nome nome_restaurante, "
			+ "r.descricao, "
			+ "r.logradouro, "
			+ "r.bairro, "
			+ "r.cidade, "
			+ "r.complemento, "
			+ "c.id id_categoria, "
			+ "c.nome nome_categoria "
			+ "FROM horarios_atendimento ah, "
			+ "restaurantes r, categorias c "
			+ "WHERE ah.id_restaurante = r.id AND "
			+ "r.id_categoria = c.id AND "
			+ "r.id = ? AND "
			+ "ah.dia_semana = ? AND "
			+ "ah.id != ? AND "
			+ "((? BETWEEN ah.hora_abertura AND ah.hora_fechamento) OR "
			+ "(? BETWEEN ah.hora_abertura AND ah.hora_fechamento) OR "
			+ "(? <= ah.hora_abertura AND ? >= ah.hora_fechamento))";
	

	private final String SELECT_ID_EXISTENTE = "SELECT COUNT (horarios_atendimento.id_restaurante) as qtde "
			+ "FROM horarios_atendimento "
			+ "WHERE horarios_atendimento.id_restaurante = ?";

	private Connection conexao;
	
	public DaoPostegresqlHorarioAtendimento() {
		this.conexao = ManagerDb.getInstance().getConexao();
	}
	
	@Override
	public void inserir(HorarioAtendimento horarioAtendimento) {
		PreparedStatement ps = null;
		try {
			ps = conexao.prepareStatement(INSERT);
			ps.setString(1, horarioAtendimento.getDiaSemana().name());
			ps.setTime(2, Time.valueOf(horarioAtendimento.getHoraAbertura()));
			ps.setTime(3, Time.valueOf(horarioAtendimento.getHoraFechamento()));
			ps.setInt(4, horarioAtendimento.getRestaurante().getId());
			ps.execute();
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro na inserção do "
					+ "horario de atendimento. Motivo: " + ex.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
		}
	}

	@Override
	public void alterar(HorarioAtendimento horarioAtendimento) {
		PreparedStatement ps = null;
		try {
			ManagerDb.getInstance().configurarAutoCommitDa(conexao, false);
			ps = conexao.prepareStatement(UPDATE);
			ps.setString(1, horarioAtendimento.getDiaSemana().name());
			ps.setTime(2, Time.valueOf(horarioAtendimento.getHoraAbertura()));
			ps.setTime(3, Time.valueOf(horarioAtendimento.getHoraFechamento()));
			ps.setInt(4, horarioAtendimento.getRestaurante().getId());
			ps.setInt(5, horarioAtendimento.getId());
			
			boolean isAlteracaoOk = ps.executeUpdate() == 1;
			if (isAlteracaoOk) {
				this.conexao.commit();
			} else {
				this.conexao.rollback();
			}
			ManagerDb.getInstance().configurarAutoCommitDa(conexao, true);
			
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro na alteração do "
					+ "horario de atendimento. Motivo: " + ex.getMessage());
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
					+ " horario de atendimento. Motivo: " + ex.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
		}
		
	}

	@Override
	public List<HorarioAtendimento> listarPor(Restaurante restaurante) {
		List<HorarioAtendimento> horariosAtendimentos = new ArrayList<HorarioAtendimento>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(SELECT_BY_NOME_REST);
			ps.setString(1, restaurante.getNome());
			rs = ps.executeQuery();
			while (rs.next()) {
				horariosAtendimentos.add(extrairDo(rs));
			}
			return horariosAtendimentos;
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro ao listar o "
					+ "restaurante pelo horario de atendimento. Motivo: " + ex.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}
	
	@Override
	public List<HorarioAtendimento> buscarConflitantePor(HorarioAtendimento horarioAtendimento) {
		List<HorarioAtendimento> horariosAtendimentos = new ArrayList<HorarioAtendimento>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(SELECT_BY_REST_AND_DAY);
			ps.setInt(1, horarioAtendimento.getRestaurante().getId());
			ps.setString(2,  horarioAtendimento.getDiaSemana().name());
			ps.setInt(3, horarioAtendimento.getId());
			ps.setTime(4, Time.valueOf(horarioAtendimento.getHoraAbertura()));
			ps.setTime(5, Time.valueOf(horarioAtendimento.getHoraFechamento()));
			ps.setTime(6, Time.valueOf(horarioAtendimento.getHoraAbertura()));
			ps.setTime(7, Time.valueOf(horarioAtendimento.getHoraFechamento()));
			rs = ps.executeQuery();
			if(rs.next()) {
				horariosAtendimentos.add(extrairDo(rs));
			}
			return horariosAtendimentos;
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro ao "
					+ "listar os horarios do atendimento. "
					+ "Motivo: " + ex.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
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
					+ "de id para remoção do restaurante. Motivo: " + ex.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}
	
	private HorarioAtendimento extrairDo(ResultSet rs) {
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
			
			Restaurante restaurante = new Restaurante(idRestaurante, nomeRestaurante,
					descricao, endereco, categoria);
			
			int idHorarioAtendimento = rs.getInt("id_atendimento");
			String diaSemana = rs.getString("dia_semana");
			String diaSemanaUpper = diaSemana.toUpperCase();
			DiasDaSemana diasDeSemana = DiasDaSemana.valueOf(diaSemanaUpper);
			Time horaAbertura = rs.getTime("hora_abertura");
			Time horaFechamento = rs.getTime("hora_fechamento");
			LocalTime horaAberturaLocal = horaAbertura.toLocalTime();
			LocalTime horaFechamentoLocal = horaFechamento.toLocalTime();
			return new HorarioAtendimento(idHorarioAtendimento, diasDeSemana,
					horaAberturaLocal, horaFechamentoLocal, restaurante);
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro ao "
					+ "extrair o horario de atendimento. Motivo: " + ex.getMessage());
		}
	}

}
