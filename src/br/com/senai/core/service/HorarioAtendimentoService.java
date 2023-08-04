package br.com.senai.core.service;

import java.time.LocalTime;
import java.util.List;

import br.com.senai.core.dao.DaoHorarioAtendimento;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.HorarioAtendimento;
import br.com.senai.core.domain.Restaurante;

public class HorarioAtendimentoService {
	
	private DaoHorarioAtendimento daoHorarioAtendimento;
	
	public HorarioAtendimentoService() {
		this.daoHorarioAtendimento = FactoryDao.getInstance().getHorarioAtendimento();
	}
	
	public void salvar(HorarioAtendimento horarioAtendimento) {
		this.validar(horarioAtendimento);
		boolean isJaPersistido = horarioAtendimento.getId() != 0;
		if (isJaPersistido) {
			this.daoHorarioAtendimento.alterar(horarioAtendimento);
		} else {
			this.daoHorarioAtendimento.inserir(horarioAtendimento);
		} 
	}

	public void validar(HorarioAtendimento horarioAtendimento) {
		if(horarioAtendimento != null) {
			boolean isRestauranteInvalido = horarioAtendimento.getRestaurante() == null
					|| horarioAtendimento.getRestaurante().getId() <= 0;
			
			if(isRestauranteInvalido) {
				throw new IllegalArgumentException("O restaurante � obrigat�rio.");
			}
			boolean isDiaDeSemanaInvalido = horarioAtendimento.getDiaSemana() == null;
			if(isDiaDeSemanaInvalido) {
				throw new IllegalArgumentException("O dia de semana � obrigat�rio.");
			}

			LocalTime timeMax = LocalTime.of(23, 59);
			LocalTime timeMin = LocalTime.of(00, 00);
			boolean isHorarioAbeturaInvalido = horarioAtendimento.getHoraAbertura() == null
					|| horarioAtendimento.getHoraAbertura().isAfter(timeMax) 
					|| horarioAtendimento.getHoraAbertura().isBefore(timeMin);
			if (isHorarioAbeturaInvalido) {
				throw new IllegalArgumentException("Digite um valor v�lido para o hor�rio de abertura.");
			}

			boolean isHorarioFehamentoInvalido = horarioAtendimento.getHoraFechamento() == null
				|| horarioAtendimento.getHoraFechamento().isAfter(timeMax) 
				|| horarioAtendimento.getHoraFechamento().isBefore(timeMin)
				|| horarioAtendimento.getHoraAbertura().equals(horarioAtendimento.getHoraFechamento())
				|| horarioAtendimento.getHoraAbertura().isAfter(horarioAtendimento.getHoraFechamento());
			if (isHorarioFehamentoInvalido) {
				throw new IllegalArgumentException("Digite um valor v�lido para o hor�rio de fechamento.");
			}
			
			if(!validaHorario(horarioAtendimento)) {
                throw new IllegalArgumentException("O hor�rio de atendimento entra "
                		+ "em conflito com outro j� cadastrado");
			}
			
		} else {
			throw new NullPointerException("O hor�rio de atendimento "
					+ "n�o pode ser nulo.");
		}
	}
	
	public void excluirPor(int idHorarioAtendimento) {
		if(idHorarioAtendimento > 0) {
			this.daoHorarioAtendimento.excluirPor(idHorarioAtendimento);
		} else {
			throw new IllegalArgumentException("O id para remo��o"
					+ " dao horario de atendimento deve ser maior que 0.");
		}
	}
		
	public boolean validaHorario(HorarioAtendimento horarioAtendimetno) {
		List<HorarioAtendimento> horariosConflitantes = daoHorarioAtendimento.buscarConflitantePor(horarioAtendimetno);
		return horariosConflitantes.size() == 0;
	}
	
	public List<HorarioAtendimento> listarPor(Restaurante restaurante) {
		return daoHorarioAtendimento.listarPor(restaurante);
	}
}
