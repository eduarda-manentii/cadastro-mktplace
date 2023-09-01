package main.java.br.com.senai.cadastrosmktplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import main.java.br.com.senai.cadastrosmktplace.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer>  {

}
