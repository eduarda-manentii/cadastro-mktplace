package main.java.br.com.senai.cadastrosmktplace.entity;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import main.java.br.com.senai.cadastrosmktplace.entity.enums.DiasDaSemana;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "horarios_atendimento")
@Entity(name = "HorarioAtendimento")
public class HorarioAtendimento {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(name = "dia_semana")
	@NotNull(message = "O dia da semana é obrigatório.")
	@Enumerated(value = EnumType.STRING)
	private DiasDaSemana diaSemana;
	
	@Column(name = "hora_abertura")
	@NotBlank(message = "O horário de abertura é obrigatório.")
	private LocalTime horaAbertura;
	
	@Column(name = "hora_fechamento")
	@NotBlank(message = "O horário de fechamento é obrigatório.")
	private LocalTime horaFechamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_restaurante")
	@NotNull(message = "O restaurante é obrigatório.")
	private Restaurante restaurante;
	
}
