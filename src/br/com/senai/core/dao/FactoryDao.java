package br.com.senai.core.dao;

import br.com.senai.core.dao.postgres.DaoPostegresqlHorarioAtendimento;
import br.com.senai.core.dao.postgres.DaoPostgresqlCategoria;
import br.com.senai.core.dao.postgres.DaoPostgresqlRestaurante;

public class FactoryDao {
	
	private static FactoryDao instance;
	
	private FactoryDao() {}
	
	public DaoCategoria getDaoCategoria() {
		return new DaoPostgresqlCategoria();
	}
	
	public DaoRestaurante getDaoRestaurante() {
		return new DaoPostgresqlRestaurante();
	}
	
	public DaoHorarioAtendimento getHorarioAtendimento() {
		return new DaoPostegresqlHorarioAtendimento();
	}
	
	public static FactoryDao getInstance() {
		if (instance == null) {
			instance = new FactoryDao();
		}
		return instance;
	}

}
