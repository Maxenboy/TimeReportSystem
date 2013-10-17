package testDatabase;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
		db.addUserToProjectGroup(user1.getId(), pg.getId());
		db.addUserToProjectGroup(user2.getId(), pg.getId());
		db.addUserToProjectGroup(user3.getId(), pg.getId());
		HashMap<Integer, Integer> roles = new HashMap<Integer, Integer>();
		roles.put(user1.getId(), User.ROLE_DEVELOPMENT_GROUP);
		roles.put(user2.getId(), User.ROLE_PROJECT_LEADER);
		roles.put(user3.getId(), User.ROLE_SYSTEM_GROUP);
		db.setUserRoles(roles);
		TimeReport timeReport1 = new TimeReport(1, user1.getId(), pg.getId());
		TimeReport timeReport2 = new TimeReport(2, user1.getId(), pg.getId());
		TimeReport timeReport3 = new TimeReport(3, user1.getId(), pg.getId());
		Activity activity1 = new Activity(Activity.ACTIVITY_NR_LECTURE, Activity.ACTIVITY_TYPE_OTHER, 90, timeReport1.getId());
		Activity activity2 = new Activity(Activity.ACTIVITY_NR_EXERCISE, Activity.ACTIVITY_TYPE_OTHER, 100, timeReport2.getId());
		Activity activity3 = new Activity(Activity.ACTIVITY_NR_SDP, Activity.ACTIVITY_TYPE_DEVELOPMENT, 200, timeReport3.getId());
		ArrayList<Activity> activities1 = new ArrayList<Activity>();
		ArrayList<Activity> activities2 = new ArrayList<Activity>();
		ArrayList<Activity> activities3 = new ArrayList<Activity>();
		activities1.add(activity1);
		activities2.add(activity2);
		activities3.add(activity3);
		db.addTimeReport(timeReport1, activities1);
		db.addTimeReport(timeReport2, activities2);
		db.addTimeReport(timeReport3, activities3);
		HashMap<String, ArrayList<String>> map = db.getStatisticsFilter(pg.getId());		
		
		for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
		    assertEquals(3, entry.getValue().size());
		}
	}

	@Test
	/**
	 * Tests getting time for a project group with two users with three time
	 * reports each.
	 */
	public void testGetTimePerWeek() {
		db.addProjectGroup(pg);
		User user1 = new User("ada10xyz");
		User user2 = new User("ain10xyz");
		db.addUser(user1);
		db.addUser(user2);
		HashMap<Integer, Integer> roles = new HashMap<Integer, Integer>();
		roles.put(user1.getId(), User.ROLE_DEVELOPMENT_GROUP);
		roles.put(user2.getId(), User.ROLE_PROJECT_LEADER);
		db.setUserRoles(roles);
		db.addUserToProjectGroup(user1.getId(), pg.getId());
		db.addUserToProjectGroup(user2.getId(), pg.getId());
		
		TimeReport timeReport11 = new TimeReport(1, user1.getId(), pg.getId());
		TimeReport timeReport12 = new TimeReport(2, user1.getId(), pg.getId());
		TimeReport timeReport13 = new TimeReport(3, user1.getId(), pg.getId());
		
		TimeReport timeReport21 = new TimeReport(1, user2.getId(), pg.getId());
		TimeReport timeReport22 = new TimeReport(2, user2.getId(), pg.getId());
		TimeReport timeReport23 = new TimeReport(3, user2.getId(), pg.getId());
		
		Activity activity111 = new Activity(Activity.ACTIVITY_NR_EXERCISE, Activity.ACTIVITY_TYPE_OTHER, 45, timeReport11.getId());
		Activity activity121 = new Activity(Activity.ACTIVITY_NR_HOME_STUDIES, Activity.ACTIVITY_TYPE_OTHER, 60, timeReport12.getId());
		Activity activity122 = new Activity(Activity.ACTIVITY_NR_SRS, Activity.ACTIVITY_TYPE_DEVELOPMENT, 200, timeReport12.getId());
		Activity activity131 = new Activity(Activity.ACTIVITY_NR_EXERCISE, Activity.ACTIVITY_TYPE_OTHER, 90, timeReport13.getId());
		ArrayList<Activity> activities11 = new ArrayList<Activity>();
		activities11.add(activity111);
		ArrayList<Activity> activities12 = new ArrayList<Activity>();
		activities12.add(activity121);
		activities12.add(activity122);
		ArrayList<Activity> activities13 = new ArrayList<Activity>();
		activities13.add(activity131);
		
		Activity activity211 = new Activity(Activity.ACTIVITY_NR_MEETING, Activity.ACTIVITY_TYPE_OTHER, 60, timeReport21.getId());
		Activity activity221 = new Activity(Activity.ACTIVITY_NR_SRS, Activity.ACTIVITY_TYPE_DEVELOPMENT, 60, timeReport22.getId());
		Activity activity231 = new Activity(Activity.ACTIVITY_NR_SRS, Activity.ACTIVITY_TYPE_REWORK, 120, timeReport23.getId());
		ArrayList<Activity> activities21 = new ArrayList<Activity>();
		activities21.add(activity211);
		ArrayList<Activity> activities22 = new ArrayList<Activity>();
		activities22.add(activity221);
		ArrayList<Activity> activities23 = new ArrayList<Activity>();
		activities23.add(activity231);
		
		db.addTimeReport(timeReport11, activities11);
		db.addTimeReport(timeReport12, activities12);
		db.addTimeReport(timeReport13, activities13);
		db.addTimeReport(timeReport21, activities21);
		db.addTimeReport(timeReport22, activities22);
		db.addTimeReport(timeReport23, activities23);
		
		HashMap<String, Integer> expectedTimePerWeek = new HashMap<String, Integer>();
		expectedTimePerWeek.put("1", 105);
		expectedTimePerWeek.put("2", 320);
		expectedTimePerWeek.put("3", 210);
		expectedTimePerWeek.put("totalProjectTime", 635);
		assertEquals(expectedTimePerWeek, db.getTimePerWeek(pg.getId()));
	}
	
	@Test
	/**
	 * Tests getting time for a project group that does not exist.
	 */
	public void testGetTimePerWeekEmpty() {
		assertEquals(1, db.getTimePerWeek(2).size());
		assertEquals(0, db.getTimePerWeek(2).get("totalProjectTime").intValue());
	}
}
