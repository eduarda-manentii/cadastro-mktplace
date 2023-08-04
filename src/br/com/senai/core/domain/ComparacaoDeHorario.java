package br.com.senai.core.domain;

import java.util.Comparator;

public class ComparacaoDeHorario implements Comparator<HorarioAtendimento>  {

	@Override
	public int compare(HorarioAtendimento horario1, HorarioAtendimento horario2) {
	  int diaComparacao = horario1.getDiaSemana().compareTo(horario2.getDiaSemana());
        if (diaComparacao != 0) {
            return diaComparacao;
        }
        return horario1.getHoraAbertura().compareTo(horario2.getHoraAbertura());
	 }
}
