package statistics;
import java.util.*;
import java.io.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.*;


@WebServlet("/Statistics")

public class Statistics extends HttpServlet { // extenda servlet eller statisticsmenu??
	public Statistics() {

	}


	/**
	 * The doGet method is called when the user gets directed to the statistics-page. 
	 * If the user is an administrator a form is shown where the user selects which project group to get data from.
	 * If the user is project-leader, four multiple-select-boxes are shown.
	 * If the user is just an ordinary user, a multiple-select-box to select week is shown.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Database db = new Database();
		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession(true);
		
		testSetSessionData(session);
		
		int userPermission = (Integer) session.getAttribute("user_Permissions");
		int projectGroupId = (Integer) session.getAttribute("project_group_id");

		switch(userPermission) {
		case 1: // Administrator gets to choose project group.
			out.append(projectGroupForm());
			break;
		case 2: // Project leader chooses filters from the available users, activities, roles and weeks. 
			out.append(printFilter(db.getStatisticsFilter(projectGroupId), userPermission));
			break;
		case 4: // Ordinary user chooses week. 
			out.append(printFilter(db.getStatisticsFilter(projectGroupId), userPermission));
			break;
		}

	}


	/**
	 * You reach the doPost method after submitting data from the statistics page.
	 * There are a few different scenarios that need to be handled depending on which data was submitted.
	 * 
	 * Administrator could have submitted project group or requested filters. 
	 * Project leader could have submitted requested filters.
	 * User could have submitted weeks.
	 * 
	 * If filters/weeks have been submitted the statistics will be shown in table- and graphical form.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Database db = new Database();
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);
		int userPermission = (Integer) session.getAttribute("user_Permissions");

		String projectGroups[] = request.getParameterValues("projectgroup");
		String users[] = request.getParameterValues("users");
		String roles[] = request.getParameterValues("roles");
		String activities[] = request.getParameterValues("activities");
		String weeks[] = request.getParameterValues("weeks");
		int projectGroupId = (Integer) session.getAttribute("project_group_id");

		if(userPermission == 1) { //Admin

			if(projectGroups != null) { // The administrator has submitted data to select project group.
				String projectGroup = projectGroups[0];
				Cookie cookie = new Cookie("projectGroup", projectGroup);
				response.addCookie(cookie);
				out.append(printFilter(db.getStatisticsFilter(Integer.parseInt(projectGroup)), userPermission));
			}
			else { // Admin has submitted filters. 
				String projectGroup= getCookieValue(request);
				if(projectGroup != null) {
					HashMap<String, ArrayList<String>> stats = db.getStatistics(Integer.parseInt(projectGroup), toStringArrayList(users), toIntegerArrayList(roles), toIntegerArrayList(activities), toIntegerArrayList(weeks));
					out.append(printGraph(stats));
					out.append(printTable(stats));
					
				} else { //
					out.append("There seems to be a problem with the cookies");
				}
			}
		} else if(userPermission == 2) { // The project leader has submitted filter-data 
			HashMap<String, ArrayList<String>> stats = db.getStatistics(projectGroupId, toStringArrayList(users), toIntegerArrayList(roles), toIntegerArrayList(activities), toIntegerArrayList(weeks));
			out.append(printGraph(stats));
			out.append(printTable(stats));

			
		} else if(userPermission == 4) { // User has submitted weeks.

			String[] username = new String[1];
			username[0] = (String) session.getAttribute("username"); // kolla att username != null? 
			HashMap<String, ArrayList<String>> stats = db.getStatistics(projectGroupId, toStringArrayList(username), null, null, toIntegerArrayList(weeks));
			out.append(printGraph(stats));
			out.append(printTable(stats));
		}
	}

	/**
	 * This method prints the form where the administrator gets to choose project group to show statistics for.
	 * 
	 * @return
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
	 * Creates a string with html and javascript code that creates a graph when printed out.
	 * @param stats HashMap with keys "user", "role", "activity", "week" and "time". 
	 * and ArrayList as value.
	 * @return String containing html and javascript that creates the graph when printed out. If stats is equal to null, it returns the string "No, graph to show. Using another filer might solve this.".
	 */	
	private String printGraph(HashMap<String, ArrayList<String>> stats) {		
		if(stats.get("time").isEmpty()) {
			return "";//"No, graph to show. Using another filter might solve this.");
		}
		
		StringBuilder htmlGraph = new StringBuilder();
		
		htmlGraph.append(
								 
				   "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
				   + "<script type=\"text/javascript\">"
				    + "google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
				     + "google.setOnLoadCallback(drawChart);"
				    + "function drawChart() {"
				       + "var data = google.visualization.arrayToDataTable(["
				      + "['Week', 'Hours'],"
				    );
		
		ArrayList<String> listOfWeeks = stats.get("week");		
		ArrayList<String> listOfTime = stats.get("time");
		ArrayList<Integer> timePerWeek = new ArrayList<Integer>();		
		
		for (int i = 0; i < getLastWeek(listOfWeeks); i++) {
			timePerWeek.add(0);
		}
		
		for(int i = 0; i < listOfWeeks.size(); i++) {
			int week = Integer.parseInt(listOfWeeks.get(i));
			int time = Integer.parseInt(listOfTime.get(i));
			timePerWeek.set(week - 1, time + timePerWeek.get(week - 1));
		}
			
		for(int i = 0; i < timePerWeek.size(); i++) {
			int week = i + 1;
			htmlGraph.append("['" + week + "'," + timePerWeek.get(i) + "]");
			if (week < timePerWeek.size()) {
				htmlGraph.append(",");
			}
		}
		
		htmlGraph.append(
				"]);"
				        + "var options = {"
				        + " title: 'Hours per week',"
				        + "  hAxis: {title: 'Week', titleTextStyle: {color: 'black'}}"
				       + " };"
				       + "var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));"
				      + "chart.draw(data, options);"
				      + "}"
				    + "</script>"
				 
				  
				  + "<div id=\"chart_div\" style=\"width: 900px; height: 500px;\"></div>"
	
			
				);
			
		return htmlGraph.toString();		
	}

