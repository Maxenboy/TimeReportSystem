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
	private int activity;
	private String activityType;
	private int time;
	private int timeReportId;
	
	public Activity(int id, int activity, String activityType, int time,
			int timeReportId) {
		this.id = id;
		this.activity = activity;
		this.activityType = activityType;
		this.time = time;
		this.timeReportId = timeReportId;
	}

	public Activity(int activity, String activityType, int time,
			int timeReportId) {
		this.activity = activity;
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

	public int getActivity() {
		return activity;
	}

	public void setActivity(int activity) {
		this.activity = activity;
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
	
	//TODO: Override equals
	//TODO: Override toString
}
