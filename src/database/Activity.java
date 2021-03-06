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
	
	/**
	 * Konstruktor som ska anv\u00E4ndas av databasklassen f\u00F6r att skapa ett 
	 * objekt av en befintlig aktivitet.
	 * @param id
	 * @param activityNr
	 * @param activityType
	 * @param time
	 * @param timeReportId
	 */
	public Activity(int id, int activityNr, String activityType, int time, int timeReportId) {
		this.id = id;
		this.activityNr = activityNr;
		this.activityType = activityType;
		this.time = time;
		this.timeReportId = timeReportId;
	}

	/**
	 * Konstruktor f\u00F6r att skapa en ny aktivitet.
	 * @param activityNr
	 * @param activityType
	 * @param time
	 * @param timeReportId
	 */
	public Activity(int activityNr, String activityType, int time, int timeReportId) {
		id = 0;
		this.activityNr = activityNr;
		this.activityType = activityType;
		this.time = time;
		this.timeReportId = timeReportId;
	}

	/**
	 * Returnerar aktivitetens id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * S\u00E4tter aktivitetens id.
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returnerar aktivitetens aktivitetsnummer.
	 */
	public int getActivityNr() {
		return activityNr;
	}

	/**
	 * S\u00E4tter atkvitetens aktivitetsnummer.
	 * @param activityNr
	 */
	public void setActivityNr(int activityNr) {
		this.activityNr = activityNr;
	}

	/**
	 * Returnerar aktivitetens aktivitetstyp.
	 */
	public String getActivityType() {
		return activityType;
	}

	/**
	 * S\u00E4tter aktivitetens aktivitetstyp.
	 * @param activityType
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	/**
	 * Returnerar aktivitetens tid.
	 */
	public int getTime() {
		return time;
	}

	/**
	 * S\u00E4tter aktivitetens tid.
	 * @param time
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * Returnerar aktivitetens tidrapportsid.
	 */
	public int getTimeReportId() {
		return timeReportId;
	}

	/**
	 * S\u00E4tter aktivitetens tidrapportid.
	 * @param timeReportId
	 */
	public void setTimeReportId(int timeReportId) {
		this.timeReportId = timeReportId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Activity [id=" + id + ", activityNr=" + activityNr
				+ ", activityType=" + activityType + ", time=" + time
				+ ", timeReportId=" + timeReportId + "]";
	}
	
	/**
	 * Mappar aktivitetstyp till motsvarande heltal.
	 * @return Heltal
	 */
	public int mapActivityTypeToInt() {
		switch(activityType) {
		case ACTIVITY_TYPE_DEVELOPMENT:
			return 0;
		case ACTIVITY_TYPE_INFORMAL_REVIEW:
			return 1;
		case ACTIVITY_TYPE_FORMAL_REVIEW:
			return 2;
		case ACTIVITY_TYPE_REWORK:
			return 3;
		}
		return 0;
	}
	
	/**
	 * Mappar heltal till motsvarande aktivitetstyp.
	 * @param typeInteger Heltal
	 * @return Aktivitetstyp. Null om den inte hittades.
	 */
	public static String mapIntToActivityType(int typeInteger) {
		switch(typeInteger) {
		case 0:
			return ACTIVITY_TYPE_DEVELOPMENT;
		case 1:
			return ACTIVITY_TYPE_INFORMAL_REVIEW;
		case 2:
			return ACTIVITY_TYPE_FORMAL_REVIEW;
		case 3:
			return ACTIVITY_TYPE_REWORK;
		}
		return null;
	}
	
	/**
	 * Mappar aktivitetsnummer till motsvarande dokumentnamn.
	 * @param activityNbr Aktivitetsnummer
	 * @return Aktivitetsstr\u00E4ng. Null om den inte hittades.
	 */
	public static String mapActivityNrToString(int activityNbr) {
		switch(activityNbr) {
		case ACTIVITY_NR_SDP:
			return "SDP";
		case ACTIVITY_NR_SRS:
			return "SRS";
		case ACTIVITY_NR_SVVS:
			return "SVVS";
		case ACTIVITY_NR_STLDD:
			return "STLDD";
		case ACTIVITY_NR_SVVI:
			return "SVVI";
		case ACTIVITY_NR_SDDD:
			return "SDDD";
		case ACTIVITY_NR_SVVR:
			return "SVVR";
		case ACTIVITY_NR_SSD:
			return "SSD";
		case ACTIVITY_NR_FINAL_REPORT:
			return "Slutrapport";
		case ACTIVITY_NR_FUNTION_TEST:
			return "Funktionstest";
		case ACTIVITY_NR_SYSTEM_TEST:
			return "Systemtest";
		case ACTIVITY_NR_REGRESSION_TEST:
			return "Regressionstest";
		case ACTIVITY_NR_MEETING:
			return "M\u00F6te";
		case ACTIVITY_NR_LECTURE:
			return "F\u00F6rel\u00E4sning";
		case ACTIVITY_NR_EXERCISE:
			return "\u00D6vning";
		case ACTIVITY_NR_COMPUTER_EXERCISE:
			return "Dator\u00F6vning";
		case ACTIVITY_NR_HOME_STUDIES:
			return "Sj\u00E4lvstudier";
		case ACTIVITY_NR_OTHER:
			return "\u00D6vrigt";
		}
		return null;
	}
}
