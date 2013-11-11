package subTimeReportMenu;

import java.util.ArrayList;
import java.util.HashMap;
import database.*;
/**
 * Denna klass hanterar skapandet av HTML-kod samt har kontakt med databasen för att hantera Tidrapporter.
 * @author martin
 *
 */
public class TimeReportGenerator {
	private Database db;
	public static final int SHOW_ALL_FOR_PRJ = 1;
	public static final int SHOW_USER_REPORT = 2;
	public static final int SHOW_SIGN = 3;
	public static final int SHOW_UNSIGNED = 4;
	public static final int REMOVE_USER_REPORT = 5;
	public static final int REMOVE_PRJ_REPORT = 6;
	public static final int REMOVE_REPORT = 7;
	public static final int CHANGE_USER_REPORT = 8;
	public static final int CHANGE_PRJ_REPORT = 9;
	public static final int SHOW_PRJ = 10;
	public static final int SIGN_PRJ = 11;
	public static final int REMOVE_PRJ = 12;
	public static final int CHANGE_PRJ = 13;
	
	/**
	 * Konstruktor
	 * @param dataBase Databasen som den här klassen kommunicerar med
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
		sb.append("<th>Anv\u00E4ndarnamn</th>");
		sb.append("<th>Rapport ID</th>");
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
		case SHOW_ALL_FOR_PRJ:
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
			User usr = db.getUser(tr.getUserId());
			sb.append("<tr>");
			sb.append("<td>" + usr.getUsername() + "</td>");
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
		sb.append("<INPUT TYPE="+ formElement("submit") + "VALUE=" + formElement("H\u00E4mta rapport") +">");
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
	
	private String showPrj(int state) {
		ArrayList<ProjectGroup> prjGroups = db.getProjectGroups();
		if(prjGroups.isEmpty())
			return null;
		String html = new String();
		switch(state) {
		case SHOW_PRJ:
			html = "<FORM METHOD=POST ACTION=ShowTimeReports>";
			break;
		case SIGN_PRJ:
			html = "<FORM METHOD=POST ACTION=\"SignTimeReports\">";
			break;
		case REMOVE_PRJ:
			html = "<FORM METHOD=POST ACTION=\"RemoveTimeReport\">";
			break;
		case CHANGE_PRJ:
			html = "<FORM METHOD=POST ACTION=\"ChangeTimeReport\">";
		}
		html += "<table class=\"table table-bordered table-hover\">";
		html += "<tr>";
		html += "<th>Grupp ID</th>";
		html += "<th>V\u00E4lj</th>";
		html += "</tr>";
		for(ProjectGroup pg: prjGroups) {
			html += "<tr>";
			html += "<td>" + pg.getProjectName() + "</td>";
			html += "<td> <input type=\"radio\" name=\"prjGroup\" value=" + formElement(Integer.toString(pg.getId())) + "> </td>"; 
			html += "</tr>";
		}
		html += "</table>";
		html += "<INPUT TYPE=\"submit\" VALUE=\"H\u00E4mta projektgrupp\">";
		html += "</FORM>";
		return html;
	}
	
	/**
	 * Listar projektgrupper eller tidrapporter, beroende på inparameter
	 * @param ID, kan vara användarID eller projektgruppsID
	 * @param state följande parametrar finns att välja på
	 * SHOW_ALL_FOR_PRJ visar alla tidrapporter för en projektgrupp
	 * SHOW_USER_REPORT visar alla tidrapporter för en användare
	 * SHOW_SIGN visar alla SIGNERADE tidrapporter för en projektgrupp
 	 * SHOW_UNSIGNED visar alla OSIGNERADE tidrapporter för en projektgrupp
 	 * REMOVE_USER_REPORT visar alla tidrapporter för en användare, för borttagning
 	 * REMOVE_PRJ_REPORT visar alla tidrapporter för en projektgrupp, för borttagning
 	 * REMOVE_REPORT visar en tidrapport, för borttagning
 	 * CHANGE_USER_REPORT visar alla tidrapporter för en användare, vid förändring av tidrapport
 	 * CHANGE_PRJ_REPORT visar alla tidrapporter för en användare, vid förändring av tidrapport
 	 * SHOW_PRJ visa alla projektgrupper
 	 * SIGN_PRJ visa alla projektgrupper, när tidrapporter ska signeras
 	 * REMOVE_PRJ visa alla projektgrupper, när tidrapporter ska tas bort
 	 * CHANGE_PRJ visa alla projektgrupper, när tidrapporter ska ändras
	 * @return html-code 
	 */
	public String showAllTimeReports(int ID, int state) {
		ArrayList<TimeReport> timeReports = new ArrayList<TimeReport>();
		String html = null;
		switch(state) {
		case SHOW_PRJ:
			html = showPrj(state);
			break;
		case SIGN_PRJ:
			html = showPrj(state);
			break;
		case REMOVE_PRJ:
			html = showPrj(state);
			break;
		case CHANGE_PRJ:
			html = showPrj(state);
			break;
		case SHOW_ALL_FOR_PRJ:
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
			timeReports = db.getTimeReportsForProjectGroupId(ID);
			if(timeReports.isEmpty())
				return null;
			html = listReports(timeReports,CHANGE_PRJ_REPORT);
			break;
		}
		return html;
	}
	
