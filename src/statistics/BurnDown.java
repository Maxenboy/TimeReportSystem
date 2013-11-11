package statistics;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
			case PERMISSION_ADMIN: // Administrator gets to choose project group.
				out.append(projectGroupForm());
				break;
			case PERMISSION_PROJ_LEADER:
				out.append(printBurnDown(db.getTimePerWeek(projectGroupId), projectGroupId, db.getProjectGroup(projectGroupId)));
				break;
			case PERMISSION_OTHER_USERS:
				out.append(printBurnDown(db.getTimePerWeek(projectGroupId), projectGroupId, db.getProjectGroup(projectGroupId)));
				break;
			case PERMISSION_WITHOUT_ROLE:
				out.append("<p style='color: red;'>Du \u00E4r inte tilldelad n\u00E5gon roll i projektet och har d\u00E4rf\u00F6r inte tillg\u00E5ng till den h\u00E4r funktionen. Kontakta din projektledare.</p>");
				break;
			default:
				out.append("<p style='color: red;'>Ov\u00E4ntad anv\u00E4ndarr\u00E4ttighetsniv\u00E5.</p>");
			}		
			out.append(getPageOutro());
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
				out.append("ERROR - Ingen projektgrupp vald av administrat\u00F6r." );
			}
			out.append(getPageOutro());
		} else {
			response.sendRedirect("LogIn");
		}
	}
	
	/**
	 * Genererar HTML-kod d\u00E4r admin f\u00F6r v\u00E4lja projektgrupp att se BurnDown f\u00F6r.
	 * 
	 * 
	 * @return Str\u00E4ng med HTML-kod som inneh\u00E5ller formul\u00E4ret
	 */
	private String projectGroupForm() {
		StringBuilder sb = new StringBuilder();
		ArrayList<ProjectGroup> pg = db.getProjectGroups();
		Iterator<ProjectGroup> itr = pg.iterator();

		sb.append("<form method='POST'> Var v\u00E4nlig v\u00E4lj vilken projektgrupp du vill visa BurnDown f\u00F6r.<br />"
				+ "<select name=projectgroup>");
		while(itr.hasNext()) {
			int projectgroup = itr.next().getId();
			sb.append("<option value='"
					+ projectgroup + "'>" + db.getProjectGroup(projectgroup).getProjectName() +
					"</option>");
		}
		sb.append("</select> <br /><input type='SUBMIT' value='Visa' /> </form>");

		return sb.toString();
	}
	
	
	
	/**
	 * Skapar en str\u00E4ng med HTML och javaskriptkod som skapar en BurnDowngraf n\u00E4r denna skrivs ut.
	 * @param timePerWeek. En HashMap med vecka som nyckel och tid som v\u00E4rde. Nyckeln totalProjektTime finns ocks\u00E5 som inneh\u00E5ller uppskattad total projekttid.
	 * projectGroupId. Int.
	 * projectGroup. ProjectGroup.
	 * @return Returnerar en str\u00E4ng html- och javascriptkod som skapar en BurnDown-graf n\u00E4r str\u00E4ngen skrivs ut. Om total f\u00E5rv\u00E4ntad projekttid \u00E4r satt till noll s\u00E5 returnerar den str\u00E4ngen "F\u00E5rv\u00E4ntad projekttid satt till noll."
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

}
