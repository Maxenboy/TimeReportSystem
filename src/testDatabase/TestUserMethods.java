package testDatabase;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.Database;
import database.User;

public class TestUserMethods {

	private Connection conn;
	private Database db;
	@Before
	public void setup() {
		db = new Database();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/puss1302?user=puss1302&password=jks78ww2");
			// conn =
			// DriverManager.getConnection("jdbc:mysql://vm26.cs.lth.se/puss1302?"
			// + "user=puss1302&password=jks78ww2");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		try{
			Statement stmt = conn.createStatement();
		    stmt.executeUpdate("TRUNCATE TABLE project_groups"); 
		    stmt.executeUpdate("TRUNCATE TABLE users"); 
		    stmt.executeUpdate("TRUNCATE TABLE time_reports"); 
		    stmt.executeUpdate("TRUNCATE TABLE activities");
		    stmt.executeUpdate("insert into users (username, password, role) values('admin', 'adminp', 1)");
		    stmt.close();
		} catch (SQLException ex) {
		}
	}

	@Test
	public void testAddUser() {
		User u = new User("User");
		assertTrue(db.addUser(u));
	}
	
	@Test
	public void testAddUserPropertiesSet() {
		User u = new User("User");
		assertTrue(db.addUser(u));
		assertNotNull(u.getId());
		assertNotNull(u.getUsername());
		assertNotNull(u.getPassword());
		assertNotNull(u.isActive());
		assertNotNull(u.getRole());
		assertEquals(u.getProjectGroup(), 0);
	}
	
	public void testAddSameUserTwice() {
		User u = new User("User");
		db.addUser(u);
		assertFalse(db.addUser(u));
	}

}
