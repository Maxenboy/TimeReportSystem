package subTimeReportMenu;

import java.util.ArrayList;
import java.util.HashMap;

import database.*;

public class TimeReportGenerator {
	private Database db;
	
	public TimeReportGenerator(Database db) {
		this.db = db;
	}
	
	private String formElement(String par) {
		return '"' + par + '"';
	}
	
	private String buildShowTimeReportTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table border=" + formElement("1") + ">");
		sb.append("<tr>");
		sb.append("<th>Report ID</th>");
		sb.append("<th>Week</th>");
		sb.append("<th>Signed</th>");
		sb.append("<th>Select</th>");
		sb.append("</tr>");
		return sb.toString();
	}
	
	public String showTimeReports(int userId) {
		ArrayList<TimeReport> timeReports = db.getTimeReportsForUserId(userId);
		if(timeReports.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=post ACTION="+formElement("ShowTimeReports")+">");
		sb.append(buildShowTimeReportTable());
		for (int i = 0; i < timeReports.size(); i++) {
			TimeReport tr = timeReports.get(i);
			sb.append("<tr>");
			sb.append("<td>" + tr.getId() + "</td>");
			sb.append("<td>" + tr.getWeek() + "</td>");
			if(tr.isSigned())
				sb.append("<td>Y/td>");
			else
				sb.append("<td>N</td>");
			sb.append("<td>" + createRadio(tr.getId())+ "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("<INPUT TYPE="+ formElement("submit") + "VALUE=" + formElement("Get Report") +">");
		sb.append("</form>");
		return sb.toString();
	}
	
	private String createRadio(int id) {
		return "<input type=" + formElement("radio") + "name=" + 
				formElement("reportId") + "value=" + formElement(Integer.toString(id))+">";
	}
	
	/**
	 * Creates the form where user can fill in an new TimeReport
	 */
	public String showNewTimeReport() {
		return null;
	}
	
	public String showChangeTimeReport(int reportId) {
		ArrayList<Activity> activities = db.getActivities(reportId);
		StringBuilder sb = new StringBuilder();
		sb.append(reportId);
		return null;
	}
	
	public String showSuccess(int i) {
		return null;
	}
	
	public String showFail(int i) {
		return null;
	}
	
	public String showTimeReport(int reportId) {		
		ArrayList<Activity> activities = db.getActivities(reportId);
		TimeReport tr = db.getTimeReport(reportId);
		ProjectGroup pg = db.getProjectGroup(tr.getProjectGroupId());
		StringBuilder sb = new StringBuilder();
		if(activities.isEmpty())
			System.out.println("Something is very wrong");
		//Table setup
		sb.append("<table border=" + formElement("1") + ">");
		sb.append("<tr>");
		sb.append("<TD COLSPAN=2><B>Report id:</B></TD><TD COLSPAN=2>" + reportId + "</TD>");
		sb.append("<TD><B>Week:</B></TD><TD COLSPAN=2>" + tr.getWeek() + "</TD>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<TD COLSPAN=2><B>Project name:</B></TD><TD COLSPAN=2>" + pg.getProjectName() + "</TD>");
		sb.append("<TD><B>Signed:</B></TD><TD COLSPAN=2>");
		sb.append(tr.isSigned() ? "Y" : "N");
		sb.append("</TD></tr>");
		sb.append("<tr>");
		sb.append("<TH>Activity Number</TH>" + 
				"<TH WIDTH=75>D</TH><TH WIDTH=75>I</TH><TH WIDTH=75>F</TH>" + 
					"<TH WIDTH=75>R</TH><TH WIDTH=75>Total time</TH></tr>");
		HashMap<Integer,int[]> table = createTimeReportPresentationTable(activities);
		for (int i = 11; i < 20; i++) {
			int[] values = table.get(i);
			if(values != null) {
				sb.append("<td><i>" + i + "</i></td>");
				int total = 0;
				for (int j = 0; j < values.length; j++) {
					sb.append("<td><i>" + values[j] + "</i></td>");
					total += values[j];
				}
				sb.append("<td>" + total + "</td>");
				sb.append("<tr>");
			}
		}
		
		sb.append("</table>");
		return sb.toString();
	}
	
	private HashMap<Integer,int[]> createTimeReportPresentationTable(ArrayList<Activity> activities) {
		HashMap<Integer,int[]> table = new HashMap<Integer,int[]>();
		for (int i = 0; i < activities.size(); i++) {
			Activity a = activities.get(i);
			if(table.containsKey(a.getActivityNr())) {
				String type = a.getActivityType();
				int[] workTime = table.get(a.getActivityNr());
				workTime[a.mapActivityTypeToInt()] += a.getTime();
			} else {
				int[] workTime = new int[4];
				workTime[a.mapActivityTypeToInt()] += a.getTime();
				table.put(a.getActivityNr(), workTime);
			}
		}
		return table;
	}
}
