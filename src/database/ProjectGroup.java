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

	//TODO: Override equals
	//TODO: Override toString
}