	/**
	 * Skapar ett formulär där användaren kan fylla i en ny tidrapport
	 * @return returns HTML-kod till PrintWriter
	 */
	public String showNewTimeForm() {
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=post ACTION=\"NewTimeReport\">");
		sb.append("<TABLE class=\"table table-bordered table-hover\">");
		sb.append("<TR><TD COLSPAN=1><B>Veckonummer:</B></TD><TD><INPUT TYPE=\"text\" NAME=\"week\" Value=\"\" SIZE=3></TD></TR>");
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
	 * Lägger till en ny tidrapport i databasen
	 * @param timeReport tidrapporten som ska läggas till
	 * @param activities aktiviteterna som är kopplade till tidrapportern
	 * @return True om databasen lyckades lägga till tidrapporten, false om den misslyckades (t.ex. vid internt fel)  
	 */
	public boolean addNewTimeReport(TimeReport timeReport, ArrayList<Activity> activities) {
		return db.addTimeReport(timeReport, activities); 
	}
	
	/**
	 * Returnerar ett formulär som innehåller värden från den tidrapport som användaren vill ändra.
	 * @param reportId, rapporten som användaren vill ändra på
	 * @return HTML-kod som PrintWriter sedan kan skriva ut.
	 */
	public String showChangeTimeReport(int reportId) {
		TimeReport tr = db.getTimeReport(reportId);
		if(tr.isSigned())
			return null;
		User usr = db.getUser(tr.getUserId());
		StringBuilder sb = new StringBuilder();
		ArrayList<Activity> activities = db.getActivities(reportId);
		sb.append("<FORM METHOD=post ACTION=\"ChangeTimeReport\">");
		sb.append("<TABLE class=\"table table-bordered table-hover\">");
		sb.append("<TR><TD COLSPAN=1><B>Vecka:</B></TD><TD><INPUT TYPE=\"text\" NAME=\"week\" Value=" + formElement(Integer.toString(tr.getWeek())) + "SIZE=3></TD><TD COLSPAN=2><B>Anv\u00E4ndarnamn:</B></TD><TD COLSPAN=2>" + usr.getUsername() + "</TD></TR>");
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
		sb.append("<INPUT TYPE=\"submit\" VALUE=\"\u00C4ndra tidrapport\">");
		sb.append("</FORM>");
		return sb.toString();
	}
	
	/**
	 * Visar all information associerad med en tidrapport
	 * @param reportId ID på den tidrapport som användaren vill visa
	 * @param state Olika states finns att tillgå
	 * REMOVE_REPORT om användaren vill ta bort tidrapporten
	 * SHOW_USER_REPORT om användaren bara vill se tidrapporten
	 * SHOW_SIGN om användaren vill signera/avsignera tidrapporten (enbart för admin/projektledare)
	 * @return
	 */
	public String showTimeReport(int reportId, int state) {		
		ArrayList<Activity> activities = db.getActivities(reportId);
		TimeReport tr = db.getTimeReport(reportId);
		User usr = db.getUser(tr.getUserId());
		ProjectGroup pg = db.getProjectGroup(tr.getProjectGroupId());
		StringBuilder sb = new StringBuilder();
		//Table setup
		sb.append("<table class=\"table table-bordered table-hover\">");
		sb.append("<tr>");
		sb.append("<TD COLSPAN=2><B>Anv\u00E4ndarnamn:</B></TD><TD COLSPAN=2>" + usr.getUsername() + "</TD>");
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
			sb.append("<form  method=\"post\" action=\"RemoveTimeReport\" onclick=\"return confirm('\u00C4r du s\u00E4ker p\u00E5 att du vill ta bort den h\u00E4r rapporten?')\">");
			sb.append("<input type=\"submit\" value=\"Radera tidrapport\" >");
			break;
		case SHOW_UNSIGNED:
			sb.append("<form  method=\"post\" action=\"SignTimeReports\" onclick=\"return confirm('\u00C4r du s\u00E4ker p\u00E5 att du vill signera den h\u00E4r rapporten?')\">");
			sb.append("<input type=\"submit\" value=\"Signera tidrapport\" >");
			break;
		case SHOW_SIGN:
			sb.append("<form  method=\"post\" action=\"SignTimeReports\" onclick=\"return confirm('\u00C4r du s\u00E4ker p\u00E5 att du vill avsignera den h\u00E4r rapporten')\">");
			sb.append("<input type=\"submit\" value=\"Avsignera tidrapport\">");
			break;
		}
		sb.append("<input type=\"hidden\" name=\"reportId\" value=" + formElement(Integer.toString(reportId)) + ">");
		sb.append("</form>");
		return sb.toString();
	}
	
	/**
	 * Signerar eller avsignerar en tidrapport, genom att kontakta datbasen
	 * @param reportId ID på den tidrapport som användaren vill signera/avsignera
	 * @return true om signeringen lyckas, false om signeringen misslyckas, t.ex. vid internt fel.
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
	 * Talar om för databasen att den ska ändra en tidrapport
	 * @param timeReport tidrapporten som ska ändras
	 * @param activities Aktiviterna som är kopplade till tidrapporten
	 * @return true om det lyckas att ändra tidrapporten, false om den misslyckas.
	 */
	public boolean changeTimeReport(TimeReport timeReport, ArrayList<Activity> activities) {
		//change to correct owner of timereport
		TimeReport tr = db.getTimeReport(timeReport.getId());
		timeReport.setUserId(tr.getUserId());
		timeReport.setProjectGroupId(tr.getProjectGroupId());
		return db.updateTimeReport(timeReport, activities);
	}

	/**
	 * Kontaktar databasen och ber den ta bort en tidrapport
	 * @param reportId tidrapporten som ska tas bort
	 * @return true om borttagningen lyckades, false om det ej lyckades (t.ex. vid internt fel.)
	 */
	public boolean removeTimeReport(String reportId) {
		return db.removeTimeReport(Integer.valueOf(reportId));
	}
}
