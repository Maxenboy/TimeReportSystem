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
	private ProjectGroup pg;
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
		pg = new ProjectGroup("TestName", 0, 7, 12);
	}
	@Test
	public void testAddNewProjectGroup() {
		assertTrue(db.addProjectGroup(pg));
	}
	
	@Test
	public void testAddExistingProjectGroup() {
		db.addProjectGroup(pg);
		assertFalse(db.addProjectGroup(pg));
	}
	
	@Test
	public void testAddNewProjectGroupIdSet() {
		db.addProjectGroup(pg);
		assertEquals(1, pg.getId());
	}
	
	@Test
	public void testGetProjectGroup() {
		db.addProjectGroup(pg);
		assertNotNull(db.getProjectGroup(1));
	}
	
	@Test
	public void testGetProjectGroupThatDoesNotExist() {
		assertNull(db.getProjectGroup(1));
	}
	
	@Test
	public void testActivateProjectGroup() {
		db.addProjectGroup(pg);
		assertTrue(db.activateProjectGroup(1));
		assertTrue(db.getProjectGroup(1).isActive());
	}
	
	@Test
	public void testActivateProjectGroupWithIdThatDoesNotExist() {
		assertFalse(db.activateProjectGroup(33));
	}
	
	@Test
	public void testDeActivateProjectGroup() {
		db.addProjectGroup(pg);
		assertTrue(db.deactivateProjectGroup(1));
		assertFalse(db.getProjectGroup(1).isActive());
	}
	
	@Test
	public void testDeActivateProjectGroupWithIdThatDoesNotExist() {
		assertFalse(db.deactivateProjectGroup(33));
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
