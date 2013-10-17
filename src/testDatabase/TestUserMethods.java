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
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.Database;
import database.ProjectGroup;
import database.User;

public class TestUserMethods {

	private Connection conn;
	private Database db;
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
		u = new User("User");
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
		assertTrue(db.addUser(u));
	}
	
	@Test
	public void testAddUserPropertiesSet() {
		assertTrue(db.addUser(u));
		assertNotNull(u.getId());
		assertNotNull(u.getUsername());
		assertNotNull(u.getPassword());
		assertNotNull(u.isActive());
		assertNotNull(u.getRole());
		assertEquals(u.getProjectGroup(), 0);
	}
	
	@Test
	public void testAddSameUserTwice() {
		db.addUser(u);
		assertFalse(db.addUser(u));
	}
	
	@Test
	public void testLoginAdmin() {
		assertNotNull(db.loginUser("admin", "adminp"));
	}
	
	@Test
	public void testLoginAdminWrongPassword() {
		assertNull(db.loginUser("admin", "wrongpassword"));
	}
	
	@Test
	public void testUserLogin() {
		db.addUser(u);
		User u = db.getUser("User");
		assertNotNull(db.loginUser("User", u.getPassword()));
	}
	
	@Test
	public void testUserLoginWrongPassword() {
		db.addUser(u);
		assertNull(db.loginUser("User", "wrongpassword"));
	}
	
	@Test
	public void testGetUserByUsername() {
		db.addUser(u);
		User u = db.getUser("User");
		assertEquals("User", u.getUsername());
	}
	
	@Test
	public void testGetUserByUsernameThatDoesNotExist() {
		assertNull(db.getUser("User"));
	}
	
	@Test
	public void testGetUsersEmpty() {
		assertEquals(db.getUsers().size(), 1); // 1 since admin always is there
	}
	
	@Test
	public void testGetUsersWithTwoUsers() {
		db.addUser(new User("User1"));
		db.addUser(new User("User2"));
		assertEquals(3, db.getUsers().size()); // 3 since admin always is there
	}
	
	@Test
	public void testGetUsersWithTwentyUsers() {
		for(int i = 0; i < 20; i++) {
			String name = "User";
			name += i;
			db.addUser(new User(name));
		}
		assertEquals(21, db.getUsers().size()); // 21 since admin always is there
	}
	
	@Test
	public void testGetUserById() {
		db.addUser(u);
		assertNotNull(db.getUser(2));
	}
	
	@Test
	public void testGetUserByIdThatDoesNotExist() {
		assertNull(db.getUser(2));
	}
	
	@Test
	public void testActivateUserByIdThatDoesNotExist() {
		assertFalse(db.activateUser(2));
	}
	
	@Test
	public void testGetUserByProjectIdThatDoesNotExist() {
		assertEquals(0, db.getUsers(11).size());
	}
	
	@Test
	public void testGetUserByProjectId() {
		ProjectGroup pg = new ProjectGroup("TheProject", 0, 7, 123);
		db.addProjectGroup(pg);
		db.addUser(u);
		db.addUserToProjectGroup(u.getId(), pg.getId());
		assertEquals(1, db.getUsers(pg.getId()).size());
	}
	
	@Test
	public void testActivateUser() {
		db.addUser(u);
		assertTrue(db.activateUser(2));
	}
	
	@Test
	public void testActivateUserActiveSet() {
		db.addUser(u);
		db.activateUser(2);
		User u = db.getUser(2);
		assertTrue(u.isActive());
	}
	
	@Test
	public void testDeActivateUser() {
		db.addUser(u);
		assertTrue(db.deactivateUser(2));
	}
	
	@Test
	public void testSetUsersRoles() {
		db.addUser(u);
		User u1 = db.getUser("User");
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(u1.getId(), User.ROLE_DEVELOPMENT_GROUP);
		assertTrue(db.setUserRoles(map));
		assertEquals(User.ROLE_DEVELOPMENT_GROUP, db.getUser("User").getRole());
	}
	
	@Test
	public void testSetUsersRolesMultiple() {
		db.addUser(new User("User1"));
		db.addUser(new User("User2"));
		db.addUser(new User("User3"));
		db.addUser(new User("User4"));
		
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(2, User.ROLE_DEVELOPMENT_GROUP);
		map.put(3, User.ROLE_PROJECT_LEADER);
		map.put(4, User.ROLE_SYSTEM_GROUP);
		map.put(5, User.ROLE_SYSTEM_LEADER);
		assertTrue(db.setUserRoles(map));
		assertEquals(User.ROLE_DEVELOPMENT_GROUP, db.getUser("User1").getRole());
		assertEquals(User.ROLE_PROJECT_LEADER, db.getUser("User2").getRole());
		assertEquals(User.ROLE_SYSTEM_GROUP, db.getUser("User3").getRole());
		assertEquals(User.ROLE_SYSTEM_LEADER, db.getUser("User4").getRole());
	}
	
	
}
