package testDatabase;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.Activity;
import database.Database;
import database.ProjectGroup;
import database.TimeReport;
import database.User;

public class TestStatisticsMethods {
	private Connection conn;
	private Database db;
	private ProjectGroup pg;

	@Before
	public void setup() throws Exception {
		db = new Database();
		pg = new ProjectGroup("TheProject", 1, 7, 180);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/puss1302?user=puss1302&password=jks78ww2");
			// conn =
			// DriverManager.getConnection("jdbc:mysql://vm26.cs.lth.se/puss1302?"
			// + "user=puss1302&password=jks78ww2");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		// Clear tables
		try {
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
	public void testGetStatisticsFilterFail() {
		HashMap<String, ArrayList<String>> map = db.getStatisticsFilter(33);
		for (ArrayList<String> list : map.values()) {
			assertEquals(0, list.size());
		}
	}
	
	@Test
	public void testGetStatisticsFilter() {
		db.addProjectGroup(pg);
		User user1 = new User("ada10xyz");
		User user2 = new User("ain10xyz");
		User user3 = new User("atf10xyz");
		db.addUser(user1);
		db.addUser(user2);
		db.addUser(user3);
		HashMap<Integer, Integer> roles = new HashMap<Integer, Integer>();
		roles.put(user1.getId(), User.ROLE_DEVELOPMENT_GROUP);
		roles.put(user2.getId(), User.ROLE_PROJECT_LEADER);
		roles.put(user3.getId(), User.ROLE_SYSTEM_GROUP);
		db.setUserRoles(roles);
		db.addUserToProjectGroup(user1.getId(), pg.getId());
		db.addUserToProjectGroup(user2.getId(), pg.getId());
		db.addUserToProjectGroup(user3.getId(), pg.getId());
		TimeReport timeReport1 = new TimeReport(1, user1.getId(), pg.getId());
		TimeReport timeReport2 = new TimeReport(2, user1.getId(), pg.getId());
		TimeReport timeReport3 = new TimeReport(3, user1.getId(), pg.getId());
		ArrayList<Activity> activities = new ArrayList<Activity>();
		db.addTimeReport(timeReport1, activities);
		db.addTimeReport(timeReport2, activities);
		db.addTimeReport(timeReport3, activities);
		HashMap<String, ArrayList<String>> map = db.getStatisticsFilter(33);
		for (ArrayList<String> list : map.values()) {
			assertEquals(3, list.size());
		}
	}

}
