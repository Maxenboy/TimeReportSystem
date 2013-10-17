package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.Statement;

public class Database {
	private Connection conn;

	/**
	 * 
	 */
	public Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:8889/puss1302?user=puss1302&password=jks78ww2");
			// conn =
			// DriverManager.getConnection("jdbc:mysql://vm26.cs.lth.se/puss1302?"
			// + "user=puss1302&password=jks78ww2");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// User-metoder
	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public User loginUser(String username, String password) {
		User u = null;
		try {
			String getTableSQL = "SELECT * FROM users WHERE username = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setString(1, username);
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
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
	 * @param user
	 * @return
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

	/**
	 * @param username
	 * @return
	 */
	public User getUser(String username) {
		User u = null;
		try {
			String getTableSQL = "SELECT * FROM users WHERE username = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setString(1, username);
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
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
	 * @return
	 */
	public ArrayList<User> getUsers() {
		ArrayList<User> list = new ArrayList<User>();
		try {
			String getUsersSQL = "SELECT * FROM users";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getUsersSQL);
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
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
	 * @param projectGroupId
	 * @return
	 */
	public ArrayList<User> getUsers(int projectGroupId) {
		ArrayList<User> list = new ArrayList<User>();
		try {
			String getUsersSQL = "SELECT * FROM users WHERE project_group_id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(getUsersSQL);
			preparedStatement.setInt(1, projectGroupId);
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
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
	 * @param userId
	 * @return
	 */
	public User getUser(int userId) {
		User u = null;
		try {
			String getTableSQL = "SELECT * FROM users WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, userId);
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
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
	 * @param userId
	 * @return
	 */
	public boolean activateUser(int userId) {
		return activateUserHelpMethod(userId, 1);
	}

	/**
	 * @param userId
	 * @return
	 */
	public boolean deactivateUser(int userId) {
		return activateUserHelpMethod(userId, 0);
	}

	/**
	 * @param userId
	 * @param active
	 * @return
	 */
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
	 * @param roles
	 * @return
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

	/**
	 * @param userId
	 * @param role
	 * @return
	 */
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
	 * @param projectGroupId
	 * @return
	 */
	/**
	 * @param projectGroupId
	 * @return
	 */
	public boolean activateProjectGroup(int projectGroupId) {
		return activateProjectGroupHelpMethod(projectGroupId, 1);
	}

	/**
	 * @param projectGroupId
	 * @return
	 */
	public boolean deactivateProjectGroup(int projectGroupId) {
		return activateProjectGroupHelpMethod(projectGroupId, 0);
	}

	/**
	 * @param projectGroupId
	 * @param active
	 * @return
	 */
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
	 * @param projectGroup
	 * @return
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

	/**
	 * @param userId
	 * @param projectGroupId
	 * @return
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
	 * @param userId
	 * @param projectGroupId
	 * @return
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
	 * @param projectGroupId
	 * @return
	 */
	public ProjectGroup getProjectGroup(int projectGroupId) {
		ProjectGroup pg = null;
		try {
			String getTableSQL = "SELECT * FROM project_groups WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, projectGroupId);
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
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
	 * @return
	 */
	public ArrayList<ProjectGroup> getProjectGroups() {
		ArrayList<ProjectGroup> list = new ArrayList<ProjectGroup>();
		try {
			String getTableSQL = "SELECT * FROM project_groups";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
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
	 * @param projectGroupId
	 * @return
	 */
	public ArrayList<TimeReport> getTimeReportsForProjectGroupId(
			int projectGroupId) {
		ArrayList<TimeReport> list = new ArrayList<TimeReport>();
		try {
			String getTableSQL = "SELECT * FROM time_reports WHERE project_group_id = ? ORDER BY id";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, projectGroupId);
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
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
	 * @param projectGroupId
	 * @return
	 */
	public ArrayList<TimeReport> getUnsignedTimeReports(int projectGroupId) {
		ArrayList<TimeReport> list = new ArrayList<TimeReport>();
		try {
			String getTableSQL = "SELECT * FROM time_reports WHERE project_group_id = ? AND signed = 0 ORDER BY id";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, projectGroupId);
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
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
	 * @param projectGroupId
	 * @return
	 */
	public ArrayList<TimeReport> getSignedTimeReports(int projectGroupId) {
		ArrayList<TimeReport> list = new ArrayList<TimeReport>();
		try {
			String getTableSQL = "SELECT * FROM time_reports WHERE project_group_id = ? AND signed = 1 ORDER BY id";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, projectGroupId);
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
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
	 * @param timeReports
	 * @return
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

	/**
	 * @param timeReport
	 * @param signed
	 * @return
	 */
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
	 * @param timeReports
	 * @return
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
	 * @param timeReportId
	 * @return
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
	 * @param timeReport
	 * @param activities
	 * @return
	 */
	public boolean updateTimeReport(TimeReport timeReport, ArrayList<Activity> activities) {
		if (removeTimeReport(timeReport.getId())) {
			return addTimeReport(timeReport, activities);
		} else {
			return false;
		}
	}

	/**
	 * @param userId
	 * @return
	 */
	public ArrayList<TimeReport> getTimeReportsForUserId(int userId) {
		ArrayList<TimeReport> list = new ArrayList<TimeReport>();
		try {
			String getTableSQL = "SELECT * FROM time_reports WHERE user_id = ? ORDER BY id";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, userId);
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
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
	 * the id to the time report.
	 * @param timeReport The time report.
	 * @param activities A list containing the activities.
	 * @return true if the time report and the activities was added successfully,
	 * false otherwise.
	 */
	public boolean addTimeReport(TimeReport timeReport,
			ArrayList<Activity> activities) {
		if (timeReport.getId() != 0) {
			System.err
					.println("addTimeReport: TimeReport id not 0. Adding it anyways.");
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
				System.err
						.println("addTimeReport: TimeReport: could not get id from database");
				return false;
			}
			rs.close();
			preparedStatement.close();

			for (Activity activity : activities) {
				if (activity.getId() != 0) {
					System.err
							.println("addTimeReport: Activity id not 0. Adding it anyways.");
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
					System.err
							.println("addTimeReport: The time report id of the Activity does not match that of the time report id. The id from the time report will be used.");
				}
				activity.setTimeReportId(timeReport.getId());
				preparedStatementActivity.setInt(4, timeReport.getId());
				preparedStatementActivity.executeUpdate();
				ResultSet rsActivity = preparedStatementActivity
						.getGeneratedKeys();
				if (rsActivity.next()) {
					activity.setId(rsActivity.getInt(1));
				} else {
					System.err
							.println("addTimeReport: Activity: could not get id from database");
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
	 * @param timeReportId
	 * @return
	 */
	public TimeReport getTimeReport(int timeReportId) {
		TimeReport report = null;
		try {
			String getTableSQL = "SELECT * FROM time_reports WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, timeReportId);
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
			while (res.next()) {
				report = new TimeReport(res.getInt(1), res.getInt(2),
						res.getBoolean(3), res.getInt(4), res.getInt(5));
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			System.err.println(ex);
		}
		return report;
	}

	// Activity-metoder
	/**
	 * @param timeReportId
	 * @return
	 */
	public ArrayList<Activity> getActivities(int timeReportId) {
		ArrayList<Activity> list = new ArrayList<Activity>();
		try {
			String getUsersSQL = "SELECT * FROM activities where time_report_id = ? order by id";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getUsersSQL);
			preparedStatement.setInt(1, timeReportId);
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
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
	 * @param activityId
	 * @return
	 */
	public Activity getActivity(int activityId) {
		Activity activity;
		try {
			String getTableSQL = "SELECT * FROM activities WHERE id = ? LIMIT 1";
			PreparedStatement preparedStatement = conn
					.prepareStatement(getTableSQL);
			preparedStatement.setInt(1, activityId);
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
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

	// Statistics-metoder
	/*
	 * Returnerar alla unika roller, användare, aktiviteter och veckor ur
	 * databasen. Nyckel: user, role, activity och week. Värde: alla unika
	 * värden för nycklarna.
	 */
	/**
	 * @param projectGroupId
	 * @return
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
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
			while(res.next()) {
				System.out.println("role");
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
				preparedStatement.executeQuery();
				ResultSet res = preparedStatement.getResultSet();
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
			preparedStatement.executeQuery();
			ResultSet res = preparedStatement.getResultSet();
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

	/*
	 * Returnerar en HashMap där nyckeln är users, roles, activites, weeks eller
	 * time. Värdena är alla fält i databasen i ordning så att index i listorna
	 * stämmer överrens mot varandra.
	 */
	/**
	 * @param projectGroupId
	 * @param usernames
	 * @param roles
	 * @param activities
	 * @param weeks
	 * @return
	 */
	public HashMap<String, ArrayList<String>> getStatistics(int projectGroupId,
			ArrayList<String> usernames, ArrayList<Integer> roles,
			ArrayList<Integer> activities, ArrayList<Integer> weeks) {
		return null;
	}

	/**
	 * Calculates the total spend time per week as well as the total spend time 
	 * for all weeks for project group.
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
