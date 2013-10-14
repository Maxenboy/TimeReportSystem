package database;

public class ProjectGroup {

	public int id;
	public String projectName;
	public boolean active;
	public int startWeek;
	public int endWeek;
	public int estimatedTime;

	public ProjectGroup(int id, String projectName, boolean active,
			int startWeek, int endWeek, int estimatedTime) {
		this.id = id;
		this.projectName = projectName;
		this.active = active;
		this.startWeek = startWeek;
		this.endWeek = endWeek;
		this.estimatedTime = estimatedTime;
	}
	
	public ProjectGroup(String projectName, int startWeek, int endWeek,
			int estimatedTime) {
		this.projectName = projectName;
		this.startWeek = startWeek;
		this.endWeek = endWeek;
		this.estimatedTime = estimatedTime;
		this.active = true;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getStartWeek() {
		return startWeek;
	}

	public void setStartWeek(int startWeek) {
		this.startWeek = startWeek;
	}

	public int getEndWeek() {
		return endWeek;
	}

	public void setEndWeek(int endWeek) {
		this.endWeek = endWeek;
	}

	public int getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(int estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

}
