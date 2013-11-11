package statistics;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.ProjectGroup;


@WebServlet("/Statistics")

public class Statistics extends gui.StatisticsMenu {
	
	public Statistics() {

	}

	/**
	 * doGet-metoden anropas d\u00E5 anv\u00E4ndaren f\u00E5rs\u00E5ker n\u00E5 statistics-sidan.
	 * Om anv\u00E4ndaren \u00E4r en administrat\u00E5r s\u00E5 visas ett formul\u00E4r d\u00E4r anv\u00E4ndaren kan v\u00E4lja projektgrupp som man vill se data f\u00E5r.
	 * Om anv\u00E4ndaren \u00E4r en projektledare, d\u00E5 visas fyra mulitple-select-l\u00E5dor.
	 * Om anv\u00E4ndaren \u00E4r en vanlig anv\u00E4ndare, en multiple-select-box f\u00E5r att v\u00E4lja vecka visas.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			PrintWriter out = response.getWriter();
			
			HttpSession session = request.getSession(true);
			
			//testSetSessionData(session); // TEST! TA BORT!
			
			int userPermission = (Integer) session.getAttribute("user_permissions");
			int projectGroupId = (Integer) session.getAttribute("project_group_id");
			
			out.append(getPageIntro());
			out.append(generateMainMenu(userPermission, request));
			out.append(generateSubMenu(userPermission));
			switch(userPermission) {
			case PERMISSION_ADMIN: // Administrator gets to choose project group.
				out.append(projectGroupForm());
				break;
			case PERMISSION_PROJ_LEADER: // Project leader chooses filters from the available users, activities, roles and weeks. 
				out.append(printFilter(db.getStatisticsFilter(projectGroupId), userPermission));
				break;
			case PERMISSION_OTHER_USERS: // Ordinary user chooses week. 
				out.append(printFilter(db.getStatisticsFilter(projectGroupId), userPermission));
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


	/**
	 * doPost-metoden n\u00E5s efter att man har skickat data via formul\u00E4r fr\u00E5n statistics-sidan.
	 * 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
	// There are a few different scenarios that need to be handled depending on which data was submitted.
	// 
	// Administrator could have submitted project group or requested filters. 
	// Project leader could have submitted requested filters.
	// User could have submitted weeks.
	// 
	// If filters/weeks have been submitted the statistics will be shown in table- and graphical form.
	 
	
		if (loggedIn(request)) {
			PrintWriter out = response.getWriter();
			HttpSession session = request.getSession(true);
			int userPermission = (Integer) session.getAttribute("user_permissions");
			
			String projectGroups[] = request.getParameterValues("projectgroup");
			String users[] = request.getParameterValues("users");
			String roles[] = request.getParameterValues("roles");
			String activities[] = request.getParameterValues("activities");
			String weeks[] = request.getParameterValues("weeks");
			int projectGroupId = (Integer) session.getAttribute("project_group_id");
			
			
			out.append(getPageIntro());
			out.append(generateMainMenu(userPermission, request));
			out.append(generateSubMenu(userPermission));
			
			if(userPermission == 1) { //Admin
				
				if(projectGroups != null) { // The administrator has submitted data to select project group.
					String projectGroup = projectGroups[0];
					Cookie cookie = new Cookie("projectGroup", projectGroup);
					response.addCookie(cookie);
					out.append(printFilter(db.getStatisticsFilter(Integer.parseInt(projectGroup)), userPermission));
					out.append(getPageOutro());
				}
				else { // Admin has submitted filters. 
					String projectGroup= getCookieValue(request);
					if(projectGroup != null) {
						HashMap<String, ArrayList<String>> stats = db.getStatistics(Integer.parseInt(projectGroup), toStringArrayList(users), toIntegerArrayList(roles), toIntegerArrayList(activities), toIntegerArrayList(weeks));
						out.append(printGraph(stats));
						out.append(printTable(stats));
						
						out.append(getPageOutro());
						
					} else { //
						out.append("Det verkar vara ett problem med cookies");
					}
				}
			} else if(userPermission == 2) { // The project leader has submitted filter-data 
				HashMap<String, ArrayList<String>> stats = db.getStatistics(projectGroupId, toStringArrayList(users), toIntegerArrayList(roles), toIntegerArrayList(activities), toIntegerArrayList(weeks));
				out.append(printGraph(stats));
				out.append(printTable(stats));
				
				out.append(getPageOutro());
				
				
			} else if(userPermission == 4) { // User has submitted weeks.
				
				String[] username = new String[1];
				
				username[0] = (String) session.getAttribute("name"); 
				HashMap<String, ArrayList<String>> stats = db.getStatistics(projectGroupId, toStringArrayList(username), null, null, toIntegerArrayList(weeks));
				out.append(printGraph(stats));
				out.append(printTable(stats));
				
				out.append(getPageOutro());
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}

	/**
	 * Metoden skriver ut ett formul\u00E4r d\u00E4r administrat\u00E5ren f\u00E5r v\u00E4lja vilken projektgrupp denne vill se statistik f\u00E5r.
	 * 
	 * @return
	 */
	private String projectGroupForm() {
		StringBuilder sb = new StringBuilder();
		ArrayList<ProjectGroup> pg = db.getProjectGroups();
		Iterator<ProjectGroup> itr = pg.iterator();

		sb.append("<form method='POST'> Var v\u00E4nlig v\u00E4lj vilken projektgrupp du vill visa statistik f\u00F6r.<br />"
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
	 * Genererar en str\u00E4ng med html- och javaskriptkod som skapar en graf n\u00E4r denna skrivs ut.
	 * @param stats HashMap med nycklarna  "user", "role", "activity", "week" och "time". 
	 * och ArrayList som v\u00E4rde.
	 * @return Str\u00E4ng inneh\u00E5llandes html- och javaskriptkod som skapar en graf d\u00E5 denna skrivs ut. Om stats \u00E4r null returneras en felstr\u00E4ng.
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
				      + "['Vecka', 'Minuter'],"
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
				        + " title: 'Minuter per vecka',"
				        + "  hAxis: {title: 'Vecka', titleTextStyle: {color: 'black'}}"
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
	 * Skriver ut ett fomrul\u00E4r d\u00E4r anv\u00E4ndaren kan v\u00E4lja hur man vill filtrera statistik-datan.
	 * @param table Tar emot m\u00E5jliga filter.
	 * @param userPermission Access-niv\u00E5n som anv\u00E4ndaren har.
	 * @return html-str\u00E4ng som skapar formul\u00E4r d\u00E4r anv\u00E4ndarne kan filtrera statstik-datan..
	 */
	private String printFilter(HashMap<String, ArrayList<String>> table, int userPermission) {
		StringBuilder sb = new StringBuilder();
		if(userPermission == 1 || userPermission == 2) { //admin // PL
			ArrayList<String> users = table.get("user");
			Iterator<String> itr = users.iterator();

			sb.append("<form method='POST'> "
					+ "<table class='table table-bordered table-hover'><tr><td> Anv\u00E4ndare <br /><select name='users' multiple>"); 
			sb.append("<option selected> Alla </option>");
			
			while(itr.hasNext()) { // Iterates through the list with usernames to build up the first part of form.				
				String username = itr.next();
				sb.append("<option value='"
						+ username + "'>" + username +
						"</option>");
			}

			sb.append("</select></td><td>"); // The first multiple select box with users is now done. On to the next one, roles.

			ArrayList<String> roles = table.get("role");
			itr = roles.iterator();

			sb.append("Roller <br /><select name='roles' multiple>");
			sb.append("<option selected> Alla </option>");
			while(itr.hasNext()) {
				String role = itr.next();
				sb.append("<option value='"
						+ role + "'>" + translateRole(Integer.parseInt(role)) +
						"</option>");				
			}
			sb.append("</select></td><td>"); // Role multiple select box is now done. On to the next one, Activity.

			ArrayList<String> activities= table.get("activity");
			itr = activities.iterator();

			sb.append("Aktiviteter <br /><select name='activities' multiple>");
			sb.append("<option selected> Alla </option>");
			while(itr.hasNext()) {
				String activity = itr.next();
				sb.append("<option value='"
						+ activity + "'>" + translateActivity(activity) +
						"</option>");				
			}
			sb.append("</select></td><td>"); // Activity multiple select box is now done. On to the last one, week.			

			ArrayList<String> weeks= table.get("week");
			itr = weeks.iterator();

			sb.append("Veckor <br /><select name='weeks' multiple>");
			sb.append("<option selected> Alla </option>");
			while(itr.hasNext()) {
				String week = itr.next();
				sb.append("<option value='"
						+ week + "'>" + "Vecka " + week +
						"</option>");				
			}
			sb.append("</select></td></tr></table>"
					+ "<br /><input type='SUBMIT' value='Visa' />"
					+ "</form>");  // form is now complete.



		} else if(userPermission == 4) { // User
			sb.append("<form method='POST'>");
			ArrayList<String> weeks = table.get("week");
			Iterator<String>  itr= weeks.iterator();

			sb.append("Veckor <br /><select name='weeks' multiple>");
			sb.append("<option selected>Alla</option>");
			while(itr.hasNext()) {
				String week = itr.next();
				sb.append("<option value='"
						+ week + "'>" + week +
						"</option>");				
			}
			sb.append("</select>"
					+ "<br /><input type='SUBMIT' value='Visa' />"
					+ "</form>");  // form is now complete.	

		} else {
			sb.append("N\u00E5got blev fel. F\u00F6rs\u00F6k att logga in igen. ");
		}
		return sb.toString();

	}


	/**
	 * Metod som g\u00E5r om en array till en arraylist.
	 * @param array
	 * @return arraylist.
	 */
	private ArrayList<String> toStringArrayList(String[] array) {
		if(array == null) {
			return null;
		}
		if(array[0].equals("Alla")) {
			return null;
		}
		
		ArrayList<String> arrayList  = new ArrayList<String>();
		for(int i = 0; i < array.length; i++) {
			arrayList.add(array[i]);
		}
		return arrayList;
	}
	/**
	* @param listOfWeeks. ArrayLista med alla veckonummer.
	* @return int st\u00E5rsta elementet i arrayen. 
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
	 * G\u00E5r om Array till ArrayList
	 * 
	 * @param string-array 
	 * @return arraylist
	 */
	private ArrayList<Integer> toIntegerArrayList(String[] array) {
		if(array == null) {
			return null;
		}
		if(array[0].equals("Alla")) {
			return null;
		}
		
		ArrayList<Integer> arrayList  = new ArrayList<Integer>();
		for(int i = 0; i < array.length; i++) {
			arrayList.add(Integer.parseInt(array[i]));
		}
		return arrayList;
	}

	/**
	 * S\u00E5ker efter en cookie med namnet "projectGroup". Returnerar v\u00E4rdet av cookien om denna finns, annars null.
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
	 * Skapar en str\u00E4ng med html- och javaskriptkod som skapar en tabell d\u00E5 denna skrivs ut.
	 * @param stats HashMap med nycklarna  "user", "role", "activity", "week" och "time" 
	 * och ArrayList som v\u00E4rde.
	 * @return String inneh\u00E5llandes html- och javascriptkod som skapar en tabell d\u00E5 denna skrivs ut.
	 */	
	private String printTable(HashMap<String, ArrayList<String>> stats) {
		if(stats.get("time").isEmpty()) {
			return("Hittade ingen tidrapport med dessa filter.");
		}
		
		StringBuilder htmlTable = new StringBuilder();
		
		htmlTable.append(
					"<script type='text/javascript' src='https://www.google.com/jsapi'></script>"
				    + "<script type='text/javascript'>"
				     + "google.load('visualization', '1', {packages:['table']});"
				      + "google.setOnLoadCallback(drawTable);"
				      + "function drawTable() {"
				       + "var data = new google.visualization.DataTable();"
				       + "data.addColumn('string', 'Anv\u00E4ndarnamn');"
				       + "data.addColumn('string', 'Roll');"
				       + "data.addColumn('string', 'Aktivitet');"
				       + "data.addColumn('string', 'Vecka');"
				       + "data.addColumn('string', 'Tid(minuter)');"
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
			htmlTable.append("['" + username + "',  '" + translateRole(Integer.parseInt(role)) + "', '" + translateActivity(activity_nr) + "', '" + week + "', '" + time + "']");
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
		htmlTable.append("<br /><br />");
		return htmlTable.toString();
	}	
	
	/**
	 * \u00E5vers\u00E4tter rollnummer till str\u00E4ng.
	 * @param role
	 * @return rollen som str\u00E4ng.
	 */
	private String translateRole(int role) {
		switch(role) {
			case 1:
				return("Administrat\u00F6r");
			case 2:
				return("Projektledare");
			case 4: 
				return("Systemgrupp");
			case 5:
				return("Systemgruppsledare");
			case 6:
				return("Utvecklingsgrupp");
			case 7:
				return("Testgrupp");
			case 8:
				return("Testledare");
			default:
				return("Ok\u00E4nd roll");
		}
	}
	
	private String translateActivity(String activity) {
		switch(activity) {
		case "11":
			return("SDP");
		case "12":
			return("SRS");
		case "13":
			return("SVVS");
		case "14":
			return("STLDD");
		case "15":
			return("SVVI");
		case "16":
			return("SDDD");
		case "17":
			return("SVVR");
		case "18":
			return("SSD");
		case "19":
			return("FINAL_REPORT");
		case "21":
			return("FUNTION_TEST");
		case "22":
			return("SYSTEM_TEST");
		case "23":
			return("REGRESSION_TEST");
		case "30":
			return("MEETING");
		case "41":
			return("LECTURE");
		case "42":
			return("EXERCISE");
		case "43":
			return("COMPUTER_EXERCISE");
		case "44":
			return("HOME_STUDIES");
		case "100":
			return("OTHER");
		case "A":
			return("OTHER");
		case "U":
			return("DEVELOPMENT");
		case "I":
			return("INFORMAL_REVIEW");
		case "F":
			return("FORMAL_REVIEW");
		case "O":
			return("REWORK");
		default:
			return("Unknown Activity");
		}
	}
	

/*
	private void testSetSessionData(HttpSession session) {
		session.setAttribute("user_Permissions", 2);
		session.setAttribute("project_group_id", 1);
		session.setAttribute("username","andsve");
	}*/
}