package subTimeReportMenu;

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
	public static final int REMOVE_PRJ_REPORT = 6;
	public static final int REMOVE_REPORT = 7;
	public static final int CHANGE_USER_REPORT = 8;
	public static final int CHANGE_PRJ_REPORT = 9;
	public static final int SHOW_PRJ = 10;
	
	/**
	 * Constructor
	 * @param dataBase The database that the TimeReportGenerator communicates with
	 */
	public TimeReportGenerator(Database dataBase) {
		this.db = dataBase;
	}
	
	private String formElement(String par) {
		return '"' + par + '"';
	}
	
	/**
	 * Helper method for showAllTimeReports, constructs a html-table
	 * @return a html-table
	 */
	private String buildShowTimeReportTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table class=\"table table-bordered table-hover\">");
		sb.append("<tr>");
		sb.append("<th>Rappport ID</th>");
		sb.append("<th>Vecka</th>");
		sb.append("<th>Signerad</th>");
		sb.append("<th>V\u00E4lj</th>");
		sb.append("</tr>");
		return sb.toString();
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
		case SHOW_ALL:
			sb.append("<FORM METHOD=post ACTION="+formElement("ShowTimeReports")+">");
			break;
		case REMOVE_REPORT:
			sb.append("<FORM METHOD=post ACTION="+formElement("RemoveTimeReport")+">");
			break;
		case REMOVE_PRJ_REPORT:
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
		case CHANGE_PRJ_REPORT:
			sb.append("<FORM METHOD=post ACTION="+formElement("ChangeTimeReport")+">");
			break;
		case CHANGE_USER_REPORT:
			sb.append("<FORM METHOD=post ACTION="+formElement("ChangeTimeReport")+">");
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
		sb.append("<INPUT TYPE="+ formElement("submit") + "VALUE=" + formElement("Hämta rapport") +">");
		sb.append("</form>");
		return sb.toString();
	}
	
	/**
	 * helper method for showAllTimeReports, constructs a html-radioButton
	 * @param id, variable id for html-value
	 * @return a html-radioButton
	 */
	private String createRadio(int id) {
		return "<input type=" + formElement("radio") + "name=" + 
				formElement("reportId") + "value=" + formElement(Integer.toString(id))+">";
	}
	
	private String showPrj() {
		ArrayList<ProjectGroup> prjGroups = db.getProjectGroups();
		if(prjGroups.isEmpty())
			return null;
		String html = "<FORM METHOD=POST ACTION=ShowTimeReports>"; 
		html += "<table class=\"table table-bordered table-hover\">";
		html += "<tr>";
		html += "<th>Grupp ID</th>";
		html += "<th>Välj</th>";
		html += "</tr>";
		for(ProjectGroup pg: prjGroups) {
			html += "<tr>";
			html += "<td>" + pg.getId() + "</td>";
			html += "<td> <input type=\"radio\" name=\"prjGroup\" value=" + formElement(Integer.toString(pg.getId())) + "> </td>"; 
			html += "</tr>";
		}
		html += "</table>";
		html += "<INPUT TYPE=\"submit\" VALUE=\"Hämta projektgrupp\">";
		html += "</FORM>";
		return html;
	}
	
	/**
	 * Lists all time reports belonging to a specific ID
	 * @param ID could be project_group_ID or userId
	 * @param state use these different values
	 * SHOW_USER_REPORT show all of one users time reports
	 * SHOW_UNSIGNED show all UNsigned time reports for a project (admin/prj_leader only)
	 * SHOW_SIGNED show all SIGNED time reports for a project (admin/prj_leader only)
	 * REMOVE_PRJ_REPORTS show all UNsigned time reports for a project and wants to REMOVE one of them (admin/prj_leader only)
	 * REMOVE_USER_REPORT show all UNsigned time reports and user wants to REMOVE a time report
	 * CHANGE_USER_REPORT show all UNsigned time reports and user wants to  CHANGE a time report
	 * CHANGE_PRJ_REPORT  show all UNsigned time reports for a project and wants to CHANGE one of them (admin/prj_leader only)
	 * @return html-code 
	 */
	public String showAllTimeReports(int ID, int state) {
		ArrayList<TimeReport> timeReports = new ArrayList<TimeReport>();
		String html = null;
		switch(state) {
		case SHOW_PRJ:
			html = showPrj();
			System.out.println(html);
			break;
		case SHOW_ALL:
			timeReports = db.getTimeReportsForProjectGroupId(ID);
			html = listReports(timeReports,state);
			break;
		case SHOW_USER_REPORT:
			timeReports = db.getTimeReportsForUserId(ID);
			if(timeReports.isEmpty())
				return null;
			html = listReports(timeReports, state);
			break;
		case SHOW_UNSIGNED:
			timeReports = db.getUnsignedTimeReports(ID);
			if(timeReports.isEmpty())
				return null;
			html = listReports(timeReports,state);
			break;
		case SHOW_SIGN:
			timeReports = db.getSignedTimeReports(ID);
			if(timeReports.isEmpty())
				return null;
			html = listReports(timeReports, state);
			break;
		case REMOVE_PRJ_REPORT:
			timeReports = db.getUnsignedTimeReports(ID);
			if(timeReports.isEmpty())
				return null;
			html = listReports(timeReports, state);
			break;
		case REMOVE_USER_REPORT:
			timeReports = db.getTimeReportsForUserId(ID);
			if(timeReports.isEmpty())
				return null;
			html = listReports(timeReports, REMOVE_REPORT);
			break;
		case CHANGE_USER_REPORT:
			timeReports = db.getTimeReportsForUserId(ID);
			if(timeReports.isEmpty())
				return null;
			html = listReports(timeReports,CHANGE_USER_REPORT);
			break;
		case CHANGE_PRJ_REPORT:
			timeReports = db.getUnsignedTimeReports(ID);
			if(timeReports.isEmpty())
				return null;
			html = listReports(timeReports,CHANGE_PRJ_REPORT);
			break;
		}
		return html;
	}
	
	/**
	 * Creates the form where user can fill in an new TimeReport
	 * @return returns a form for the PrintWriter to print.
	 */
	public String showNewTimeForm() {
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=post ACTION=\"NewTimeReport\">");
		sb.append("<TABLE class=\"table table-bordered table-hover\">");
		sb.append("<TR><TD COLSPAN=1><B>Week:</B></TD><TD><INPUT TYPE=\"text\" NAME=\"week\" Value=\"\" SIZE=3></TD></TR>");
		sb.append("<TR><TH>Nummer</TH><TH>Aktivitet</TH><TH WIDTH=75>D</TH><TH WIDTH=75>I</TH><TH WIDTH=75>F</TH><TH WIDTH=75>R</TH></TR>");
		int activityNbr = 11;
		int formField = 0;
		while(activityNbr < 20) {
			sb.append("<TR><TD>" + activityNbr + "</TD><TD>" + Activity.mapActivityNrToString(activityNbr)  + "</TD>");
			for(int i = 0; i < 4; i++) {
				sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(formField)) + " Value=\"\" SIZE=3></I></TD>");
				formField++;
			}
			sb.append("</TR>");
			activityNbr++;
		}
		activityNbr = 21;
		sb.append("<TD BGCOLOR=\"lightgrey\" COLSPAN=6><FONT COLOR=\"lightgrey\">_</FONT></TD>");
		while(activityNbr < 24) {
			sb.append("<TR><TD>" + activityNbr + "</TD><TD COLSPAN=4>"+ Activity.mapActivityNrToString(activityNbr) + "</TD>");
			sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(activityNbr) + " ") +  " Value=\"\" SIZE=3>");
			sb.append("</I></TD></TR>");
			activityNbr++;
		}
		activityNbr = 30;
		sb.append("<TR><TD>" + activityNbr + "</TD><TD COLSPAN=4>"+ Activity.mapActivityNrToString(activityNbr) + "</TD>");
		sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(activityNbr) + " ") +  " Value=\"\" SIZE=3>");
		sb.append("</I></TD></TR>");
		activityNbr = 41;
		while(activityNbr < 45) {
			sb.append("<TR><TD>" + activityNbr + "</TD><TD COLSPAN=4>"+ Activity.mapActivityNrToString(activityNbr) + "</TD>");
			sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(activityNbr)) +  " Value=\"\" SIZE=3>");
			sb.append("</I></TD></TR>");
			activityNbr++;
		}
		activityNbr = 100;
		sb.append("<TR><TD>" + activityNbr + "</TD><TD COLSPAN=4>"+ Activity.mapActivityNrToString(activityNbr) + "</TD>");
		sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(activityNbr)) +  " Value=\"\" SIZE=3>");
		sb.append("</I></TD></TR>");
		sb.append("</TABLE>");
		sb.append("<INPUT TYPE=\"hidden\" NAME=\"FormFields\" Value=\"0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,21 ,22 ,23 ,30 ,41,42,43,44,100,\">");
		sb.append("<INPUT TYPE=\"submit\" VALUE=\"Skicka in tidrapport\">");
		sb.append("</FORM>");
		return sb.toString();
	}
	
	/**
	 * Adds a new timeReport to the database
	 * @param timeReport the new timeReport to be added
	 * @param activities the activities connected to the new time report
	 * @return True if database added the time report, false if e.g. an error occured  
	 */
	public boolean addNewTimeReport(TimeReport timeReport, ArrayList<Activity> activities) {
		return db.addTimeReport(timeReport, activities); 
	}
	
	/**
	 * Returns a form containing the values of a report the user wants to change
	 * @param reportId, the report the user wants to change
	 * @return a form for PrintWriter to print
	 */
	public String showChangeTimeReport(int reportId) {
		TimeReport tr = db.getTimeReport(reportId);
		if(tr.isSigned())
			return null;
		StringBuilder sb = new StringBuilder();
		ArrayList<Activity> activities = db.getActivities(reportId);
		sb.append("<FORM METHOD=post ACTION=\"ChangeTimeReport\">");
		sb.append("<TABLE class=\"table table-bordered table-hover\">");
		sb.append("<TR><TD COLSPAN=1><B>Vecka:</B></TD><TD><INPUT TYPE=\"text\" NAME=\"week\" Value=" + formElement(Integer.toString(tr.getWeek())) + "SIZE=3></TD></TR>");
		sb.append("<TR><TH>Number</TH><TH>Aktivitet</TH><TH WIDTH=75>D</TH><TH WIDTH=75>I</TH><TH WIDTH=75>F</TH><TH WIDTH=75>R</TH></TR>");
		int activityNbr = 11;
		int formField = 0;
		while(activityNbr < 20) {
			sb.append("<TR><TD>" + activityNbr + "</TD><TD>" + Activity.mapActivityNrToString(activityNbr)  + "</TD>");
			for(int i = 0; i < 4; i++) {
				boolean foundExistingActivity = false;
				for(int j = 0; j < activities.size(); j++) {
					Activity a = activities.get(j);
					if(a.getActivityNr() == activityNbr && Activity.mapIntToActivityType(i).equals(a.getActivityType())) {
						foundExistingActivity = true;
						sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(formField)) + " Value=" + formElement(Integer.toString(a.getTime())) + "SIZE=3></I></TD>");
						break;
					}
				}
				if(!foundExistingActivity)
					sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(formField)) + " Value=\"\" SIZE=3></I></TD>");
				formField++;
			}
			sb.append("</TR>");
			activityNbr++;
		}
		activityNbr = 21;
		sb.append("<TD BGCOLOR=\"lightgrey\" COLSPAN=6><FONT COLOR=\"lightgrey\">_</FONT></TD>");
		while(activityNbr < 24) {
			sb.append("<TR><TD>" + activityNbr + "</TD><TD COLSPAN=4>"+ Activity.mapActivityNrToString(activityNbr) + "</TD>");
			boolean foundExistingActivity = false;
			for (int i = 0; i < activities.size(); i++) {
				Activity a = activities.get(i);
				if(a.getActivityNr() == activityNbr) {
					foundExistingActivity = true;
					sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(activityNbr) + " ") + " Value=" + formElement(Integer.toString(a.getTime())) + "SIZE=3></I></TD>");
					break;
				}
			}
			if(!foundExistingActivity)
				sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(activityNbr) + " ") +  " Value=\"\" SIZE=3>");
			sb.append("</I></TD></TR>");
			activityNbr++;
		}
		activityNbr = 30;
		sb.append("<TR><TD>" + activityNbr + "</TD><TD COLSPAN=4>"+ Activity.mapActivityNrToString(activityNbr) + "</TD>");
		boolean foundExistingActivity = false;
		for (int i = 0; i < activities.size(); i++) {
			Activity a = activities.get(i);
			if(a.getActivityNr() == activityNbr) {
				foundExistingActivity = true;
				sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(formField)) + " Value=" + formElement(Integer.toString(a.getTime())) + "SIZE=3></I></TD>");
				break;
			}
		}
		if(!foundExistingActivity)
			sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(activityNbr) + " ") +  " Value=\"\" SIZE=3>");
		sb.append("</I></TD></TR>");
		activityNbr = 41;
		while(activityNbr < 45) {
			sb.append("<TR><TD>" + activityNbr + "</TD><TD COLSPAN=4>"+ Activity.mapActivityNrToString(activityNbr) + "</TD>");
			foundExistingActivity = false;
			for (int i = 0; i < activities.size(); i++) {
				Activity a = activities.get(i);
				if(a.getActivityNr() == activityNbr) {
					foundExistingActivity = true;
					sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(activityNbr)) + " Value=" + formElement(Integer.toString(a.getTime())) + "SIZE=3></I></TD>");
					break;
				}
			}
			if(!foundExistingActivity)
				sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(activityNbr)) +  " Value=\"\" SIZE=3>");
			sb.append("</I></TD></TR>");
			activityNbr++;
		}
		activityNbr = 100;
		sb.append("<TR><TD>" + activityNbr + "</TD><TD COLSPAN=4>"+ Activity.mapActivityNrToString(activityNbr) + "</TD>");
		foundExistingActivity = false;
		for (int i = 0; i < activities.size(); i++) {
			Activity a = activities.get(i);
			if(a.getActivityNr() == activityNbr) {
				foundExistingActivity = true;
				sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(activityNbr)) + " Value=" + formElement(Integer.toString(a.getTime())) + "SIZE=3></I></TD>");
				break;
			}
		}
		if(!foundExistingActivity)
			sb.append("<TD><I><INPUT TYPE=\"text\" NAME=" + formElement(Integer.toString(activityNbr)) +  " Value=\"\" SIZE=3>");
		sb.append("</TABLE>");
		sb.append("<INPUT TYPE=\"hidden\" NAME=\"FormFields\" Value=\"0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,21 ,22 ,23 ,30 ,41,42,43,44,100,\">");
		sb.append("<INPUT TYPE=\"hidden\" NAME=\"reportId\" Value=" + formElement(Integer.toString(reportId)) + ">");
		sb.append("<INPUT TYPE=\"submit\" VALUE=\"Skicka in tidrapport\">");
		sb.append("</FORM>");
		return sb.toString();
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
		sb.append("<table class=\"table table-bordered table-hover\">");
		sb.append("<tr>");
		sb.append("<TD COLSPAN=2><B>Rapport:</B></TD><TD COLSPAN=2>" + reportId + "</TD>");
		sb.append("<TD><B>Week:</B></TD><TD COLSPAN=2>" + tr.getWeek() + "</TD>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<TD COLSPAN=2><B>Projektnamn:</B></TD><TD COLSPAN=2>" + pg.getProjectName() + "</TD>");
		sb.append("<TD><B>Signerad:</B></TD><TD COLSPAN=2>");
		sb.append(tr.isSigned() ? "Y" : "N");
		sb.append("</TD></tr>");
		sb.append("<tr>");
		sb.append("<TH>Number</TH><TH>Aktivitet</TH>" + 
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
			if(!tr.isSigned())
				sb.append(confirmationButton(REMOVE_REPORT,reportId));
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
	/**
	 * Helper method for showTimeReport
	 * @param activities
	 * @return
	 */
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
	
	/**
	 * Helper method for showTimeReport
	 * @param activities
	 * @return
	 */
	private ArrayList<Activity> removePrintedActivities(ArrayList<Activity> activities) {
		ArrayList<Activity> nonPrintedActivities = new ArrayList<Activity>();
		for(int i = 0; i < activities.size(); i++) {
			if(activities.get(i).getActivityNr() > 20) {
				nonPrintedActivities.add(activities.get(i));
			}
		}
		return nonPrintedActivities;
	}
	
	/**
	 * Helper method for showTimeReport
	 * @param state
	 * @param reportId
	 * @return
	 */
	private String confirmationButton(int state, int reportId) {
		StringBuilder sb = new StringBuilder();
		switch(state) {
		case REMOVE_REPORT:
			sb.append("<form  method=\"post\" action=\"RemoveTimeReport\" onclick=\"return confirm('Are you sure you want to remove this report?')\">");
			sb.append("<input type=\"submit\" value=\"Radera tidrapport\" >");
			break;
		case SHOW_UNSIGNED:
			sb.append("<form  method=\"get\" action=\"SignTimeReports\" onclick=\"return confirm('Are you sure you want to sign this report?')\">");
			sb.append("<input type=\"submit\" value=\"Signera tidrapport\" >");
			break;
		case SHOW_SIGN:
			sb.append("<form  method=\"get\" action=\"SignTimeReports\" onclick=\"return confirm('Are you sure you want to unsign this report?')\">");
			sb.append("<input type=\"submit\" value=\"Avsignera tidrapport\">");
			break;
		}
		sb.append("<input type=\"hidden\" name=\"reportId\" value=" + formElement(Integer.toString(reportId)) + ">");
		sb.append("</form>");
		return sb.toString();
	}
	
	/**
	 * Signs or unsigns a report.
	 * @param reportId the report that the user wants to sign/unsign
	 * @return true if success, false if e.g. there occurred an internal error 
	 */
	public boolean signOrUnsignReport(String reportId) {
		//Create dummy TimeReports
		TimeReport tr = db.getTimeReport(Integer.valueOf(reportId));
		ArrayList<TimeReport> timeReports = new ArrayList<TimeReport>();
		timeReports.add(tr);
		boolean success = false;
		if(tr.isSigned())
			success = db.unsignTimeReports(timeReports);
		else
			success = db.signTimeReports(timeReports);

		return success;
	}
	/**
	 * Tells the database to change a time report
	 * @param timeReport the time report the user wants to update
	 * @param activities the activities connected to the time report the user wants to update.
	 * @return true if successful update, false if e.g. there occured an internal error
	 */
	public boolean changeTimeReport(TimeReport timeReport, ArrayList<Activity> activities) {
		return db.updateTimeReport(timeReport, activities);
	}

	/**
	 * Tells the database to remove a time report
	 * @param reportId the report the user wants to remove
	 * @return true if successful removal, false if e.g. there occurred an internal error.
	 */
	public boolean removeTimeReport(String reportId) {
		return db.removeTimeReport(Integer.valueOf(reportId));
	}
}
