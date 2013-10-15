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
import java.util.List;

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
	
	@Test
	public void testAddSameUserTwice() {
		User u = new User("User");
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
		db.addUser(new User("User"));
		User u = db.getUser("User");
		assertNotNull(db.loginUser("User", u.getPassword()));
	}
	
	@Test
	public void testUserLoginWrongPassword() {
		db.addUser(new User("User"));
		assertNull(db.loginUser("User", "wrongpassword"));
	}
	
	@Test
	public void testGetUserByUsername() {
		db.addUser(new User("User"));
		User u = db.getUser("User");
		assertEquals("User", u.getUsername());
	}
	
	@Test
	public void testGetUserByUsernameThatDoesNotExist() {
		assertNull(db.getUser("User"));
	}
	
	@Test
	public void testGetUsersEmpty() {
		assertEquals(db.getUsers().size(), 0);
	}
	
	@Test
	public void testGetUsersWithTwoUsers() {
		db.addUser(new User("User1"));
		db.addUser(new User("User2"));
		assertEquals(2, db.getUsers().size());
	}
	
	@Test
	public void testGetUsersWithTwentyUsers() {
		for(int i = 0; i < 20; i++) {
			String name = "User";
			name += i;
			db.addUser(new User(name));
		}
		assertEquals(20, db.getUsers().size());
	}
	
	@Test
	public void testGetUserById() {
		db.addUser(new User("User"));
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
	public void testActivateUser() {
		db.addUser(new User("User"));
		assertTrue(db.activateUser(2));
	}
	
	@Test
	public void testActivateUserActiveSet() {
		db.addUser(new User("User"));
		db.activateUser(2);
		User u = db.getUser(2);
		assertTrue(u.isActive());
	}
	
	@Test
	public void testDeActivateUser() {
		db.addUser(new User("User"));
		assertTrue(db.deactivateUser(2));
	}
	
	@Test
	public void testSetUsersRoles() {
		db.addUser(new User("User1"));
		User u = db.getUser("User1");
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(u.getId(), User.ROLE_DEVELOPMENT_GROUP);
		assertTrue(db.setUserRoles(map));
		assertEquals(User.ROLE_DEVELOPMENT_GROUP, db.getUser("User1").getRole());
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
