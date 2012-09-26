package net.entcraft.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
	
	private String host, database, username, password;
	private Connection connection;
	
	public SQL(String h, String d, String user, String pass) {
		this.host = h;
		this.database = d;
		this.username = user;
		this.password = pass;
	}
	
	public void refreshConnection() {
		if (connection == null) {
			init();
		}
	}
	
	public void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean init() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, username, password);
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public synchronized void standardQuery(String query) {
		this.refreshConnection();
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean existenceQuery(String query) {
		this.refreshConnection();
		try {
			return sqlQuery(query).next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized ResultSet sqlQuery(String query) {
		this.refreshConnection();
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized boolean checkTable(String table) {
		this.refreshConnection();
		DatabaseMetaData dbm;
		try {
			dbm = connection.getMetaData();
			ResultSet tables = dbm.getTables(null, null, table, null);
			return tables.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
