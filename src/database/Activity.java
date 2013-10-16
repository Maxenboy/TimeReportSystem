package database;

public class Activity {
	public static final int ACTIVITY_NR_SDP = 11;
	public static final int ACTIVITY_NR_SRS = 12;
	public static final int ACTIVITY_NR_SVVS = 13;
	public static final int ACTIVITY_NR_STLDD = 14;
	public static final int ACTIVITY_NR_SVVI = 15;
	public static final int ACTIVITY_NR_SDDD = 16;
	public static final int ACTIVITY_NR_SVVR = 17;
	public static final int ACTIVITY_NR_SSD = 18;
	public static final int ACTIVITY_NR_FINAL_REPORT = 19;
	public static final int ACTIVITY_NR_FUNTION_TEST = 21;
	public static final int ACTIVITY_NR_SYSTEM_TEST = 22;
	public static final int ACTIVITY_NR_REGRESSION_TEST = 23;
	public static final int ACTIVITY_NR_MEETING = 30;
	public static final int ACTIVITY_NR_LECTURE = 41;
	public static final int ACTIVITY_NR_EXERCISE = 42;
	public static final int ACTIVITY_NR_COMPUTER_EXERCISE = 43;
	public static final int ACTIVITY_NR_HOME_STUDIES = 44;
	public static final int ACTIVITY_NR_OTHER = 100;
	public static final String ACTIVITY_TYPE_OTHER = "A";
	public static final String ACTIVITY_TYPE_DEVELOPMENT = "U";
	public static final String ACTIVITY_TYPE_INFORMAL_REVIEW = "I";
	public static final String ACTIVITY_TYPE_FORMAL_REVIEW = "F";
	public static final String ACTIVITY_TYPE_REWORK = "O";
	
	private int id;
	private int activityNr;
	private String activityType;
	private int time;
	private int timeReportId;
	
	public Activity(int id, int activityNr, String activityType, int time,
			int timeReportId) {
		this.id = id;
		this.activityNr = activityNr;
		this.activityType = activityType;
		this.time = time;
		this.timeReportId = timeReportId;
	}

	public Activity(int activityNr, String activityType, int time,
			int timeReportId) {
		this.activityNr = activityNr;
		this.activityType = activityType;
		this.time = time;
		this.timeReportId = timeReportId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getActivityNr() {
		return activityNr;
	}

	public void setActivityNr(int activityNr) {
		this.activityNr = activityNr;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getTimeReportId() {
		return timeReportId;
	}

	public void setTimeReportId(int timeReportId) {
		this.timeReportId = timeReportId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + activityNr;
		result = prime * result
				+ ((activityType == null) ? 0 : activityType.hashCode());
		result = prime * result + id;
		result = prime * result + time;
		result = prime * result + timeReportId;
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
		Activity other = (Activity) obj;
		if (activityNr != other.activityNr)
			return false;
		if (activityType == null) {
			if (other.activityType != null)
				return false;
		} else if (!activityType.equals(other.activityType))
			return false;
		if (id != other.id)
			return false;
		if (time != other.time)
			return false;
		if (timeReportId != other.timeReportId)
			return false;
		return true;
	}
	
	//TODO: Override toString
}
