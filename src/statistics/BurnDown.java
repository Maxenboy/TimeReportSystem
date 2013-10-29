package statistics;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Database;
import database.ProjectGroup;


@WebServlet("/BurnDown")
public class BurnDown extends gui.StatisticsMenu { // extenda servlet eller statisticsmenu??
	public BurnDown() {
		// Konstruktor
	}
	
	
	/**
	 * @throws IOException  
	 * 
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Database db = new Database();
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(true);
		int userPermission = (Integer) session.getAttribute("user_Permissions");
		int projectGroupId = (Integer) session.getAttribute("project_group_id");

		out.append(getPageIntro());
		out.append(generateMainMenu(userPermission));
		out.append(generateSubMenu(userPermission));

		switch(userPermission) {
		case 1: // Administrator gets to choose project group.
			out.append(projectGroupForm());
			out.append(getPageOutro());
			break;
		case 2:
			out.append(printBurnDown(db.getTimePerWeek(projectGroupId), projectGroupId));
			out.append(getPageOutro());
			break;
		case 4:
			out.append(printBurnDown(db.getTimePerWeek(projectGroupId), projectGroupId));
			out.append(getPageOutro());
			break;
		default:
			out.append("Unexpected user permission level.");	
			out.append(getPageOutro());
			break;
		}		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Database db = new Database();
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);

		String[] projectGroup = request.getParameterValues("projectgroup");
		int userPermission = (Integer) session.getAttribute("user_Permissions");
		
		out.append(getPageIntro());
		out.append(generateMainMenu(userPermission));
		out.append(generateSubMenu(userPermission));

		if(projectGroup[0] != null) {
			out.append(printBurnDown(db.getTimePerWeek(Integer.parseInt(projectGroup[0])), Integer.parseInt(projectGroup[0])));
		} else {
			out.append("ERROR - No project group chosen by administrator." );
		}
		out.append(getPageOutro());
	}
	
	/**
	 * Genererar HTML-kod där admin får välja projektgrupp att se BurnDown för.
	 * 
	 * 
	 * @return Sträng med HTML-kod som innehåller formuläret
	 */
	private String projectGroupForm() {
		StringBuilder sb = new StringBuilder();
		Database db = new Database();
		ArrayList<ProjectGroup> pg = db.getProjectGroups();
		Iterator<ProjectGroup> itr = pg.iterator();

		sb.append("<form method='POST'> Select project group<br />"
				+ "<select name=projectgroup>");
		while(itr.hasNext()) {
			int projectgroup = itr.next().getId();
			sb.append("<option value='"
					+ projectgroup + "'>" + projectgroup +
					"</option>");
		}
		sb.append("</select> <br /><input type='SUBMIT' value='Submit' /> </form>");

		return sb.toString();
	}
	
	
	/**
	 * Genererar html-kod som innehåller BurnDown-plotten.
	 * @param burnDownData innehåller ***
	 * @return Sträng som innehåller HTML-kod som visar BurnDown plotten.
	 */
	private String printBurnDown(HashMap<String, Integer> timePerWeek, int projectGroupId) {
		if(timePerWeek.get("totalProjectTime") == 0) {
			return("Excpected total projet time is set to zero");
		}
		
		StringBuilder htmlBurnDown = new StringBuilder();

		htmlBurnDown.append(		
		"<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
		  + "<script type=\"text/javascript\">"
		   + "google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
		   + "google.setOnLoadCallback(drawChart);"
		  + "function drawChart() {"
		    + "var data = google.visualization.arrayToDataTable(["
		   + "['Week', 'Real time left', 'Expected time left'],"
		); 
		
		double totalProjectTime = timePerWeek.get("totalProjectTime");
		double numberOfWeeks = numberOfWeeks(timePerWeek);
		double totalTimeSpent = 0;
		int week = 0;

		htmlBurnDown.append("['" + week + "',  " + totalProjectTime + ", " + totalProjectTime + "],");
		week++;
		
		while(week <= numberOfWeeks) {
			double expectedTimeLeft = (1-week/numberOfWeeks)*totalProjectTime;			
			totalTimeSpent += timePerWeek.get(Integer.toString(week));
			double realTimeLeft = totalProjectTime - totalTimeSpent;

			htmlBurnDown.append("['" + week + "',  " + realTimeLeft + ", " + expectedTimeLeft + "]");
			if (week < numberOfWeeks) {
				htmlBurnDown.append(",");
			}		
			week++;
		}
				
		htmlBurnDown.append(    
		"]);"

		     + "var options = {"
		       + "title: 'Burndown for projectID " + projectGroupId + "'"
		      + "};"

		      + "var chart = new google.visualization.LineChart(document.getElementById('chart_div'));"
		      + "chart.draw(data, options);"
		    + "}"
		  + "</script>"
		 + "<div id=\"chart_div\" style=\"width: 900px; height: 500px;\"></div>"
		);
		return htmlBurnDown.toString();
	}		


	
	/**
	 * @param timePerWeek. A HashMap with week as key and time as value. There is also the key "totalProjectTime" which contains the total project time.
	 * @return int The biggest week number timePerWeek contains as key.
	 */	
	private int numberOfWeeks(HashMap<String, Integer> timePerWeek) {
		int numberOfWeeks = 0;
		int week = 1;

		while(timePerWeek.containsKey(Integer.toString(week))) {
			week++;
			numberOfWeeks++;
		}		
		return numberOfWeeks; 
	}


}
