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
public class BurnDown extends gui.StatisticsMenu {
	public BurnDown() {
		// Konstruktor
	}
	
	
	/**
	 * @throws IOException  
	 * 
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			PrintWriter out = response.getWriter();
			
			HttpSession session = request.getSession(true);
			//testSetSessionData(session);
			int userPermission = (Integer) session.getAttribute("user_permissions");
			int projectGroupId = (Integer) session.getAttribute("project_group_id");
			
			out.append(getPageIntro());
			out.append(generateMainMenu(userPermission, request));
			out.append(generateSubMenu(userPermission));
			
			switch(userPermission) {
			case 1: // Administrator gets to choose project group.
				out.append(projectGroupForm());
				out.append(getPageOutro());
				break;
			case 2:
				out.append(printBurnDown(db.getTimePerWeek(projectGroupId), projectGroupId, db.getProjectGroup(projectGroupId)));
				out.append(getPageOutro());
				break;
			case 4:
				out.append(printBurnDown(db.getTimePerWeek(projectGroupId), projectGroupId, db.getProjectGroup(projectGroupId)));
				out.append(getPageOutro());
				break;
			default:
				out.append("Unexpected user permission level.");	
				out.append(getPageOutro());
				break;
			}		
		} else {
			response.sendRedirect("LogIn");
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			PrintWriter out = response.getWriter();
			HttpSession session = request.getSession(true);
			
			String[] projectGroup = request.getParameterValues("projectgroup");
			int userPermission = (Integer) session.getAttribute("user_permissions");
			
			out.append(getPageIntro());
			out.append(generateMainMenu(userPermission, request));
			out.append(generateSubMenu(userPermission));
			
			if(projectGroup[0] != null) {
				out.append(printBurnDown(db.getTimePerWeek(Integer.parseInt(projectGroup[0])), Integer.parseInt(projectGroup[0]), db.getProjectGroup(Integer.parseInt(projectGroup[0]))));
			} else {
				out.append("ERROR - No project group chosen by administrator." );
			}
			out.append(getPageOutro());
		} else {
			response.sendRedirect("LogIn");
		}
	}
	
	/**
	 * Genererar HTML-kod d�r admin f�r v�lja projektgrupp att se BurnDown f�r.
	 * 
	 * 
	 * @return Str�ng med HTML-kod som inneh�ller formul�ret
	 */
	private String projectGroupForm() {
		StringBuilder sb = new StringBuilder();
		//Database db = new Database();
		ArrayList<ProjectGroup> pg = db.getProjectGroups();
		Iterator<ProjectGroup> itr = pg.iterator();

		sb.append("<form method='POST'> Var v\u00E4nlig v\u00E4lj vilken projektgrupp du vill visa BurnDown f\u00F6r.<br />"
				+ "<select name=projectgroup>");
		while(itr.hasNext()) {
			int projectgroup = itr.next().getId();
			sb.append("<option value='"
					+ projectgroup + "'>" + projectgroup +
					"</option>");
		}
		sb.append("</select> <br /><input type='SUBMIT' value='Skicka' /> </form>");

		return sb.toString();
	}
	
	
	
	/**
	 * Creates a string with html and javascript code that creates a burndown graph when printed out.
	 * @param timePerWeek. A HashMap with week as key and time as value. There is also the key "totalProjectTime" which contains the total project time.
	 * projectGroupId. Int.
	 * projectGroup. ProjectGroup.
	 * @return A string containing html and javascript that creates the burndown graph when printed out. If the total project time is 0, it returns the string "F�rv�ntad projekttid �r satt till noll."
	 */	
	private String printBurnDown(HashMap<String, Integer> timePerWeek, int projectGroupId, ProjectGroup projectGroup) {
		if(timePerWeek.get("totalProjectTime") == 0) {
			return("F\u00F6rv\u00E4ntad projekttid \u00E4r satt till noll.");
		}
		
		StringBuilder htmlBurnDown = new StringBuilder();

		htmlBurnDown.append(		
		"<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
		  + "<script type=\"text/javascript\">"
		   + "google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
		   + "google.setOnLoadCallback(drawChart);"
		  + "function drawChart() {"
		    + "var data = google.visualization.arrayToDataTable(["
		   + "['Vecka', 'Verklig tid kvar', 'F\u00F6rv\u00E4ntad tid kvar'],"
		); 
		
		double estimatedProjectTime = projectGroup.getEstimatedTime();
		double numberOfWeeksProject = projectGroup.getEndWeek() - projectGroup.getStartWeek() + 1; 
		double totalTimeSpent = 0;
		int week = 0;

		htmlBurnDown.append("['" + week + "',  " + estimatedProjectTime + ", " + estimatedProjectTime + "],");
		week++;
		
		while(week <= numberOfWeeksProject) {
			double expectedTimeLeft = (1-week/numberOfWeeksProject)*estimatedProjectTime;
			if (timePerWeek.containsKey(Integer.toString(week))) {
			totalTimeSpent += timePerWeek.get(Integer.toString(week))/60;
			}
			double realTimeLeft = estimatedProjectTime - totalTimeSpent;
			htmlBurnDown.append("['" + week + "',  " + realTimeLeft + ", " + expectedTimeLeft + "]");
			if (week < numberOfWeeksProject) {
			htmlBurnDown.append(",");
			}	
			week++;
		}
				
		htmlBurnDown.append(    
		"]);"

		     + "var options = {"
		       + "title: 'Burndown' "
		      + "};"

		      + "var chart = new google.visualization.LineChart(document.getElementById('chart_div'));"
		      + "chart.draw(data, options);"
		    + "}"
		  + "</script>"
		 + "<div id=\"chart_div\" style=\"width: 900px; height: 500px;\"></div>"
		);
		return htmlBurnDown.toString();
	}		
/*
	private void testSetSessionData(HttpSession session) {
		session.setAttribute("user_permissions", 1);
		session.setAttribute("project_group_id", 1);
		session.setAttribute("username","andsve");
	}*/

}
