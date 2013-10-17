package database;

public class ProjectGroup {
	private int id;
	private String projectName;
	private boolean active;
	private int startWeek;
	private int endWeek;
	private int estimatedTime;

	/**
	 * Constructor used by database
	 * @param id
	 * @param projectName
	 * @param active
	 * @param startWeek
	 * @param endWeek
	 * @param estimatedTime
	 */
	public ProjectGroup(int id, String projectName, boolean active,
			int startWeek, int endWeek, int estimatedTime) {
		this.id = id;
		this.projectName = projectName;
		this.active = active;
		this.startWeek = startWeek;
		this.endWeek = endWeek;
		this.estimatedTime = estimatedTime;
	}

	/**
	 * Constructor used for creating a new record in the database
	 * @param projectName
	 * @param startWeek
	 * @param endWeek
	 * @param estimatedTime
	 */
	public ProjectGroup(String projectName, int startWeek, int endWeek,
			int estimatedTime) {
		this.projectName = projectName;
		this.startWeek = startWeek;
		this.endWeek = endWeek;
		this.estimatedTime = estimatedTime;
		this.active = true;
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
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return
	 */
	public int getStartWeek() {
		return startWeek;
	}

	/**
	 * @param startWeek
	 */
	public void setStartWeek(int startWeek) {
		this.startWeek = startWeek;
	}

	/**
	 * @return
	 */
	public int getEndWeek() {
		return endWeek;
	}

	/**
	 * @param endWeek
	 */
	public void setEndWeek(int endWeek) {
		this.endWeek = endWeek;
	}

	/**
	 * @return
	 */
	public int getEstimatedTime() {
		return estimatedTime;
	}

	/**
	 * @param estimatedTime
	 */
	public void setEstimatedTime(int estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + endWeek;
		result = prime * result + estimatedTime;
		result = prime * result + id;
		result = prime * result
				+ ((projectName == null) ? 0 : projectName.hashCode());
		result = prime * result + startWeek;
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
		ProjectGroup other = (ProjectGroup) obj;
		if (active != other.active)
			return false;
		if (endWeek != other.endWeek)
			return false;
		if (estimatedTime != other.estimatedTime)
			return false;
		if (id != other.id)
			return false;
		if (projectName == null) {
			if (other.projectName != null)
				return false;
		} else if (!projectName.equals(other.projectName))
			return false;
		if (startWeek != other.startWeek)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProjectGroup [id=" + id + ", projectName=" + projectName
				+ ", active=" + active + ", startWeek=" + startWeek
				+ ", endWeek=" + endWeek + ", estimatedTime=" + estimatedTime
				+ "]";
	}

	
}
