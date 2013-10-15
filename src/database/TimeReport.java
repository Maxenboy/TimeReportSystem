package database;

public class TimeReport {
	public int id;
	public int week;
	public boolean signed;
	public int userId;
	public int projectGroupId;

	public TimeReport(int id, int week, boolean signed, int userId,
			int projectGroupId) {
		this.id = id;
		this.week = week;
		this.signed = signed;
		this.userId = userId;
		this.projectGroupId = projectGroupId;
	}

	public TimeReport(int week, int userId, int projectGroupId) {
		this.week = week;
		this.userId = userId;
		this.projectGroupId = projectGroupId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public boolean isSigned() {
		return signed;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getProjectGroupId() {
		return projectGroupId;
	}

	public void setProjectGroupId(int projectGroupId) {
		this.projectGroupId = projectGroupId;
	}

	//TODO: Override equals
	//TODO: Override toString
}
