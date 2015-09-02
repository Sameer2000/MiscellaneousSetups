package com.app.src.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class CrudTemplate {

	private static final String savePerson = "insert into salary (amount, credit_date) values (?, now())";
	private static final String saveSalary = "insert into person (name, salary_id) values (?, last_insert_id())";
	
	

	public static Boolean saveTemplate(Connection connection, int amount, String personName) {
		try {
			if (connection != null) {
				PreparedStatement statement1 = connection.prepareStatement(savePerson);
				PreparedStatement statement2 = connection.prepareStatement(saveSalary);
				statement1.setInt(1, amount);
				statement2.setString(1, personName);
				statement1.execute();
				return statement2.execute();
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Boolean getPersonQuaterly(Connection connection, int amount, String personName) {
		try {
			if (connection != null) {
				PreparedStatement statement1 = connection.prepareStatement(savePerson);
				PreparedStatement statement2 = connection.prepareStatement(saveSalary);
				statement1.setInt(1, amount);
				statement2.setString(1, personName);
				statement1.execute();
				return statement2.execute();
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Boolean getPersonYearly(Connection connection, int amount, String personName) {
		try {
			if (connection != null) {
				PreparedStatement statement1 = connection.prepareStatement(savePerson);
				PreparedStatement statement2 = connection.prepareStatement(saveSalary);
				statement1.setInt(1, amount);
				statement2.setString(1, personName);
				statement1.execute();
				return statement2.execute();
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
