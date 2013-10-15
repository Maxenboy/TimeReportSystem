package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
	private Connection conn;

	public Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:8889/puss1302?user=puss1302&password=jks78ww2");
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

	// User-metoder
	public User loginUser(String username, String password) {

		// select * FROM users WHERE username = " + username
		return null;
	}

	public boolean addUser(User user) {
		boolean resultOK = true;
		try {
			String insertTableSQL = "INSERT INTO users"
					+ "(username, password) VALUES"
					+ "(?,?)";
			PreparedStatement preparedStatement = conn
					.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			String getTableSQL = "SELECT * FROM users WHERE username = ? LIMIT 1";
			preparedStatement = conn.prepareStatement(getTableSQL);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
			while (res.next()) {
				user.setId(res.getInt(1));
				user.setActive(res.getBoolean(4));
				user.setRole(res.getInt(5));
				user.setProjectGroup(res.getInt(6));
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			// System.err.println(ex);
			resultOK = false;
		} 
		return resultOK;
	}

	public boolean activateUser(int userId) {
		return false;
	}

	public boolean deactivateUser(int userId) {
		return false;

	}

	public boolean setUserRoles(HashMap<Integer, Integer> roles) {
		return false;

	}

	public ArrayList<User> getUsers() {
		return null;

	}

	public ArrayList<User> getUsers(int projectGroupId) {
		return null;

	}

	public User getUser(int userId) {
		return null;

	}

	public User getUser(String username) {
		return null;

	}
	
	//ProjectGroup-metoder

	public boolean activateProjectGroup(int projectGroupId) {
		return false;

	}

	public boolean deactivateProjectGroup(int projectGroupId) {
		return false;

	}

	public boolean addProjectGroup(ProjectGroup projectGroup) {
		boolean resultOK = true;
		try {
			String insertTableSQL = "INSERT INTO project_groups"
					+ "(project_name, active, start_week, end_week, estimated_time) VALUES"
					+ "(?,?,?,?,?)";
			PreparedStatement preparedStatement = conn
					.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, projectGroup.getProjectName());
			preparedStatement.setBoolean(2, projectGroup.isActive());
			preparedStatement.setInt(3, projectGroup.getStartWeek());
			preparedStatement.setInt(4, projectGroup.getEndWeek());
			preparedStatement.setInt(5, projectGroup.getEstimatedTime());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			String getTableSQL = "SELECT id FROM project_groups WHERE project_name = ? LIMIT 1";
			preparedStatement = conn.prepareStatement(getTableSQL);
			preparedStatement.setString(1, projectGroup.getProjectName());
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
			while (res.next()) {
				projectGroup.setId(res.getInt(1));
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			// System.err.println(ex);
			resultOK = false;
		}
		return resultOK;
	}

	public boolean addUserToProjectGroup(int userId, int projectGroupId) {
		return false;

	}

	public boolean removeUserFromProjectGroup(int userId, int projectGroupId) {
		return false;

	}

	public ProjectGroup getProjectGroup(int projectGroupId) {
		return null;

	}

	public ArrayList<ProjectGroup> getProjectGroups() {
		return null;

	}

	// TimeReport-metoder
	public ArrayList<TimeReport> getTimeReportsForProjectGroupId(
			int projectGroupId) {
		return null;

	}

	public ArrayList<TimeReport> getSignedTimeReports(int projectGroupId) {
		return null;

	}

	public boolean signTimeReports(ArrayList<TimeReport> timeReports) {
		return false;

	}

	public boolean unsignTimeReports(ArrayList<TimeReport> timeReports) {
		return false;

	}

	public boolean removeTimeReport(int timeReportId) {
		return false;

	}

	public boolean updateTimeReport(TimeReport timeReport,
			ArrayList<Activity> activities) {
		return false;

	}

	public ArrayList<TimeReport> getTimeReportsForUserId(int userId) {
		return null;

	}

	public boolean addTimeReport(TimeReport timeReport,
			ArrayList<Activity> activities) {
		return false;

	}

	public TimeReport getTimeReport(int timeReportId) {
		return null;

	}

	// Activity-metoder
	public ArrayList<Activity> getActivities(int timeReportId) {
		return null;

	}

	public Activity getActivity(int activityId) {
		return null;

	}

	// Statistics-metoder
	public HashMap<String, ArrayList<String>> getStatisticsFilter(
			int projectGroupId) {
		return null;

	}

	public HashMap<String, Integer> getTimePerWeek(int projectGroupId) {
		return null;

	}

	// public static void main(String[] args) {
	// Database db = new Database();
	// ProjectGroup pg = new ProjectGroup("TestName3", 0, 7, 12);
	// db.addProjectGroup(pg);
	// System.out.println("main " + pg.getId());
	// }
}
