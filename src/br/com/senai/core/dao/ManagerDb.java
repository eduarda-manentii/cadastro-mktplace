package br.com.senai.core.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import br.com.senai.core.util.properties.Manipulador;

public class ManagerDb {
	
	private static ManagerDb manager;
	private Connection conexao;
	
	private ManagerDb() {		
		try {
	    	Properties prop = Manipulador.getProp();
			Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
			this.conexao = DriverManager.getConnection(
					prop.getProperty("database-url"), 
					prop.getProperty("database-user"),
					prop.getProperty("database-password"));
		}catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro de conexao "
					+ "com o banco de dados. Motivo: " + e.getMessage());
		}
	}
	
	public Connection getConexao() {
		return conexao;
	}
	
	public void configurarAutoCommitDa(Connection conexao, boolean isHabilitado) {
		try {
			if (conexao != null) {
				conexao.setAutoCommit(isHabilitado);
			}
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro na ativação do"
					+ " autocommit. O motivo é: " + ex.getMessage());
		}
	}
	
	public void fechar(PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro no "
					+ " PreparedStatement. O motivo é: " + ex.getMessage());
		}
	}
	
	public void fechar(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro no fechamento do"
					+ " ResultSet. O motivo é: " + ex.getMessage());
		}
	}
	
	public static ManagerDb getInstance() {
		if (manager == null) {
			manager = new ManagerDb();
		}
		return manager;
	}
	
	

}
