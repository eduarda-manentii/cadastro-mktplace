package main.java.br.com.senai.cadastrosmktplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Endereco {

	@Column(name = "cidade")
	@NotBlank(message = "A cidade é obrigatória.")
	@Size(max = 80, message = "O nome da cidade não dever conter mais de 80 caracteres.")
	private String cidade;
	
	@Column(name = "logradouro")
	@NotBlank(message = "O logradouro é obrigatório.")
	@Size(max = 200, message = "O logradouro da cidade não dever conter mais de 200 caracteres.")
	private String logradouro;
	
	@Column(name = "bairro")
	@NotBlank(message = "O bairro é obrigatório.")
	@Size(max = 50, message = "O bairro da cidade não dever conter mais de 50 caracteres.")
	private String bairro;
	
	@Column(name = "complemento")
	private String complemento;
	
}