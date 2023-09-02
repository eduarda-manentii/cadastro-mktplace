package main.java.br.com.senai.cadastrosmktplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import main.java.br.com.senai.cadastrosmktplace.entity.HorarioAtendimento;

public interface HorarioAtendimentoRepository extends JpaRepository<HorarioAtendimento, Integer> {
	

    @Query(value = "SELECT ah.id AS id_atendimento, ah.dia_semana, ah.hora_abertura, ah.hora_fechamento, " +
            "r.id AS id_restaurante, r.nome AS nome_restaurante, r.descricao, r.logradouro, r.bairro, r.cidade, " +
            "r.complemento, c.id AS id_categoria, c.nome AS nome_categoria " +
            "FROM horarios_atendimento ah " +
            "INNER JOIN restaurantes r ON ah.id_restaurante = r.id " +
            "INNER JOIN categorias c ON r.id_categoria = c.id " +
            "WHERE UPPER(r.nome) = UPPER(:nome) " +
            "ORDER BY CASE " +
            "WHEN dia_semana = 'DOMINGO' THEN 1 " +
            "WHEN dia_semana = 'SEGUNDA' THEN 2 " +
            "WHEN dia_semana = 'TERCA' THEN 3 " +
            "WHEN dia_semana = 'QUARTA' THEN 4 " +
            "WHEN dia_semana = 'QUINTA' THEN 5 " +
            "WHEN dia_semana = 'SEXTA' THEN 6 " +
            "WHEN dia_semana = 'SABADO' THEN 7 END", nativeQuery = true)
    public List<HorarioAtendimento> selectByNomeRest(@Param("nome") String nome);

    @Query(value = "SELECT ah.id AS id_atendimento, ah.dia_semana, ah.hora_abertura, ah.hora_fechamento, " +
            "r.id AS id_restaurante, r.nome AS nome_restaurante, r.descricao, r.logradouro, r.bairro, r.cidade, " +
            "r.complemento, c.id AS id_categoria, c.nome AS nome_categoria " +
            "FROM horarios_atendimento ah " +
            "INNER JOIN restaurantes r ON ah.id_restaurante = r.id " +
            "INNER JOIN categorias c ON r.id_categoria = c.id " +
            "WHERE ah.id_restaurante = :idRestaurante " +
            "AND ah.dia_semana = :diaSemana " +
            "AND ah.id != :id " +
            "AND ((:hora BETWEEN ah.hora_abertura AND ah.hora_fechamento) OR " +
            "(:horaFechamento BETWEEN ah.hora_abertura AND ah.hora_fechamento) OR " +
            "(:horaAbertura <= ah.hora_abertura AND :horaFechamento >= ah.hora_fechamento))", nativeQuery = true)
    public List<HorarioAtendimento> selectByRestAndDay(@Param("idRestaurante") Integer idRestaurante, @Param("diaSemana") String diaSemana,
                                      @Param("id") Integer id, @Param("hora") String hora,
                                      @Param("horaAbertura") String horaAbertura, @Param("horaFechamento") String horaFechamento);

    @Query(value = "SELECT COUNT(horarios_atendimento.id_restaurante) as qtde " +
            "FROM horarios_atendimento " +
            "WHERE horarios_atendimento.id_restaurante = :idRestaurante", nativeQuery = true)
    public Integer selectIdExistente(@Param("idRestaurante") Integer idRestaurante);
	
}
