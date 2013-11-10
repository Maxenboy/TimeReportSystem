package testDatabase;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.Activity;
import database.Database;
import database.ProjectGroup;
import database.TimeReport;
import database.User;

public class TestActivityMethods {
	private Database db;

	@Before
	public void setup() throws Exception {
		// Clear tables
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
//			conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/puss1302?user=puss1302&password=jks78ww2");
			conn = DriverManager.getConnection("jdbc:mysql://vm26.cs.lth.se/puss1302?user=puss1302&password=jks78ww2");
			Statement stmt = conn.createStatement();
		    stmt.executeUpdate("TRUNCATE TABLE project_groups"); 
		    stmt.executeUpdate("TRUNCATE TABLE users"); 
		    stmt.executeUpdate("TRUNCATE TABLE time_reports"); 
		    stmt.executeUpdate("TRUNCATE TABLE activities");
		    stmt.executeUpdate("insert into users (username, password, role) values('admin', 'adminp', 1)");
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		db = new Database();
	}

	@After
	public void tearDown() throws Exception {
		db.closeConnection();
	}

	@Test
	public void testGetActivities() {
		ProjectGroup projectGroup = new ProjectGroup("puss1302", 1, 5, 5000);
		db.addProjectGroup(projectGroup);
		User user = new User("ada10xyz");
		db.addUser(user);
		db.addUserToProjectGroup(user.getId(), projectGroup.getId());
		TimeReport timeReport = new TimeReport(1, user.getId(), projectGroup.getId());
		Activity activity1 = new Activity(Activity.ACTIVITY_NR_LECTURE, Activity.ACTIVITY_TYPE_OTHER, 90, timeReport.getId());
		Activity activity2 = new Activity(Activity.ACTIVITY_NR_EXERCISE, Activity.ACTIVITY_TYPE_OTHER, 100, timeReport.getId());
		Activity activity3 = new Activity(Activity.ACTIVITY_NR_SDP, Activity.ACTIVITY_TYPE_DEVELOPMENT, 200, timeReport.getId());
		ArrayList<Activity> activities = new ArrayList<Activity>();
		activities.add(activity1);
		activities.add(activity2);
		activities.add(activity3);
		db.addTimeReport(timeReport, activities);
		ArrayList<Activity> resultList = db.getActivities(timeReport.getId());
		assertEquals(3, resultList.size());
		assertEquals(activity1, resultList.get(0));
		assertEquals(activity2, resultList.get(1));
		assertEquals(activity3, resultList.get(2));
	}

	@Test
	public void testGetActivity() {
		ProjectGroup projectGroup = new ProjectGroup("puss1302", 1, 5, 5000);
		db.addProjectGroup(projectGroup);
		User user = new User("ada10xyz");
		db.addUser(user);
		db.addUserToProjectGroup(user.getId(), projectGroup.getId());
		TimeReport timeReport = new TimeReport(1, user.getId(), projectGroup.getId());
		Activity activity = new Activity(Activity.ACTIVITY_NR_LECTURE, Activity.ACTIVITY_TYPE_OTHER, 90, timeReport.getId());
		ArrayList<Activity> activities = new ArrayList<Activity>();
		activities.add(activity);
		db.addTimeReport(timeReport, activities);
		assertEquals(activity, db.getActivity(activity.getId()));
	}

}
