package testDatabase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.Database;
import database.ProjectGroup;
import database.User;


public class TestProjectGroupMethods {
	
	private Connection conn;
	private Database db;
	private ProjectGroup pg;
	private User u;
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
		u = new User("User");
		
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
		
	@Test
	public void testAddUserToProjectGroup() {
		db.addProjectGroup(pg);
		db.addUser(u);
		u = db.getUser(u.getUsername());
		assertTrue(db.addUserToProjectGroup(u.getId(), 1));
	}
	
	@Test
	public void testAddUserThatDoesNotExistToProjectGroup() {
		db.addProjectGroup(pg);
		assertFalse(db.addUserToProjectGroup(33, 1));	
	}
	
	@Test
	public void testAddExistingUserToProjectGroupThatDoesNotExist() {
		db.addUser(u);
		u = db.getUser(u.getUsername());
		assertFalse(db.addUserToProjectGroup(u.getId(), 33));
	}
	
	@Test
	public void testAddUserThatDoesNotExistToProjectGroupThatDoesNotExist() {
		assertFalse(db.addUserToProjectGroup(33, 33));
	}
	
	@Test
	public void testRemoveUserFromProjectGroup() {
		db.addUser(u);
		u = db.getUser(u.getUsername());
		db.addProjectGroup(pg);
		db.addUserToProjectGroup(u.getId(), 1);
		assertTrue(db.removeUserFromProjectGroup(u.getId(), 1));
	}
	
	@Test
	public void testRemoveUserThatDoesNotExistFromProjectGroup() {
		db.addProjectGroup(pg);
		assertFalse(db.removeUserFromProjectGroup(3, 1));
	}
	
	@Test
	public void testRemoveUserFromProjectGroupThatDoesNotExist() {
		assertFalse(db.removeUserFromProjectGroup(3, 2));
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
