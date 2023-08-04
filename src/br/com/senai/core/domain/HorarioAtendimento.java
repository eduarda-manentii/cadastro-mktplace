package br.com.senai.core.domain;

import java.time.LocalTime;
import java.util.Objects;

public class HorarioAtendimento {
	
	private int id;
	private DiasDaSemana diaSemana;
	private LocalTime horaAbertura;
	private LocalTime horaFechamento;
	private Restaurante restaurante;
	
	public HorarioAtendimento(DiasDaSemana diaSemana, LocalTime horaAbertura, 
			LocalTime horaFechamento, Restaurante restaurante) {
		this.diaSemana = diaSemana;
		this.horaAbertura = horaAbertura;
		this.horaFechamento = horaFechamento;
		this.restaurante = restaurante;
	}

	public HorarioAtendimento(int id, DiasDaSemana diaSemana, 
			LocalTime horaAbertura, LocalTime horaFechamento,
			Restaurante restaurante) {
		this(diaSemana, horaAbertura, horaFechamento, restaurante);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DiasDaSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiasDaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public LocalTime getHoraAbertura() {
		return horaAbertura;
	}

	public void setHoraAbertura(LocalTime horaAbertura) {
		this.horaAbertura = horaAbertura;
	}

	public LocalTime getHoraFechamento() {
		return horaFechamento;
	}

	public void setHoraFechamento(LocalTime horaFechamento) {
		this.horaFechamento = horaFechamento;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HorarioAtendimento other = (HorarioAtendimento) obj;
		return id == other.id;
	}

}
