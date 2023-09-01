package main.java.br.com.senai.cadastrosmktplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "restaurantes")
@Entity(name = "Restaurante")
public class Restaurante {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(name = "nome")
	@NotBlank(message = "O nome do restaurante é obrigatório.")
	@Size(min = 3, max = 250, message = "O nome do restaurante deve conter entre 3 e 250 caracteres.")
	@ToString.Include
	private String nome;
	
	@Column(name = "descricao")
	@NotBlank(message = "A descrição do restaurante é obrigatória.")
	private String descricao;
	
	@Embedded
	@Valid
	private Endereco endereco;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_categoria")
	@NotNull(message = "A categoria é obrigatória.")
	private Categoria categoria;
	
	@Transient
	public boolean isPersistido() {
		return getId() != null && getId() > 0;
	}
	
}