package br.com.senai.view.componentes.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.senai.core.domain.HorarioAtendimento;

public class HorarioAtendimentoTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private List<HorarioAtendimento> horariosAtendimento;
	
	public HorarioAtendimentoTableModel(List<HorarioAtendimento> horarioAtendimento) {
		this.horariosAtendimento = horarioAtendimento;
	}
	
	public HorarioAtendimento getPor(int rowIndex) {
		return horariosAtendimento.get(rowIndex);
	}
	
	public String getColumnName(int columnIndex) {
		if(columnIndex == 0) {
			return "Dia da Semana";
		} else if (columnIndex == 1) {
			return "Abertura";
		} else if (columnIndex == 2) {
			return "Fechamento";
		}
		throw new IllegalArgumentException("Índice inválido.");
	}
	
	@Override
	public int getRowCount() {
		return horariosAtendimento.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		HorarioAtendimento horarioDaLinha = horariosAtendimento.get(rowIndex);
		if(columnIndex == 0) {
			return horarioDaLinha.getDiaSemana();
		} else if (columnIndex == 1) {
			return horarioDaLinha.getHoraAbertura();
		} else if (columnIndex == 2) {
			return horarioDaLinha.getHoraFechamento();
		} 
		throw new IllegalArgumentException("Índíce inválido.");
	}
	
	public void removePor(int rowIndex) {
		this.horariosAtendimento.remove(rowIndex);
		fireTableDataChanged();
	}
	
	public boolean isVazio() {
		return horariosAtendimento.isEmpty();
	}
	
	

}
