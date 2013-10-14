package database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import java.sql.Connection;

public class Database {
	private Connection conn;

	public Database() {
		try {
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:8888/puss1302?"
							+ "user=root&password=root");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public User loginUser(String username, String password) {
		return null;
	}

	public boolean addUser(User user) {
		return false;

	}

	public boolean activateUser(int userId) {
		return false;

	}

	public boolean deactivateUser(int userId) {
		return false;

	}

	public boolean activateProjectGroup(int projectGroupId) {
		return false;

	}

	public boolean deactivateProjectGroup(int projectGroupId) {
		return false;

	}

	public boolean addProjectGroup(ProjectGroup projectGroup) {
		return false;

	}

	public boolean addUserToProjectGroup(int userId, int projectGroupId) {
		return false;

	}

	public boolean removeUserFromProjectGroup(int userId, int projectGroupId) {
		return false;

	}

	public boolean setUserRoles(HashMap<Integer, Integer> roles) {
		return false;

	}

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

	public ArrayList<User> getUsers() {
		return null;

	}

	public ArrayList<User> getUsers(int projectGroupId) {
		return null;

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

	public ArrayList<Activity> getActivities(int timeReportId) {
		return null;

	}

	public User getUser(int userId) {
		return null;

	}

	public User getUser(String username) {
		return null;

	}

	public ProjectGroup getProjectGroup(int projectGroupId) {
		return null;

	}

	public TimeReport getTimeReport(int timeReportId) {
		return null;

	}

	public Activity getActivity(int activityId) {
		return null;

	}

	public ArrayList<ProjectGroup> getProjectGroups() {
		return null;

	}

	public HashMap<String, ArrayList<String>> getStatisticsFilter(
			int projectGroupId) {
		return null;

	}

	public HashMap<String, Integer> getTimePerWeek(int projectGroupId) {
		return null;

	}
}
