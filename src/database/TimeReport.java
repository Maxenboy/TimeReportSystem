package database;

public class TimeReport {
	private int id;
	private int week;
	private boolean signed;
	private int userId;
	private int projectGroupId;

	/**
	 * Constructor used by database
	 * @param id
	 * @param week
	 * @param signed
	 * @param userId
	 * @param projectGroupId
	 */
	public TimeReport(int id, int week, boolean signed, int userId,
			int projectGroupId) {
		this.id = id;
		this.week = week;
		this.signed = signed;
		this.userId = userId;
		this.projectGroupId = projectGroupId;
	}

	/**
	 * Constructor used for creating a new record
	 * @param week
	 * @param userId
	 * @param projectGroupId
	 */
	public TimeReport(int week, int userId, int projectGroupId) {
		this.week = week;
		this.userId = userId;
		this.projectGroupId = projectGroupId;
	}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public int getWeek() {
		return week;
	}

	/**
	 * @param week
	 */
	public void setWeek(int week) {
		this.week = week;
	}

	/**
	 * @return
	 */
	public boolean isSigned() {
		return signed;
	}

	/**
	 * @param signed
	 */
	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	/**
	 * @return
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return
	 */
	public int getProjectGroupId() {
		return projectGroupId;
	}

	/**
	 * @param projectGroupId
	 */
	public void setProjectGroupId(int projectGroupId) {
		this.projectGroupId = projectGroupId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
