package subTimeReportMenu;

import java.util.ArrayList;

import database.Database;
import database.TimeReport;

public class TimeReportGenerator {
	private Database db;
	
	public TimeReportGenerator(Database db) {
		this.db = db;
	}
	
	private String formElement(String par) {
		return '"' + par + '"';
	}
	
	private String buildTable() {
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
		sb.append("<form>");
		sb.append(buildTable());
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
