package subprojectgroupsmenu;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import database.*;

@WebServlet("/NewProjectGroup")
public class NewProjectGroup extends gui.ProjectGroupsMenu {
	private static final long serialVersionUID = 1L;
	private ProjectGroups group = new ProjectGroups(db);
	/**
	 * doGet-metoden anropas alla g\u00E5nger d\u00E5 en adminstrat\u00F6r vill skapa en projektgrupp.
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession();
			int userPermission = (Integer) session.getAttribute("user_permissions");
			if (userPermission == PERMISSION_ADMIN) {
				PrintWriter out = response.getWriter();
				out.print(getPageIntro());
				out.append(generateMainMenu(userPermission, request));
				out.print(generateSubMenu(userPermission));
				String name = request.getParameter("projectname");
				String startWeek = request.getParameter("startweek");
				String endWeek = request.getParameter("endweek");
				String estimatedHours = request.getParameter("estimatedhours");
				if (name == null || startWeek == null || endWeek == null
						|| estimatedHours == null) {
					out.print(addProjectGroupForm());
				} else {
					if (validInput(name, startWeek, endWeek)) {
						if (group.createProjectGroup(name, startWeek, endWeek,
								estimatedHours)) {
							out.print(showProjectGroups());
						} else {
							out.print("<script>$(alert(\"Kunde inte l\u00E4gga till projektgrupp\"))</script>"
									+ addProjectGroupForm());
						}
					} else {
						out.print("<script>$(alert(\"Information inkorrekt inmatad\"))</script>"
								+ addProjectGroupForm());
					}
				}
				out.print(getPageOutro());
			} else {
				response.sendRedirect("");
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
	}

	private boolean validInput(String projectname, String startweek,
			String endweek) {
		boolean ok = false;
		if (projectname.length() < 10 && Pattern.matches("[a-zA-Z0-9]*", projectname)) {
			if (Pattern.matches("\\d", startweek)) {
				if (Pattern.matches("\\d", endweek)) {
					ok = true;
				}
			}
		}
		return ok;
	}


	private String addProjectGroupForm() {
		String html;
		html = "<form name=" + formElement("input") + "action="
				+ formElement("NewProjectGroup") + "method="
				+ formElement("get") + ">";
		html += "Gruppnamn: <input type=" + formElement("text") + " name="
				+ formElement("projectname") + "><br>";
		html += "Startvecka: <input type=" + formElement("text")
				+ " name=" + formElement("startweek") + "><br>";
		html += "Slutvecka: <input type=" + formElement("text") + " name="
				+ formElement("endweek") + "><br>";
		html += "Estimerade timmar: <input type=" + formElement("text")
				+ " name=" + formElement("estimatedhours") + "><br>";
		html += "<input type=" + formElement("submit") + " value="
				+ formElement("Spara") + ">";
		html += "</form>";
		return html;
	}

	private String showProjectGroups() {
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=post ACTION=" + formElement("ShowUsers") + ">");
		sb.append(buildProjectGroupsTable());
		List<ProjectGroup> groups = db.getProjectGroups();
		for (ProjectGroup g : groups) {
			sb.append("<tr>");
			sb.append("<td>" + g.getProjectName() + "</td>");
			sb.append("<td>" + g.getStartWeek() + "</td>");
			sb.append("<td>" + g.getEndWeek() + "</td>");
			sb.append("<td>" + g.getEstimatedTime() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("</form>");
		return sb.toString();
	}

	private String buildProjectGroupsTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table class=\"table table-bordered table-hover\"" + ">");
		sb.append("<tr>");
		sb.append("<th>Projektgrupp</th>");
		sb.append("<th>Startvecka</th>");
		sb.append("<th>Slutvecka</th>");
		sb.append("<th>Antal estimerade timmar</th>");
		sb.append("</tr>");
		return sb.toString();
	}

}
