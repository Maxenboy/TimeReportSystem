package subprojectgroupsmenu;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import database.*;

@WebServlet("/NewProjectGroup")
public class NewProjectGroup extends gui.ProjectGroupsMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProjectGroups group = new ProjectGroups();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession();
			PrintWriter out = response.getWriter();
			out.print(getPageIntro());
			int userPermission = (Integer) session.getAttribute("user_permissions");
			out.append(generateMainMenu(userPermission, request));
			out.print(generateSubMenu(userPermission));
			if (request.getParameter("session") == null) {
				out.print(addProjectGroupForm());
			} else if (request.getParameter("session").equals("success")) {
				out.print(showProjectGroups());
			} else {
				if (request.getParameter("inputname") == null) {
					out.print("<script>$(alert(\"Information inkorrekt inmatad\"))</script>"
							+ addProjectGroupForm());
				} else {
					out.print("<script>$(alert(\"Kunde inte l\u00E4gga till projektgrupp\"))</script>"
							+ addProjectGroupForm());
				}
			}
			out.print(getPageOutro());
		} else {
			response.sendRedirect("LogIn");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			String name = request.getParameter("projectname");
			String startWeek = request.getParameter("startweek");
			String endWeek = request.getParameter("endweek");
			String estimatedHours = request.getParameter("estimatedhours");
			if (validInput(name, startWeek, endWeek)) {
				if (group.createProjectGroup(name, startWeek, endWeek,
						estimatedHours)) {
					response.sendRedirect(request.getRequestURI()
							+ "?session=success");
				} else {
					response.sendRedirect(request.getRequestURI() + "?session=false");
				}
			} else {
				response.sendRedirect(request.getRequestURI()
						+ "?session=false&inputname=bad");
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}

	private boolean validInput(String projectname, String startweek,
			String endweek) {
		boolean notOk = false;
		if (projectname.length() > 10 && Pattern.matches("\\W", projectname)) {
			notOk = true;
		}
		if (Pattern.matches("\\D", startweek)) {
			notOk = true;
		}
		if (Pattern.matches("\\D", endweek)) {
			notOk = true;
		}
		return !notOk;
	}

	private String addProjectGroupForm() {
		String html;
		html = "<p> <form name=" + formElement("input") + "id="
				+ formElement("addprojectgroup");
		html += " method=" + formElement("get");
		html += "<p> Projektnamn: <input type=" + formElement("text")
				+ " projectname=" + formElement("addprojectname") + '>';
		html += "<p> Startvecka: <input type=" + formElement("text")
				+ " startweek=" + formElement("startweek") + '>';
		html += "<p> Slutvecka: <input type=" + formElement("text")
				+ " endweek=" + formElement("endweek") + '>';
		html += "<p> Antal estimerade timmar: <input type=" + formElement("text")
				+ " estimatedhours=" + formElement("estimatedhours") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("Spara") + '>';
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
