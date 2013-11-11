package database;

public class TimeReport {
	private int id;
	private int week;
	private boolean signed;
	private int userId;
	private int projectGroupId;

	/**
	 * Konstruktor som ska anv\u00E4ndas av databasklassen f\u00F6r att skapa ett 
	 * objekt av en befintlig tidrapport.
	 * @param id
	 * @param week
	 * @param signed
	 * @param userId
	 * @param projectGroupId
	 */
	public TimeReport(int id, int week, boolean signed, int userId, int projectGroupId) {
		this.id = id;
		this.week = week;
		this.signed = signed;
		this.userId = userId;
		this.projectGroupId = projectGroupId;
	}

	/**
	 * Konstruktor f\u00F6r att skapa en ny osignerad tidrapport.
	 * @param week
	 * @param userId
	 * @param projectGroupId
	 */
	public TimeReport(int week, int userId, int projectGroupId) {
		id = 0;
		this.week = week;
		signed = false;
		this.userId = userId;
		this.projectGroupId = projectGroupId;
	}

	/**
	 * Returnerar tidrapportens id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * S\u00E4tter tidrapportens id.
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returnerar tidrapportens vecka.
	 */
	public int getWeek() {
		return week;
	}

	/**
	 * S\u00E4tter tidrapportens vecka.
	 * @param week
	 */
	public void setWeek(int week) {
		this.week = week;
	}

	/**
	 * Returnerar huruvida tidrapporten \u00E4r signerad eller inte.
	 */
	public boolean isSigned() {
		return signed;
	}

	/**
	 * S\u00E4tter tidrapporten till signerad eller osignerad.
	 * @param signed
	 */
	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	/**
	 * Returnerar tidrapportens anv\u00E4ndarid.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * S\u00E4tter tidrapportens anv\u00E4ndarid.
	 * @param userId
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * Returnerar tidrapportens projektgruppsid.
	 */
	public int getProjectGroupId() {
		return projectGroupId;
	}

	/**
	 * S\u00E4tter tidrapportens projektgruppsid.
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TimeReport [id=" + id + ", week=" + week + ", signed=" + signed
				+ ", userId=" + userId + ", projectGroupId=" + projectGroupId
				+ "]";
	}
	
}