	/**
	 * Prints the form from where the user can choose how to filter the statistics-data.
	 * @param table Receives available filters. 
	 * @param userPermission The permission level of the user.
	 * @return
	 */
	private String printFilter(HashMap<String, ArrayList<String>> table, int userPermission) {
		StringBuilder sb = new StringBuilder();
		if(userPermission == 1 || userPermission == 2) { //admin // PL
			ArrayList<String> users = table.get("user");
			Iterator<String> itr = users.iterator();

			sb.append("<form method='POST'> "
					+ "<table><tr><td> Users <br /><select name='users' multiple  style='width=100px'>"); 
			
			while(itr.hasNext()) { // Iterates through the list with usernames to build up the first part of form.				
				String username = itr.next();
				sb.append("<option value='"
						+ username + "'>" + username +
						"</option>");
			}

			sb.append("</select></td><td>"); // The first multiple select box with users is now done. On to the next one, roles.

			ArrayList<String> roles = table.get("role");
			itr = roles.iterator();

			sb.append("Roles <br /><select name='roles' multiple  style='width=100px'>");
			while(itr.hasNext()) {
				String role = itr.next();
				sb.append("<option value='"
						+ role + "'>" + role +
						"</option>");				
			}
			sb.append("</select></td><td>"); // Role multiple select box is now done. On to the next one, Activity.

			ArrayList<String> activities= table.get("activity");
			itr = activities.iterator();

			sb.append("Activities <br /><select name='activities' multiple  style='width=100px'>");
			while(itr.hasNext()) {
				String activity = itr.next();
				sb.append("<option value='"
						+ activity + "'>" + activity +
						"</option>");				
			}
			sb.append("</select></td><td>"); // Activity multiple select box is now done. On to the last one, week.			

			ArrayList<String> weeks= table.get("week");
			itr = weeks.iterator();

			sb.append("Weeks <br /><select name='weeks' multiple style='width=100px'>");
			while(itr.hasNext()) {
				String week = itr.next();
				sb.append("<option value='"
						+ week + "'>" + "Vecka " + week +
						"</option>");				
			}
			sb.append("</select></td></tr></table>"
					+ "<br /><input type='SUBMIT' value='Submit' />"
					+ "</form>");  // form is now complete.



		} else if(userPermission == 4) { // User
			sb.append("<form method='POST'>");
			ArrayList<String> weeks = table.get("week");
			Iterator<String>  itr= weeks.iterator();

			sb.append("Weeks <br /><select name='weeks' multiple style='width=100px'>");
			while(itr.hasNext()) {
				String week = itr.next();
				sb.append("<option value='"
						+ week + "'>" + week +
						"</option>");				
			}
			sb.append("</select>"
					+ "<br /><input type='SUBMIT' value='Submit' />"
					+ "</form>");  // form is now complete.	

		} else {
			sb.append("Something went wrong. Are you logged in?");
		}
		return sb.toString();

	}


