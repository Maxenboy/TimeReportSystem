package testDatabase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

public class TestTimeReportMethods {
	private Database db;

	@Before
	public void setup() throws Exception {
		Connection conn = null;
		// Clear tables
		try {
			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/puss1302?user=puss1302&password=jks78ww2");
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
	public void testGetTimeReportsForProjectGroupId() {
		ProjectGroup projectGroup = new ProjectGroup("puss1302", 1, 10, 8000);
		db.addProjectGroup(projectGroup);
		User user1 = new User("ada10xyz");
		User user2 = new User("ada10yxz");
		User user3 = new User("ada10yzx");
		db.addUser(user1);
		db.addUserToProjectGroup(user1.getId(), projectGroup.getId());
		db.addUser(user2);
		db.addUserToProjectGroup(user2.getId(), projectGroup.getId());
		db.addUser(user3);
		db.addUserToProjectGroup(user3.getId(), projectGroup.getId());
		TimeReport timeReport1 = new TimeReport(1, user1.getId(), projectGroup.getId());
		TimeReport timeReport2 = new TimeReport(1, user2.getId(), projectGroup.getId());
		TimeReport timeReport3 = new TimeReport(1, user3.getId(), projectGroup.getId());
		db.addTimeReport(timeReport1, new ArrayList<Activity>());
		db.addTimeReport(timeReport2, new ArrayList<Activity>());
		db.addTimeReport(timeReport3, new ArrayList<Activity>());
		ArrayList<TimeReport> allTimeReports = new ArrayList<TimeReport>();
		allTimeReports.add(timeReport1);
		allTimeReports.add(timeReport2);
		allTimeReports.add(timeReport3);
		ArrayList<TimeReport> signedTimeReports = new ArrayList<TimeReport>();
		signedTimeReports.add(timeReport3);
		db.signTimeReports(signedTimeReports);
		assertEquals(allTimeReports, db.getTimeReportsForProjectGroupId(projectGroup.getId()));
	}
	
	@Test
	public void testGetUnsignedTimeReports() {
		ProjectGroup projectGroup = new ProjectGroup("puss1302", 1, 10, 8000);
		db.addProjectGroup(projectGroup);
		User user1 = new User("ada10xyz");
		User user2 = new User("ada10yxz");
		User user3 = new User("ada10yzx");
		db.addUser(user1);
		db.addUserToProjectGroup(user1.getId(), projectGroup.getId());
		db.addUser(user2);
		db.addUserToProjectGroup(user2.getId(), projectGroup.getId());
		db.addUser(user3);
		db.addUserToProjectGroup(user3.getId(), projectGroup.getId());
		TimeReport timeReport1 = new TimeReport(1, user1.getId(), projectGroup.getId());
		TimeReport timeReport2 = new TimeReport(1, user2.getId(), projectGroup.getId());
		TimeReport timeReport3 = new TimeReport(1, user3.getId(), projectGroup.getId());
		db.addTimeReport(timeReport1, new ArrayList<Activity>());
		db.addTimeReport(timeReport2, new ArrayList<Activity>());
		db.addTimeReport(timeReport3, new ArrayList<Activity>());
		ArrayList<TimeReport> timeReports = new ArrayList<TimeReport>();
		timeReports.add(timeReport1);
		timeReports.add(timeReport2);
		timeReports.add(timeReport3);
		assertEquals(timeReports, db.getUnsignedTimeReports(projectGroup.getId()));
	}

	@Test
	public void testGetSignedTimeReports() {
		ProjectGroup projectGroup = new ProjectGroup("puss1302", 1, 10, 8000);
		db.addProjectGroup(projectGroup);
		User user1 = new User("ada10xyz");
		User user2 = new User("ada10yxz");
		User user3 = new User("ada10yzx");
		db.addUser(user1);
		db.addUserToProjectGroup(user1.getId(), projectGroup.getId());
		db.addUser(user2);
		db.addUserToProjectGroup(user2.getId(), projectGroup.getId());
		db.addUser(user3);
		db.addUserToProjectGroup(user3.getId(), projectGroup.getId());
		TimeReport timeReport1 = new TimeReport(1, user1.getId(), projectGroup.getId());
		TimeReport timeReport2 = new TimeReport(1, user2.getId(), projectGroup.getId());
		TimeReport timeReport3 = new TimeReport(1, user3.getId(), projectGroup.getId());
		db.addTimeReport(timeReport1, new ArrayList<Activity>());
		db.addTimeReport(timeReport2, new ArrayList<Activity>());
		db.addTimeReport(timeReport3, new ArrayList<Activity>());
		ArrayList<TimeReport> timeReports = new ArrayList<TimeReport>();
		timeReports.add(timeReport1);
		timeReports.add(timeReport2);
		db.signTimeReports(timeReports);
		assertEquals(timeReports, db.getSignedTimeReports(projectGroup.getId()));
	}
	
	@Test
	public void testGetSignedTimeReportsEmpty() {
		ProjectGroup projectGroup = new ProjectGroup("puss1302", 1, 10, 8000);
		db.addProjectGroup(projectGroup);
		assertEquals(0, db.getSignedTimeReports(projectGroup.getId()).size());
	}
	
	@Test
	public void testSignTimeReports() {
		ProjectGroup projectGroup = new ProjectGroup("puss1302", 1, 10, 8000);
		db.addProjectGroup(projectGroup);
		User user = new User("ada10xyz");
		db.addUser(user);
		db.addUserToProjectGroup(user.getId(), projectGroup.getId());
		TimeReport timeReport1 = new TimeReport(1, user.getId(), projectGroup.getId());
		TimeReport timeReport2 = new TimeReport(2, user.getId(), projectGroup.getId());
		TimeReport timeReport3 = new TimeReport(3, user.getId(), projectGroup.getId());
		ArrayList<Activity> activities = new ArrayList<Activity>();
		db.addTimeReport(timeReport1, activities);
		db.addTimeReport(timeReport2, activities);
		db.addTimeReport(timeReport3, activities);
		ArrayList<TimeReport> timeReports = new ArrayList<TimeReport>();
		timeReports.add(timeReport1);
		timeReports.add(timeReport2);
		timeReports.add(timeReport3);
		db.signTimeReports(timeReports);
		assertTrue(timeReport1.isSigned());
		assertTrue(timeReport2.isSigned());
		assertTrue(timeReport3.isSigned());
	}

	@Test
	public void testUnsignTimeReports() {
		ProjectGroup projectGroup = new ProjectGroup("puss1302", 1, 10, 8000);
		db.addProjectGroup(projectGroup);
		User user = new User("ada10xyz");
		db.addUser(user);
		db.addUserToProjectGroup(user.getId(), projectGroup.getId());
		TimeReport timeReport1 = new TimeReport(1, user.getId(), projectGroup.getId());
		TimeReport timeReport2 = new TimeReport(2, user.getId(), projectGroup.getId());
		TimeReport timeReport3 = new TimeReport(3, user.getId(), projectGroup.getId());
		ArrayList<Activity> activities = new ArrayList<Activity>();
		db.addTimeReport(timeReport1, activities);
		db.addTimeReport(timeReport2, activities);
		db.addTimeReport(timeReport3, activities);
		ArrayList<TimeReport> timeReports = new ArrayList<TimeReport>();
		timeReports.add(timeReport1);
		timeReports.add(timeReport2);
		timeReports.add(timeReport3);
		db.signTimeReports(timeReports);
		db.unsignTimeReports(timeReports);
		assertFalse(timeReport1.isSigned());
		assertFalse(timeReport2.isSigned());
		assertFalse(timeReport3.isSigned());
	}

	@Test
	public void testRemoveTimeReport() {
		ProjectGroup projectGroup = new ProjectGroup("puss1302", 1, 10, 8000);
		db.addProjectGroup(projectGroup);
		User user = new User("ada10xyz");
		db.addUser(user);
		db.addUserToProjectGroup(user.getId(), projectGroup.getId());
		TimeReport timeReport = new TimeReport(1, user.getId(), projectGroup.getId());
		Activity activity1 = new Activity(Activity.ACTIVITY_NR_COMPUTER_EXERCISE, Activity.ACTIVITY_TYPE_OTHER, 90, timeReport.getId());
		ArrayList<Activity> activities = new ArrayList<Activity>();
		activities.add(activity1);
		db.addTimeReport(timeReport, activities);
		assertTrue(db.removeTimeReport(timeReport.getId()));
		assertNull(db.getTimeReport(timeReport.getId()));
	}
	
	@Test
	public void testRemoveTimeReportFail() {
		assertFalse(db.removeTimeReport(1));
	}

	@Test
	public void testUpdateTimeReport() {
		ProjectGroup projectGroup = new ProjectGroup("puss1302", 1, 10, 8000);
		db.addProjectGroup(projectGroup);
		User user = new User("ada10xyz");
		db.addUser(user);
		db.addUserToProjectGroup(user.getId(), projectGroup.getId());
		TimeReport timeReport = new TimeReport(1, user.getId(), projectGroup.getId());
		Activity activity1 = new Activity(Activity.ACTIVITY_NR_COMPUTER_EXERCISE, Activity.ACTIVITY_TYPE_OTHER, 90, timeReport.getId());
		ArrayList<Activity> activities = new ArrayList<Activity>();
		activities.add(activity1);
		db.addTimeReport(timeReport, activities);
		
		timeReport.setSigned(true);
		Activity activity2 = new Activity(Activity.ACTIVITY_NR_EXERCISE, Activity.ACTIVITY_TYPE_OTHER, 90, timeReport.getId());
		activities = new ArrayList<Activity>();
		activities.add(activity2);
		assertTrue(db.updateTimeReport(timeReport, activities));
		assertEquals(timeReport, db.getTimeReport(timeReport.getId()));
		assertEquals(activities, db.getActivities(timeReport.getId()));
	}

	@Test
	public void testUpdateTimeReportFail() {
		TimeReport timeReport = new TimeReport(1, 2, 2);
		assertFalse(db.updateTimeReport(timeReport, new ArrayList<Activity>()));
	}
	
	@Test
	public void testGetTimeReportsForUserId() {
		ProjectGroup projectGroup = new ProjectGroup("puss1302", 1, 10, 8000);
		db.addProjectGroup(projectGroup);
		User user = new User("ada10xyz");
		db.addUser(user);
		db.addUserToProjectGroup(user.getId(), projectGroup.getId());
		
		TimeReport timeReport1 = new TimeReport(1, user.getId(), projectGroup.getId());
		TimeReport timeReport2 = new TimeReport(2, user.getId(), projectGroup.getId());
		TimeReport timeReport3 = new TimeReport(3, user.getId(), projectGroup.getId());
		db.addTimeReport(timeReport1, new ArrayList<Activity>());
		db.addTimeReport(timeReport2, new ArrayList<Activity>());
		db.addTimeReport(timeReport3, new ArrayList<Activity>());
		ArrayList<TimeReport> timeReports = db.getTimeReportsForUserId(user.getId());
		assertEquals(3, timeReports.size());
		assertEquals(timeReport1, timeReports.get(0));
		assertEquals(timeReport2, timeReports.get(1));
		assertEquals(timeReport3, timeReports.get(2));
	}
	
	@Test
	public void testGetTimeReportsForUserIdEmpty() {
		assertEquals(0, db.getTimeReportsForUserId(2).size());
	}

	@Test
	public void testAddTimeReport() {
		ProjectGroup projectGroup = new ProjectGroup("puss1302", 1, 10, 8000);
		db.addProjectGroup(projectGroup);
		User user = new User("ada10xyz");
		db.addUser(user);
		db.addUserToProjectGroup(user.getId(), projectGroup.getId());
		
		TimeReport timeReport = new TimeReport(1, user.getId(), projectGroup.getId());
		assertTrue(db.addTimeReport(timeReport, new ArrayList<Activity>()));
	}
	
	@Test
	public void testAddTimeReportFail() {
		TimeReport timeReport = new TimeReport(1, 2, 2);
		assertTrue(db.addTimeReport(timeReport, new ArrayList<Activity>()));
	}

	@Test
	public void testGetTimeReport() {
		ProjectGroup projectGroup = new ProjectGroup("puss1302", 1, 10, 8000);
		db.addProjectGroup(projectGroup);
		User user = new User("ada10xyz");
		db.addUser(user);
		db.addUserToProjectGroup(user.getId(), projectGroup.getId());
		
		TimeReport timeReport = new TimeReport(1, user.getId(), projectGroup.getId());
		db.addTimeReport(timeReport, new ArrayList<Activity>());
		assertEquals(timeReport, db.getTimeReport(timeReport.getId()));
	}
	
	@Test
	public void testGetTimeReportFail() {
		assertNull(db.getTimeReport(1));
	}

}
