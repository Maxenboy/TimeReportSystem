package database;

public class TimeReport {
	private int id;
	private int week;
	private boolean signed;
	private int userId;
	private int projectGroupId;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + projectGroupId;
		result = prime * result + (signed ? 1231 : 1237);
		result = prime * result + userId;
		result = prime * result + week;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeReport other = (TimeReport) obj;
		if (id != other.id)
			return false;
		if (projectGroupId != other.projectGroupId)
			return false;
		if (signed != other.signed)
			return false;
		if (userId != other.userId)
			return false;
		if (week != other.week)
			return false;
		return true;
	}

	//TODO: Override toString
}