	/**
	 * Converts array into an arraylist
	 * @param array
	 * @return
	 */
	private ArrayList<String> toStringArrayList(String[] array) {
		if(array == null) {
			return null;
		}
		
		ArrayList<String> arrayList  = new ArrayList<String>();
		for(int i = 0; i < array.length; i++) {
			arrayList.add(array[i]);
		}
		return arrayList;
	}
	/**
	* @param listOfWeeks. ArrayList of all weeks numbers.
	* @return int The biggest week number listOfWeeks contains.
	*/
	private int getLastWeek(ArrayList<String> listOfWeeks) {
		int lastWeek = 0;
		
		for(int i = 0; i < listOfWeeks.size(); i++) {
			int week = Integer.parseInt(listOfWeeks.get(i));
			if (week > lastWeek) {
				lastWeek = week;
			}
		}
		return lastWeek;
	}


	/**
	 * Converts array into an arraylist
	 * 
	 * @param string-array 
	 * @return arraylist
	 */
	private ArrayList<Integer> toIntegerArrayList(String[] array) {
		if(array == null) {
			return null;
		}
		
		ArrayList<Integer> arrayList  = new ArrayList<Integer>();
		for(int i = 0; i < array.length; i++) {
			arrayList.add(Integer.parseInt(array[i]));
		}
		return arrayList;
	}

	/**
	 * Searches for a cookie with the name "projectGroup". Returns the value of this cookie if found, otherwise null.
	 * @param request
	 * @return
	 */
	private String getCookieValue(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		for(int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if(cookie.getName().equals("projectGroup")) {
				return cookie.getValue();
			}
		}
		return null;
	}

	/**
	 * Creates a string with html and javascript code that creates a table when printed out.
	 * @param stats HashMap with keys "user", "role", "activity", "week" and "time" 
	 * and ArrayList as value.
	 * @return String containing html and javascript that creates the table when printed out. If stats is equal to null, it returns the string "No, table to show. Using another filer might solve this.".
	 */	
	private String printTable(HashMap<String, ArrayList<String>> stats) {
		if(stats.get("time").isEmpty()) {
			return("No table or graph to show. Using another filter might solve this.");
		}
		
		StringBuilder htmlTable = new StringBuilder();
		
		htmlTable.append(
					"<script type='text/javascript' src='https://www.google.com/jsapi'></script>"
				    + "<script type='text/javascript'>"
				     + "google.load('visualization', '1', {packages:['table']});"
				      + "google.setOnLoadCallback(drawTable);"
				      + "function drawTable() {"
				       + "var data = new google.visualization.DataTable();"
				       + "data.addColumn('string', 'Username');"
				       + "data.addColumn('string', 'Role');"
				       + "data.addColumn('string', 'Activity');"
				       + "data.addColumn('string', 'Week');"
				       + "data.addColumn('string', 'Time');"
				       + "data.addRows(["
					);
		
		ArrayList<String> listOfUsernames = stats.get("username");		
		ArrayList<String> listOfRoles = stats.get("role");
		ArrayList<String> listOfActivity_nr = stats.get("activity_nr");				
		ArrayList<String> listOfWeeks = stats.get("week");		
		ArrayList<String> listOfTime = stats.get("time");
		
		for(int i = 0; i < listOfUsernames.size(); i++) {
			String username = listOfUsernames.get(i);
			String role = listOfRoles.get(i);
			String activity_nr = listOfActivity_nr.get(i);
			String week = listOfWeeks.get(i);
			String time = listOfTime.get(i);	
			htmlTable.append("['" + username + "',  '" + role + "', '" + activity_nr + "', '" + week + "', '" + time + "']");
			if (i + 1 < listOfUsernames.size()) {
				htmlTable.append(",");
			}
		}
						
		htmlTable.append(
						"]);"
				        + "var table = new google.visualization.Table(document.getElementById('table_div'));"
				        + "table.draw(data, {showRowNumber: false});"
				      + "}"
				    + "</script>"
				   + "<div id='table_div'></div>"
		);
		return htmlTable.toString();
	}	
}