package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

public class Database {
	private Connection conn;

	public Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager
//					.getConnection("jdbc:mysql://localhost:3306/puss1302?user=puss1302&password=jks78ww2");
			 conn =
			 DriverManager.getConnection("jdbc:mysql://vm26.cs.lth.se/ada08ml1?"
			 + "user=ada08ml1&password=7z8vlx7o");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// User-metoder
	/**
	 * Logins a user with the specified username and password. 
	 * @param username
	 * @param password
	 * @return null if username/password is wrong, the User-object otherwise
	 */
	public User loginUser(String username, String password) {
		User u = null;
		try {
			String getTableSQL = "SELECT * FROM users WHERE username = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setString(1, username);
			ResultSet res = preparedStatement.executeQuery();
			while (res.next()) {
				if (res.getString(3).equals(password)) {
					u = new User(res.getInt(1), res.getString(2),
							res.getString(3), res.getBoolean(4), res.getInt(5),
							res.getInt(6));
				}
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			// ex.printStackTrace();
			// System.err.println(ex);
		}
		return u;
	}

	/**
	 * Adds the user.
	 * @param user
	 * @return true if successful, false otherwise
	 */
	public boolean addUser(User user) {
		boolean resultOK = true;
		try {
			String insertTableSQL = "INSERT INTO users"
					+ "(username, password) VALUES" + "(?,?)";
			PreparedStatement preparedStatement = conn
					.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			String getTableSQL = "SELECT * FROM users WHERE username = ? LIMIT 1";
			preparedStatement = conn.prepareStatement(getTableSQL);
			preparedStatement.setString(1, user.getUsername());
			ResultSet res = preparedStatement.executeQuery();
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

	/**
	 * Gets the user with the username
	 * @param username
	 * @return User-object if successful, null otherwise
	 */
	public User getUser(String username) {
		User u = null;
		try {
			String getTableSQL = "SELECT * FROM users WHERE username = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setString(1, username);
			ResultSet res = preparedStatement.executeQuery();
			while (res.next()) {
				u = new User(res.getInt(1), res.getString(2), res.getString(3),
						res.getBoolean(4), res.getInt(5), res.getInt(6));
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			// System.err.println(ex);
		}
		return u;

	}

	/**
	 * Gets all users
	 * @return ArrayList with all users
	 */
	public ArrayList<User> getUsers() {
		ArrayList<User> list = new ArrayList<User>();
		try {
			String getUsersSQL = "SELECT * FROM users";
			PreparedStatement preparedStatement = conn.prepareStatement(getUsersSQL);
			ResultSet res = preparedStatement.executeQuery();
			User u = null;
			while (res.next()) {
				u = new User(res.getInt(1), res.getString(2), res.getString(3),
						res.getBoolean(4), res.getInt(5), res.getInt(6));
				list.add(u);
			}
		} catch (SQLException ex) {
			// System.err.println(ex);
		}
		return list;

	}

	/**
	 * Gets all users in a projectGroup with the id projectGroupId
	 * @param projectGroupId
	 * @return ArrayList with all users
	 */
	public ArrayList<User> getUsers(int projectGroupId) {
		ArrayList<User> list = new ArrayList<User>();
		try {
			String getUsersSQL = "SELECT * FROM users WHERE project_group_id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(getUsersSQL);
			preparedStatement.setInt(1, projectGroupId);
			ResultSet res = preparedStatement.executeQuery();
			User u = null;
			while (res.next()) {
				u = new User(res.getInt(1), res.getString(2), res.getString(3),
						res.getBoolean(4), res.getInt(5), res.getInt(6));
				list.add(u);
			}
		} catch (SQLException ex) {
			// System.err.println(ex);
		}
		return list;
	}

	/**
	 * Get a user by userId
	 * @param userId
	 * @return User-object if successful, null otherwise
	 */
	public User getUser(int userId) {
		User u = null;
		try {
			String getTableSQL = "SELECT * FROM users WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, userId);
			ResultSet res = preparedStatement.executeQuery();
			res.next();
			u = new User(res.getInt(1), res.getString(2), res.getString(3),
					res.getBoolean(4), res.getInt(5), res.getInt(6));
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			// System.err.println(ex);
		}
		return u;

	}

	/**
	 * Activates the user with the id userId
	 * @param userId
	 * @return true if successful, false otherwise
	 */
	public boolean activateUser(int userId) {
		return activateUserHelpMethod(userId, 1);
	}

	/**
	 * Deactivates the user with the id userId
	 * @param userId
	 * @return true if successful, false otherwise
	 */
	public boolean deactivateUser(int userId) {
		return activateUserHelpMethod(userId, 0);
	}

	private boolean activateUserHelpMethod(int userId, int active) {
		boolean resultOk = true;
		try {
			String checkIfUserIdExistsSQL = "SELECT active FROM users WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(checkIfUserIdExistsSQL);
			preparedStatement.setInt(1, userId);
			ResultSet res = preparedStatement.executeQuery();
			if (res.next()) {
				String insertIntoSQL = "UPDATE users SET active=? WHERE id=?";
				preparedStatement = conn.prepareStatement(insertIntoSQL);
				preparedStatement.setInt(1, active);
				preparedStatement.setInt(2, userId);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			} else {
				resultOk = false;
				preparedStatement.close();
			}

		} catch (SQLException ex) {
			// System.err.println(ex);
			resultOk = false;
		}
		return resultOk;
	}

	/**
	 * Set the roles for the userIds specified as keys and the values for that key is the role to set for the user
	 * @param roles
	 * @return true if successful, false otherwise
	 */
	public boolean setUserRoles(HashMap<Integer, Integer> roles) {
		boolean resultOk = true;
		for (Map.Entry<Integer, Integer> entry : roles.entrySet()) {
			resultOk = setUserRole(entry.getKey(), entry.getValue());
			if (!resultOk) {
				break;
			}
		}
		return resultOk;
	}

	private boolean setUserRole(int userId, int role) {
		boolean resultOk = true;
		try {
			String checkIfUserIdExistsSQL = "SELECT active FROM users WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(checkIfUserIdExistsSQL);
			preparedStatement.setInt(1, userId);
			ResultSet res = preparedStatement.executeQuery();
			if (res.next()) {
				String insertIntoSQL = "UPDATE users SET role=? WHERE id=?";
				preparedStatement = conn.prepareStatement(insertIntoSQL);
				preparedStatement.setInt(1, role);
				preparedStatement.setInt(2, userId);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			} else {
				resultOk = false;
				preparedStatement.close();
			}

		} catch (SQLException ex) {
			// System.err.println(ex);
			resultOk = false;
		}
		return resultOk;
	}

	// ProjectGroup-metoder
	/**
	 * Activates the project group with id projectGroupId
	 * @param projectGroupId
	 * @return true if successful, false otherwise
	 */
	public boolean activateProjectGroup(int projectGroupId) {
		return activateProjectGroupHelpMethod(projectGroupId, 1);
	}

	/**
	 * Deactivates the project group with id projectGroupId
	 * @param projectGroupId
	 * @return true if successful, false otherwise
	 */
	public boolean deactivateProjectGroup(int projectGroupId) {
		return activateProjectGroupHelpMethod(projectGroupId, 0);
	}

	private boolean activateProjectGroupHelpMethod(int projectGroupId,
			int active) {
		boolean resultOk = true;
		try {
			String checkIfUserIdExistsSQL = "SELECT active FROM project_groups WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(checkIfUserIdExistsSQL);
			preparedStatement.setInt(1, projectGroupId);
			ResultSet res = preparedStatement.executeQuery();
			if (res.next()) {
				String insertIntoSQL = "UPDATE project_groups SET active=? WHERE id=?";
				preparedStatement = conn.prepareStatement(insertIntoSQL);
				preparedStatement.setInt(1, active);
				preparedStatement.setInt(2, projectGroupId);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			} else {
				resultOk = false;
				preparedStatement.close();
			}

		} catch (SQLException ex) {
			// System.err.println(ex);
			resultOk = false;
		}
		return resultOk;
	}

	/**
	 * Adds a project group
	 * @param projectGroup
	 * @return true if successful, false otherwise
	 */
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
			ResultSet res = preparedStatement.executeQuery();
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

	/**
	 * Adds a user with id userId to the project group with id projectGroupId
	 * @param userId
	 * @param projectGroupId
	 * @return true if successful, false otherwise
	 */
	public boolean addUserToProjectGroup(int userId, int projectGroupId) {
		boolean resultOk = true;
		try {
			String checkIfProjectGroupIdExistsSQL = "SELECT * FROM project_groups WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(checkIfProjectGroupIdExistsSQL);
			preparedStatement.setInt(1, projectGroupId);
			ResultSet res = preparedStatement.executeQuery();
			if (res.next()) {
				String checkIfUserIdExistsSQL = "SELECT * FROM users WHERE id = ? LIMIT 1";
				preparedStatement = conn
						.prepareStatement(checkIfUserIdExistsSQL);
				preparedStatement.setInt(1, userId);
				ResultSet res2 = preparedStatement.executeQuery();
				if (res2.next()) {
					String insertIntoSQL = "UPDATE users SET project_group_id=? WHERE id=?";
					preparedStatement = conn.prepareStatement(insertIntoSQL);
					preparedStatement.setInt(1, projectGroupId);
					preparedStatement.setInt(2, userId);
					preparedStatement.executeUpdate();
					preparedStatement.close();
				} else {
					resultOk = false;
					preparedStatement.close();
				}
			} else {
				resultOk = false;
				preparedStatement.close();
			}

		} catch (SQLException ex) {
			// System.err.println(ex);
			resultOk = false;
		}
		return resultOk;
	}

	/**
	 * Removes the user with id userId from the project group with id projectGroupId
	 * @param userId
	 * @param projectGroupId
	 * @return true if successful, false otherwise
	 */
	public boolean removeUserFromProjectGroup(int userId, int projectGroupId) {
		boolean resultOk = true;
		try {
			String checkIfProjectGroupIdExistsSQL = "SELECT * FROM project_groups WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(checkIfProjectGroupIdExistsSQL);
			preparedStatement.setInt(1, projectGroupId);
			ResultSet res = preparedStatement.executeQuery();
			if (res.next()) {
				String checkIfUserIdExistsSQL = "SELECT * FROM users WHERE id = ? LIMIT 1";
				preparedStatement = conn
						.prepareStatement(checkIfUserIdExistsSQL);
				preparedStatement.setInt(1, userId);
				ResultSet res2 = preparedStatement.executeQuery();
				if (res2.next()) {
					String insertIntoSQL = "UPDATE users SET project_group_id='0' WHERE id=?";
					preparedStatement = conn.prepareStatement(insertIntoSQL);
					preparedStatement.setInt(1, userId);
					preparedStatement.executeUpdate();
					preparedStatement.close();
				} else {
					resultOk = false;
					preparedStatement.close();
				}
				res2.close();
			} else {
				resultOk = false;
				preparedStatement.close();
			}
			res.close();
		} catch (SQLException ex) {
			// System.err.println(ex);
			resultOk = false;
		}
		return resultOk;
	}

	/**
	 * Get project group with id projectGroupId
	 * @param projectGroupId
	 * @return the ProjectGroup if successful, null otherwise
	 */
	public ProjectGroup getProjectGroup(int projectGroupId) {
		ProjectGroup pg = null;
		try {
			String getTableSQL = "SELECT * FROM project_groups WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, projectGroupId);
			ResultSet res = preparedStatement.executeQuery();
			while (res.next()) {
				pg = new ProjectGroup(res.getInt(1), res.getString(2),
						res.getBoolean(3), res.getInt(4), res.getInt(5),
						res.getInt(6));
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			// System.err.println(ex);
		}
		return pg;
	}

	/**
	 * Get all project groups
	 * @return ArrayList with all project groups
	 */
	public ArrayList<ProjectGroup> getProjectGroups() {
		ArrayList<ProjectGroup> list = new ArrayList<ProjectGroup>();
		try {
			String getTableSQL = "SELECT * FROM project_groups";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			ResultSet res = preparedStatement.executeQuery();
			while (res.next()) {
				list.add(new ProjectGroup(res.getInt(1), res.getString(2), res
						.getBoolean(3), res.getInt(4), res.getInt(5), res
						.getInt(6)));
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			// System.err.println(ex);
		}
		return list;
	}

	// TimeReport-metoder
	/**
	 * Get all time reports for project group with id projectGroupId
	 * @param projectGroupId
	 * @return ArrayList with time reports
	 */
	public ArrayList<TimeReport> getTimeReportsForProjectGroupId(
			int projectGroupId) {
		ArrayList<TimeReport> list = new ArrayList<TimeReport>();
		try {
			String getTableSQL = "SELECT * FROM time_reports WHERE project_group_id = ? ORDER BY id";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, projectGroupId);
			ResultSet res = preparedStatement.executeQuery();
			while (res.next()) {
				list.add(new TimeReport(res.getInt(1), res.getInt(2), res
						.getBoolean(3), res.getInt(4), res.getInt(5)));
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			// System.err.println(ex);
		}
		return list;
	}

	/**
	 * Get all unsigned reports for the project group with id projectGroupId
	 * @param projectGroupId
	 * @return ArrayList with the TimeReports
	 */
	public ArrayList<TimeReport> getUnsignedTimeReports(int projectGroupId) {
		ArrayList<TimeReport> list = new ArrayList<TimeReport>();
		try {
			String getTableSQL = "SELECT * FROM time_reports WHERE project_group_id = ? AND signed = 0 ORDER BY id";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, projectGroupId);
			ResultSet res = preparedStatement.executeQuery();
			while (res.next()) {
				list.add(new TimeReport(res.getInt(1), res.getInt(2), res
						.getBoolean(3), res.getInt(4), res.getInt(5)));
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			// System.err.println(ex);
		}
		return list;
	}

	/**
	 * Get all signed time reports for project group with id projectGroupId
	 * @param projectGroupId
	 * @return ArrayList with the TimeReports
	 */
	public ArrayList<TimeReport> getSignedTimeReports(int projectGroupId) {
		ArrayList<TimeReport> list = new ArrayList<TimeReport>();
		try {
			String getTableSQL = "SELECT * FROM time_reports WHERE project_group_id = ? AND signed = 1 ORDER BY id";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, projectGroupId);
			ResultSet res = preparedStatement.executeQuery();
			while (res.next()) {
				list.add(new TimeReport(res.getInt(1), res.getInt(2), res
						.getBoolean(3), res.getInt(4), res.getInt(5)));
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			// System.err.println(ex);
		}
		return list;
	}

	/**
	 * Sign time reports
	 * @param timeReports
	 * @return true if successful, false otherwise
	 */
	public boolean signTimeReports(ArrayList<TimeReport> timeReports) {
		boolean resultOk = true;
		for (TimeReport tr : timeReports) {
			resultOk = signTimeReportsHelperMethod(tr, true);
			if (!resultOk) {
				break;
			}
		}
		return resultOk;
	}

	private boolean signTimeReportsHelperMethod(TimeReport timeReport,
			boolean signed) {
		boolean resultOk = true;
		try {
			String checkIfTimeReportExists = "SELECT signed FROM time_reports WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(checkIfTimeReportExists);
			preparedStatement.setInt(1, timeReport.getId());
			ResultSet res = preparedStatement.executeQuery();
			if (res.next()) {
				String insertIntoSQL = "UPDATE time_reports SET signed=? WHERE id=?";
				preparedStatement = conn.prepareStatement(insertIntoSQL);
				preparedStatement.setBoolean(1, signed);
				preparedStatement.setInt(2, timeReport.getId());
				preparedStatement.executeUpdate();
				preparedStatement.close();
				timeReport.setSigned(signed);
			} else {
				resultOk = false;
				preparedStatement.close();
			}

		} catch (SQLException ex) {
			// System.err.println(ex);
			resultOk = false;
		}
		return resultOk;
	}

	/**
	 * Unsign timeReports
	 * @param timeReports
	 * @return true if successful, false otherwise
	 */
	public boolean unsignTimeReports(ArrayList<TimeReport> timeReports) {
		boolean resultOk = true;
		for (TimeReport tr : timeReports) {
			resultOk = signTimeReportsHelperMethod(tr, false);
			if (!resultOk) {
				break;
			}
		}
		return resultOk;
	}

	/**
	 * Removes the time report with id timeReportId and the associated activities
	 * @param timeReportId
	 * @return true if successful, false otherwise
	 */
	public boolean removeTimeReport(int timeReportId) {
		boolean resultOk = true;
		try {
			String checkIfTimeReportExists = "SELECT signed FROM time_reports WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(checkIfTimeReportExists);
			preparedStatement.setInt(1, timeReportId);
			ResultSet res = preparedStatement.executeQuery();
			if (res.next()) {
				String removeTimeReportSQL = "DELETE FROM time_reports WHERE id = ?";
				preparedStatement = conn.prepareStatement(removeTimeReportSQL);
				preparedStatement.setInt(1, timeReportId);
				preparedStatement.executeUpdate();
				String removeActivitiesRelatedToTimeReportSQL = "DELETE FROM activities WHERE time_report_id = ?";
				preparedStatement = conn
						.prepareStatement(removeActivitiesRelatedToTimeReportSQL);
				preparedStatement.setInt(1, timeReportId);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			} else {
				resultOk = false;
				preparedStatement.close();
			}

		} catch (SQLException ex) {
			// System.err.println(ex);
			resultOk = false;
		}
		return resultOk;
	}

	/**
	 * Updates the time report with the associating activities
	 * @param timeReport
	 * @param activities
	 * @return true if successful, false otherwise
	 */
	public boolean updateTimeReport(TimeReport timeReport, ArrayList<Activity> activities) {
		if (removeTimeReport(timeReport.getId())) {
			return addTimeReport(timeReport, activities);
		} else {
			return false;
		}
	}

	/**
	 * Get all time reports for the user with id userId
	 * @param userId
	 * @return ArrayList with TimeReports
	 */
	public ArrayList<TimeReport> getTimeReportsForUserId(int userId) {
		ArrayList<TimeReport> list = new ArrayList<TimeReport>();
		try {
			String getTableSQL = "SELECT * FROM time_reports WHERE user_id = ? ORDER BY id";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, userId);
			ResultSet res = preparedStatement.executeQuery();
			while (res.next()) {
				list.add(new TimeReport(res.getInt(1), res.getInt(2), res
						.getBoolean(3), res.getInt(4), res.getInt(5)));
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			// System.err.println(ex);
		}
		return list;
	}

	/**
	 * Adds a new time report and its activities to the database. The time report
	 * and activity ids will be generated and will be changed in the User and 
	 * Activity objects. The time report id of the activities will be set to
	 * the id of the time report.
	 * @param timeReport The time report.
	 * @param activities A list containing the activities.
	 * @return true if the time report and the activities was added successfully,
	 * false otherwise.
	 */
	public boolean addTimeReport(TimeReport timeReport,
			ArrayList<Activity> activities) {
		if (timeReport.getId() != 0) {
//			System.err
//					.println("addTimeReport: TimeReport id not 0. Adding it anyways.");
		}
		try {
			String insertTableSQL = "INSERT INTO time_reports (week, signed, user_id, project_group_id) VALUES (?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(
					insertTableSQL, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, timeReport.getWeek());
			preparedStatement.setBoolean(2, timeReport.isSigned());
			preparedStatement.setInt(3, timeReport.getUserId());
			preparedStatement.setInt(4, timeReport.getProjectGroupId());
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				timeReport.setId(rs.getInt(1));
			} else {
//				System.err
//						.println("addTimeReport: TimeReport: could not get id from database");
				return false;
			}
			rs.close();
			preparedStatement.close();

			for (Activity activity : activities) {
				if (activity.getId() != 0) {
//					System.err
//							.println("addTimeReport: Activity id not 0. Adding it anyways.");
				}
				String insertActivityTableSQL = "INSERT INTO activities (activity_nr, activity_type, time, time_report_id) VALUES (?,?,?,?)";
				PreparedStatement preparedStatementActivity = conn
						.prepareStatement(insertActivityTableSQL,
								Statement.RETURN_GENERATED_KEYS);
				preparedStatementActivity.setInt(1, activity.getActivityNr());
				preparedStatementActivity.setString(2,
						activity.getActivityType());
				preparedStatementActivity.setInt(3, activity.getTime());
				if (activity.getTimeReportId() != timeReport.getId()
						&& activity.getTimeReportId() != 0) {
//					System.err
//							.println("addTimeReport: The time report id of the Activity does not match that of the time report id. The id from the time report will be used.");
				}
				activity.setTimeReportId(timeReport.getId());
				preparedStatementActivity.setInt(4, timeReport.getId());
				preparedStatementActivity.executeUpdate();
				ResultSet rsActivity = preparedStatementActivity
						.getGeneratedKeys();
				if (rsActivity.next()) {
					activity.setId(rsActivity.getInt(1));
				} else {
//					System.err
//							.println("addTimeReport: Activity: could not get id from database");
					rsActivity.close();
					preparedStatementActivity.close();
					return false;
				}
				rsActivity.close();
				preparedStatementActivity.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Get the time report with id timeReportId
	 * @param timeReportId
	 * @return the TimeReport, null if not found
	 */
	public TimeReport getTimeReport(int timeReportId) {
		TimeReport report = null;
		try {
			String getTableSQL = "SELECT * FROM time_reports WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, timeReportId);
			ResultSet res = preparedStatement.executeQuery();
			while (res.next()) {
				report = new TimeReport(res.getInt(1), res.getInt(2),
						res.getBoolean(3), res.getInt(4), res.getInt(5));
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
//			System.err.println(ex);
		}
		return report;
	}

	// Activity-metoder
	/**
	 * Get the activities associated with the time report with id timeReportId
	 * @param timeReportId
	 * @return ArrayList with activities.
	 */
	public ArrayList<Activity> getActivities(int timeReportId) {
		ArrayList<Activity> list = new ArrayList<Activity>();
		try {
			String getUsersSQL = "SELECT * FROM activities where time_report_id = ? order by id";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getUsersSQL);
			preparedStatement.setInt(1, timeReportId);
			ResultSet res = preparedStatement.executeQuery();
			Activity activity = null;
			while (res.next()) {
				activity = new Activity(res.getInt(1), res.getInt(2),
						res.getString(3), res.getInt(4), res.getInt(5));
				list.add(activity);
			}
		} catch (SQLException ex) {
			// System.err.println(ex);
		}
		return list;

	}

	/**
	 * Get activity with id activityId
	 * @param activityId
	 * @return the Activity
	 */
	public Activity getActivity(int activityId) {
		Activity activity;
		try {
			String getTableSQL = "SELECT * FROM activities WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, activityId);
			ResultSet res = preparedStatement.executeQuery();
			res.next();
			activity = new Activity(res.getInt(1), res.getInt(2),
					res.getString(3), res.getInt(4), res.getInt(5));
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			// System.err.println(ex);
			return null;
		}
		return activity;
	}

	/**
	 * Gets all unique roles, users, activities and weeks. 
	 * @param projectGroupId
	 * @return HashMap with keys "user", "role", "activity" and "week". Values are Arraylists.
	 */
	public HashMap<String, ArrayList<String>> getStatisticsFilter(int projectGroupId) {
		ArrayList<String> users = new ArrayList<String>();
		ArrayList<String> roles = new ArrayList<String>();
		ArrayList<String> activities = new ArrayList<String>();
		ArrayList<String> weeks = new ArrayList<String>();
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		ArrayList<User> list = getUsers(projectGroupId);
		for(User u : list) {
			users.add(u.getUsername());
		}
		
		try {
			String getTableSQL = "SELECT DISTINCT(role) FROM users WHERE project_group_id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, projectGroupId);
			ResultSet res = preparedStatement.executeQuery();
			while(res.next()) {
				roles.add(Integer.toString(res.getInt("role")));
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			// System.err.println(ex);
		}
		
		try {
			ArrayList<String> activityNr = new ArrayList<String>();
			for(TimeReport tr : getTimeReportsForProjectGroupId(projectGroupId)) {
				String getTableSQL = "SELECT DISTINCT(activity_nr) FROM activities WHERE time_report_id = ?";
				PreparedStatement preparedStatement = conn.prepareStatement(getTableSQL);
				preparedStatement.setInt(1, tr.getId());
				ResultSet res = preparedStatement.executeQuery();
				while(res.next()) {
					if(!activityNr.contains(Integer.toString(res.getInt("activity_nr")))) {
						activityNr.add(Integer.toString(res.getInt("activity_nr")));
					}
				}
				res.close();
				preparedStatement.close();
			}
			for(String s : activityNr) {
				activities.add(s);
			}
		} catch (SQLException ex) {
			// System.err.println(ex);
		}
		
		try {
			String getTableSQL = "SELECT DISTINCT(week) FROM time_reports WHERE project_group_id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, projectGroupId);
			ResultSet res = preparedStatement.executeQuery();
			while(res.next()) {
				weeks.add(Integer.toString(res.getInt("week")));
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			// System.err.println(ex);
		}
		
		map.put("user", users);
		map.put("role", roles);
		map.put("activity", activities);
		map.put("week", weeks);
		
		return map;
	}
	
	/**
	 * Gets statistics by fetching the activities that matches the given 
	 * parameters. The parameters act as a filter for the statistics. If a 
	 * parameter is null or an empty list means that this filter is not set
	 * and all values for this property will be accepted during the matching.
	 * @param projectGroupId The project group id.
	 * @param usernames The list of usernames to collect statistics for.
	 * @param roles The list of roles to collect statistics for.
	 * @param activities The list of activities to collect statistics for.
	 * @param weeks The list of weeks to collect statistics for.
	 * @return HashMap with keys "user", "role", "activity", "week" and "time" 
	 * and ArrayList as value. The HashMap forms a table where the keys are
	 * the columns and all the ArrayLists at equal indices are the rows. 
	 * That means that all the values for a specific index in the ArrayLists 
	 * corresponds to a matching activity for the given filter. 
	 */
	public HashMap<String, ArrayList<String>> getStatistics(int projectGroupId,
			ArrayList<String> usernames, ArrayList<Integer> roles,
			ArrayList<Integer> activities, ArrayList<Integer> weeks) {
		
		HashMap<String, ArrayList<String>> stats = new HashMap<String, ArrayList<String>>();
		stats.put("username", new ArrayList<String>());
		stats.put("role", new ArrayList<String>());
		stats.put("activity_nr", new ArrayList<String>());
		stats.put("week", new ArrayList<String>());
		stats.put("time", new ArrayList<String>());
		
		/*
		 * Basically what we want to run is:
		 * SELECT * FROM activities WHERE activity_nr IN (?,...) AND time_report_id IN (SELECT id FROM time_reports WHERE project_group_id=? AND week IN (?,...) AND user_id IN (SELECT id FROM users WHERE username IN (?,...) AND role IN (?,...)));
		 * But since the lists can be null and empty we also have to take care of those cases.
		 */
		try {
			// Users
			StringBuilder usersSql = new StringBuilder();
			PreparedStatement usersStmt;
			if ((usernames == null || usernames.isEmpty()) && (roles == null || roles.isEmpty())) {
			    usersStmt = conn.prepareStatement("SELECT id FROM users");
			} else if (usernames == null || usernames.isEmpty()) {
				usersSql.append("SELECT id FROM users WHERE role IN ").append(getQuestionMarksForList(roles));	
				usersStmt = conn.prepareStatement(usersSql.toString());
				for (int i = 0; i < roles.size(); i++) {
					usersStmt.setInt(i + 1, roles.get(i));
				}
			} else if (roles == null || roles.isEmpty()) {
			    usersSql.append("SELECT id FROM users WHERE username IN ").append(getQuestionMarksForList(usernames));
			    usersStmt = conn.prepareStatement(usersSql.toString());
				for (int i = 0; i < usernames.size(); i++) {
					usersStmt.setString(i + 1, usernames.get(i));
				}
			} else {
			    usersSql.append("SELECT id FROM users WHERE username IN ").append(getQuestionMarksForList(usernames)).append(" AND role IN ").append(getQuestionMarksForList(roles));
			    usersStmt = conn.prepareStatement(usersSql.toString());
				for (int i = 0; i < usernames.size(); i++) {
					usersStmt.setString(i + 1, usernames.get(i));
				}
				for (int j = 0; j < roles.size(); j++) {
					usersStmt.setInt(j + usernames.size() + 1, roles.get(j));
				}
			}
			ResultSet usersRes = usersStmt.executeQuery();
			ArrayList<Integer> usersList = new ArrayList<Integer>();
			while (usersRes.next()) {
				usersList.add(usersRes.getInt("id"));
			}
			usersStmt.close();
			if (usersList.size() == 0) {
//				System.err.println("getStatistics: No matching users.");
				return stats;
			}

			// Time reports
			StringBuilder timeReportsSql = new StringBuilder();
			PreparedStatement timeReportsStmt;
			if (weeks == null || weeks.isEmpty()) {
			    timeReportsSql.append("SELECT id FROM time_reports WHERE project_group_id=? AND user_id IN ").append(getQuestionMarksForList(usersList));
			    timeReportsStmt = conn.prepareStatement(timeReportsSql.toString());
			    timeReportsStmt.setInt(1, projectGroupId);
				for (int i = 0; i < usersList.size(); i++) {
					timeReportsStmt.setInt(i + 2, usersList.get(i));
				}
			} else {
			    timeReportsSql.append("SELECT id FROM time_reports WHERE project_group_id=? AND week IN ").append(getQuestionMarksForList(weeks)).append(" AND user_id IN ").append(getQuestionMarksForList(usersList));
			    timeReportsStmt = conn.prepareStatement(timeReportsSql.toString());
			    timeReportsStmt.setInt(1, projectGroupId);
				for (int i = 0; i < weeks.size(); i++) {
					timeReportsStmt.setInt(i + 2, weeks.get(i));
				}
				for (int j = 0; j < usersList.size(); j++) {
					timeReportsStmt.setInt(j + weeks.size() + 2, usersList.get(j));
				}
			}
			ResultSet timeReportsRes = timeReportsStmt.executeQuery();
			ArrayList<Integer> timeReportsList = new ArrayList<Integer>();
			while (timeReportsRes.next()) {
				timeReportsList.add(timeReportsRes.getInt("id"));
			}
			timeReportsStmt.close();
			if (timeReportsList.size() == 0) {
//				System.err.println("getStatistics: No matching time reports.");
				return stats;
			}
			
			// Activities
			StringBuilder activitiesSql = new StringBuilder();
			PreparedStatement activitiesStmt;
			if (activities == null || activities.isEmpty()) {
			    activitiesSql.append("SELECT * FROM activities WHERE time_report_id IN ").append(getQuestionMarksForList(timeReportsList)).append(" ORDER BY id");
			    activitiesStmt = conn.prepareStatement(activitiesSql.toString());
				for (int i = 0; i < timeReportsList.size(); i++) {
					activitiesStmt.setInt(i + 1, timeReportsList.get(i));
				}
			} else {
			    activitiesSql.append("SELECT * FROM activities WHERE activity_nr IN ").append(getQuestionMarksForList(activities)).append(" AND time_report_id IN ").append(getQuestionMarksForList(timeReportsList)).append(" ORDER BY id");
			    activitiesStmt = conn.prepareStatement(activitiesSql.toString());
				for (int i = 0; i < activities.size(); i++) {
					activitiesStmt.setInt(i + 1, activities.get(i));
				}
				for (int j = 0; j < timeReportsList.size(); j++) {
					activitiesStmt.setInt(j + activities.size() + 1, timeReportsList.get(j));
				}
			}
			ResultSet activitiesRes = activitiesStmt.executeQuery();
			while (activitiesRes.next()) {
				// Add values to the map's lists
				TimeReport timeReport = getTimeReport(activitiesRes.getInt("time_report_id"));
				User user = getUser(timeReport.getUserId());
				stats.get("username").add(user.getUsername());
				stats.get("role").add(Integer.toString(user.getRole()));
				stats.get("activity_nr").add(activitiesRes.getString("activity_nr"));
				stats.get("week").add(Integer.toString(timeReport.getWeek()));
				stats.get("time").add(activitiesRes.getString("time"));
			}
			activitiesStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
//			System.err.println(e);
		}
		return stats;
	}
	
	private <T> String getQuestionMarksForList(List<T> list) {
	    StringBuilder str = new StringBuilder();
	    str.append("(");
	    for (int i = 0; i < list.size(); i++) {
	    	str.append("?");
	    	if (i < list.size() - 1) {
	    		str.append(", ");
	    	}
	    }
	    str.append(")");
	    return str.toString();
	}
	
	/**
	 * Calculates the total spend time per week as well as the total spend time 
	 * for all weeks for a project group.
	 * @param projectGroupId The id of the project group.
	 * @return A HashMap with week as key and time as value. There is also the 
	 * key "totalProjectTime" which contains the total project time. If the 
	 * project group did not contain any time reports and activities, or the 
	 * project group was not found, the map will only contain the 
	 * "totalProjectTime" key with value 0. 
	 */
	public HashMap<String, Integer> getTimePerWeek(int projectGroupId) {
		HashMap<String, Integer> timePerWeek = new HashMap<String, Integer>();
		ArrayList<TimeReport> timeReports = getTimeReportsForProjectGroupId(projectGroupId);
		int totalTime = 0;
		for (TimeReport timeReport : timeReports) {
			String week = Integer.toString(timeReport.getWeek());
			ArrayList<Activity> activities = getActivities(timeReport.getId());
			int weekSum = 0;
			for (Activity activity : activities) {
				weekSum += activity.getTime();
			}
			totalTime += weekSum;
			if (timePerWeek.get(week) == null) {
				timePerWeek.put(week, weekSum);
			} else {
				timePerWeek.put(week, timePerWeek.get(week) + weekSum);
			}
		}
		timePerWeek.put("totalProjectTime", totalTime);
		return timePerWeek;
	}
}