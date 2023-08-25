package br.com.senai.core.service;

import java.time.LocalTime;
import java.util.List;

import com.google.common.base.Preconditions;

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
		Preconditions.checkNotNull(horarioAtendimento, "O horário de atendimento "
				+ "não pode ser nulo.");
		
		boolean isRestauranteInvalido = horarioAtendimento.getRestaurante() == null
				|| horarioAtendimento.getRestaurante().getId() <= 0;
		Preconditions.checkArgument(!isRestauranteInvalido, "O restaurante é obrigatório.");
			
		boolean isDiaDeSemanaInvalido = horarioAtendimento.getDiaSemana() == null;
		Preconditions.checkArgument(!isDiaDeSemanaInvalido, "O dia da semana é obrigatório.");

		LocalTime timeMax = LocalTime.of(23, 59);
		LocalTime timeMin = LocalTime.of(00, 00);
		boolean isHorarioAbeturaInvalido = horarioAtendimento.getHoraAbertura() == null
				|| horarioAtendimento.getHoraAbertura().isAfter(timeMax) 
				|| horarioAtendimento.getHoraAbertura().isBefore(timeMin);
		Preconditions.checkArgument(!isHorarioAbeturaInvalido, "Digite um valor válido para o horário de abertura.");

		boolean isHorarioFehamentoInvalido = horarioAtendimento.getHoraFechamento() == null
			|| horarioAtendimento.getHoraFechamento().isAfter(timeMax) 
			|| horarioAtendimento.getHoraFechamento().isBefore(timeMin)
			|| horarioAtendimento.getHoraAbertura().equals(horarioAtendimento.getHoraFechamento())
			|| horarioAtendimento.getHoraAbertura().isAfter(horarioAtendimento.getHoraFechamento());
		Preconditions.checkArgument(!isHorarioFehamentoInvalido, "Digite um valor válido para o horário de fechamento.");

		Preconditions.checkArgument(validaHorario(horarioAtendimento), "O horário de atendimento entra "
        		+ "em conflito com outro já cadastrado");
	}
	
	public void excluirPor(int idHorarioAtendimento) {
		Preconditions.checkArgument(idHorarioAtendimento > 0, "O id para remoção"
				+ " do horário de atendimento deve ser maior que 0.");
		this.daoHorarioAtendimento.excluirPor(idHorarioAtendimento);
	}
		
	public boolean validaHorario(HorarioAtendimento horarioAtendimetno) {
		List<HorarioAtendimento> horariosConflitantes = daoHorarioAtendimento.buscarConflitantePor(horarioAtendimetno);
		return horariosConflitantes.size() == 0;
	}
	
	public List<HorarioAtendimento> listarPor(Restaurante restaurante) {
		return daoHorarioAtendimento.listarPor(restaurante);
	}
}
