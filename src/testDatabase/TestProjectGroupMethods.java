package testDatabase;

import static org.junit.Assert.*;

import java.sql.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.Database;
import database.ProjectGroup;


public class TestProjectGroupMethods {
	
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
	@Test
	public void testAddNewProjectGroup() {
		assertTrue(db.addProjectGroup(new ProjectGroup("TestName", 0, 7, 12)));
	}
	
	@Test
	public void testAddExistingProjectGroup() {
		ProjectGroup pg = new ProjectGroup("TestName", 0, 7, 12);
		db.addProjectGroup(pg);
		assertFalse(db.addProjectGroup(pg));
	}
	
	@Test
	public void testAddNewProjectGroupIdSet() {
		ProjectGroup pg = new ProjectGroup("TestName", 0, 7, 12);
		db.addProjectGroup(pg);
		assertEquals(1, pg.getId());
	}
	
	@Test
	public void testActivateProjectGroup() {
		assertTrue(false);
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
}
