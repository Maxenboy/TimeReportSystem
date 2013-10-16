package subTimeReportMenu;

import java.util.ArrayList;

import database.Database;
import database.TimeReport;

public class TimeReportGenerator {
	private Database db;
	
	public TimeReportGenerator(Database db) {
		this.db = db;
	}
	
	public String showTimeReports(int userId) {
		ArrayList<TimeReport> timeReports = db.getTimeReportsForProjectGroupId(userId);
		if(timeReports.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < timeReports.size(); i++) {
			
		}
		
		return null;
	}
	
	/**
	 * Creates the form where user can fill in an new TimeReport
	 */
	public String showNewTimeReport() {
		return null;
	}
	
	public String showChangeTimeReport(int reportId) {
		return null;
	}
	
	public String showSuccess(int i) {
		return null;
	}
	
	public String showFail(int i) {
		return null;
	}
	
	public String showTimeReport(int reportId) {
		return null;
	}
}
