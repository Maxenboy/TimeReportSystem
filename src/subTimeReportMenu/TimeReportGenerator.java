package subTimeReportMenu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import database.*;

public class TimeReportGenerator {
	private Database db;
	public static final int SHOW_ALL = 1;
	public static final int SHOW_USER_REPORT = 2;
	public static final int SHOW_SIGN = 3;
	public static final int SHOW_UNSIGNED = 4;
	public static final int REMOVE_USER_REPORT = 5;
	public static final int REMOVE_PRJ_REPORTS = 6;
	public static final int REMOVE_REPORT = 7;
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
	
	private String buildSignTimeReportTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table border=" + formElement("1") + ">");
		sb.append("<tr>");
		sb.append("<th>User</th>");
		sb.append("<th>Report ID</th>");
		sb.append("<th>Week</th>");
		sb.append("<th>Select</th>");
		sb.append("</tr>");
		return sb.toString();
	}

	private String showTimeReportsWithCheckBoxes(ArrayList<TimeReport> timeReports, int state) {
		if(timeReports.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		String s = new String();
		String buttonName = new String();
		switch(state) {
		case REMOVE_PRJ_REPORTS:
			buttonName = "Delete Reports";
			s = "RemoveTimeReports";
			break;
		case REMOVE_USER_REPORT:
			buttonName = "Delete Reports";
			s = "RemoveTimeReports";
			break;
		}
		sb.append("<FORM METHOD=post ACTION="+formElement(s)+">");
		sb.append(buildSignTimeReportTable());
		for (int i = 0; i < timeReports.size(); i++) {
			TimeReport tr = timeReports.get(i);
			sb.append("<tr>");
			sb.append("<td>" + tr.getUserId() + "</td>");
			sb.append("<td>" + tr.getId() + "</td>");
			sb.append("<td>" + tr.getWeek() + "</td>");
			sb.append("<td>" + createCheck(tr.getId())+ "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("<INPUT TYPE="+ formElement("submit") + "VALUE=" + formElement(buttonName) +">");
		sb.append("</form>");
		return sb.toString();
	}
	
	private String createCheck(int id) {
		return "<input type=" + formElement("checkbox") + "name=" + 
				formElement("reportIds") + "value=" + formElement(Integer.toString(id))+">";
	}
	
	/**
	 * Helper method for showAllTimeReports
	 * @param timeReports The time reports that should be listed
	 * @param state Defines whether user is about to remove time reports or wants to view them.
	 * @return
	 */
	private String listReports(ArrayList<TimeReport> timeReports, int state) {
		StringBuilder sb = new StringBuilder();
		if(timeReports.isEmpty()) {
			return null;
		}
		switch(state) {
		case REMOVE_REPORT:
			sb.append("<FORM METHOD=post ACTION="+formElement("RemoveTimeReport")+">");
			break;
		case SHOW_USER_REPORT:
			sb.append("<FORM METHOD=post ACTION="+formElement("ShowTimeReports")+">");
			break;
		case SHOW_SIGN:
			sb.append("<FORM METHOD=post ACTION="+formElement("SignTimeReports")+">");
			break;
		case SHOW_UNSIGNED:
			sb.append("<FORM METHOD=post ACTION="+formElement("SignTimeReports")+">");
			break;
		}
		sb.append(buildShowTimeReportTable());
		for (int i = 0; i < timeReports.size(); i++) {
			TimeReport tr = timeReports.get(i);
			sb.append("<tr>");
			sb.append("<td>" + tr.getId() + "</td>");
			sb.append("<td>" + tr.getWeek() + "</td>");
			if(tr.isSigned())
				sb.append("<td>Y</td>");
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
	 * Lists all time reports belonging to a specific ID
	 * @param ID could be project_group_ID or userId
	 * @param state use these different values
	 * SHOW_USER_REPORT Time reports for certain user
	 * SHOW_UNSIGNED if user is proj-leader and wants to list all UNsigned reports for that project_group_id
	 * SHOW_SIGNED if user is proj-leader and wants to list all signed reports for that project_group_id
	 * REMOVE_PRJ_REPORTS if user is proj-leader and wants to remove unsigned time reports
	 * REMOVE_USER_REPORT if user wants to remove a time report.
	 * @return html-code 
	 */
	public String showAllTimeReports(int ID, int state) {
		ArrayList<TimeReport> timeReports = new ArrayList<TimeReport>();
		String html = null;
		switch(state) {
		case SHOW_USER_REPORT:
			timeReports = db.getTimeReportsForUserId(ID);
			html = listReports(timeReports, state);
			break;
		case SHOW_UNSIGNED:
			timeReports = db.getUnsignedTimeReports(ID);
			html = listReports(timeReports,state);
			break;
		case SHOW_SIGN:
			timeReports = db.getSignedTimeReports(ID);
			html = listReports(timeReports, state);
			break;
		case REMOVE_PRJ_REPORTS:
			timeReports = db.getUnsignedTimeReports(ID);
			html = showTimeReportsWithCheckBoxes(timeReports, state);
			break;
		case REMOVE_USER_REPORT:
			timeReports = db.getTimeReportsForUserId(ID);
			html = listReports(timeReports, REMOVE_REPORT);
		}
		return html;
	}
	
	
	
	/**
	 * Creates the form where user can fill in an new TimeReport
	 */
	public String showNewTimeForm() {
		File file = new File("git/puss/WebContent/NewTimeReport.html");
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s;
			while((s = br.readLine()) != null) 
				sb.append(s);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}
	
	public boolean addNewTimeReport(TimeReport timeReport, ArrayList<Activity> activities) {
		return db.addTimeReport(timeReport, activities); 
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
	
	/**
	 * Shows all the information associated with a certain time report
	 * @param reportId The ID of the time report the user wants to view
	 * @param state If the user is about to remove the time report use
	 * REMOVE_REPORT else use SHOW_USER_REPORT
	 * @return
	 */
	public String showTimeReport(int reportId, int state) {		
		ArrayList<Activity> activities = db.getActivities(reportId);
		TimeReport tr = db.getTimeReport(reportId);
		ProjectGroup pg = db.getProjectGroup(tr.getProjectGroupId());
		StringBuilder sb = new StringBuilder();
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
		sb.append("<TH>Number</TH><TH>Activity</TH>" + 
				"<TH WIDTH=75>D</TH><TH WIDTH=75>I</TH><TH WIDTH=75>F</TH>" + 
					"<TH WIDTH=75>R</TH><TH WIDTH=75>Total time</TH></tr>");
		HashMap<Integer,int[]> table = createTimeReportPresentationTable(activities);
		for (int i = 11; i < 20; i++) {
			int[] values = table.get(i);
			if(values != null) {
				sb.append("<td>" + i + "</td>");
				sb.append("<td>" + Activity.mapActivityNrToString(i) + "</td>");
				int total = 0;
				for (int j = 0; j < values.length; j++) {
					sb.append("<td><i>" + values[j] + "</i></td>");
					total += values[j];
				}
				sb.append("<td>" + total + "</td>");
				sb.append("<tr>");
			}
		}
		//Print activities not associated with documents
		activities = removePrintedActivities(activities);
		for (int i = 0; i < activities.size(); i++) {
			Activity a = activities.get(i);
			sb.append("<tr>");
			sb.append("<td>" + a.getActivityNr() + "</td>");
			sb.append("<TD COLSPAN = 5>" + 
					Activity.mapActivityNrToString(a.getActivityNr()) + "</TD>");
			sb.append("<td>" + a.getTime() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		//TODO: Fix this button to remove a time report after confirmation
		//Create a button if user wants to remove a time report.
		switch(state) {
		case REMOVE_REPORT:
			if(!tr.isSigned()) {
				sb.append(confirmationButton(state,reportId) + "<button onclick=" + 
						formElement("confirmation()") + ">Remove Report</button>");
			}
			break;
		case SHOW_SIGN:
			if(tr.isSigned()) 
				sb.append(confirmationButton(SHOW_SIGN,reportId));
			else if (!tr.isSigned())
				sb.append(confirmationButton(SHOW_UNSIGNED,reportId));
			break;
		}
		return sb.toString();
	}
	
	private HashMap<Integer,int[]> createTimeReportPresentationTable(ArrayList<Activity> activities) {
		HashMap<Integer,int[]> table = new HashMap<Integer,int[]>();
		for (int i = 0; i < activities.size(); i++) {
			Activity a = activities.get(i);
			if(table.containsKey(a.getActivityNr()) && a.getActivityNr() < 20) {
				int[] workTime = table.get(a.getActivityNr());
				workTime[a.mapActivityTypeToInt()] += a.getTime();
			} else if(a.getActivityNr() < 20) {
				int[] workTime = new int[4];
				workTime[a.mapActivityTypeToInt()] += a.getTime();
				table.put(a.getActivityNr(), workTime);
			}
		}
		return table;
	}
	
	private ArrayList<Activity> removePrintedActivities(ArrayList<Activity> activities) {
		ArrayList<Activity> nonPrintedActivities = new ArrayList<Activity>();
		for(int i = 0; i < activities.size(); i++) {
			if(activities.get(i).getActivityNr() > 20) {
				nonPrintedActivities.add(activities.get(i));
			}
		}
		return nonPrintedActivities;
	}
	
	private String confirmationButton(int state, int reportId) {
		StringBuilder sb = new StringBuilder();
		switch(state) {
		case REMOVE_REPORT:
			sb.append("<script> function confirmation(){return confirm('Are you sure you want to remove this report?')}</script>");
			sb.append("<form  method=\"get\" action=\"RemoveTimeReports\" onSubmit=\"confirmation()\">");
			sb.append("<input type=\"submit\" name=\"confirmRemove\" value=\"Remove report\">");
			break;
		case SHOW_UNSIGNED:
			sb.append("<script> function confirmation(){return confirm('Are you sure you want to sign this report?')}</script>");
			sb.append("<form  method=\"get\" action=\"SignTimeReports\" onSubmit=\"confirmation()\">");
			sb.append("<input type=" + formElement("submit") + "name=\"confirmUnsign\" value=\"Sign report\">");
			break;
		case SHOW_SIGN:
			sb.append("<script> function confirmation(){return confirm('Are you sure you want to unsign this report?')}</script>");
			sb.append("<form  method=\"get\" action=\"SignTimeReports\" onSubmit=\"confirmation()\">");
			sb.append("<input type=\"submit\" name=\"confirmSign\" value=\"Unsign report\">");
			break;
		}
		sb.append("<input type=\"hidden\" name=\"reportId\" value=" + formElement(Integer.toString(reportId)) + ">");
		sb.append("</form>");
		return sb.toString();
	}
	
	/**
	 * 
	 * @param reportIds
	 * @param sign
	 * @return
	 */
	public String signOrUnsignReport(String reportId) {
		StringBuilder sb = new StringBuilder();
		//Create dummy TimeReports
		TimeReport tr = db.getTimeReport(Integer.valueOf(reportId));
		ArrayList<TimeReport> timeReports = new ArrayList<TimeReport>();
		timeReports.add(tr);
		boolean success = false;
		if(tr.isSigned())
			success = db.unsignTimeReports(timeReports);
		else
			success = db.signTimeReports(timeReports);
		if(success) {
			sb.append("<h3> The following time report was signed:</h3>");
			sb.append(reportId + "<br>");
		} else {
			return null;
		}
		return sb.toString();
	}

	public String removeTimeReports(String[] reportIds) {
		StringBuilder sb = new StringBuilder();
		boolean success = false;
		for(String rId: reportIds) {
			success = db.removeTimeReport(Integer.valueOf(rId));
			if(!success)
				break;
		}
		if(success) {
			sb.append("<h3> The following time reports were deleted:</h3>");
			for(String rId: reportIds) {
				sb.append(rId + "<br>");
			}
		} else {
			return null;
		}
		return sb.toString();
	}
}
