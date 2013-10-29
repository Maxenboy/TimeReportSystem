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
	private Database db = new Database();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		out.print(generateMainMenu((Integer) session
				.getAttribute("user_permissions")));
		out.print(generateSubMenu((Integer) session
				.getAttribute("user_permissions")));
		if (request.getParameter("session") == null) {
			out.print(addProjectGroupForm());
		} else if (request.getParameter("session").equals("sucess")) {
			out.print(showProjectGroups());
		} else {
			if (request.getParameter("inputname") == null) {
				out.print("<script>$(alert(\"Information entered incorrectly.\"))</script>"
						+ addProjectGroupForm());
			} else {
				out.print("<script>$(alert(\"Couldn't add project group.\"))</script>"
						+ addProjectGroupForm());
			}
		}
		out.print(getPageOutro());

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession(true);
		String name = request.getParameter("projectname");
		String startWeek = request.getParameter("startweek");
		String endWeek = request.getParameter("endweek");
		String estimatedHours = request.getParameter("estimatedhours");
		if (validInput(name, startWeek, endWeek)) {
			if (group.createProjectGroup(name, startWeek, endWeek,
					estimatedHours)) {
				response.sendRedirect(request.getRequestURI()
						+ "session=sucess");
			} else {
				response.sendRedirect(request.getRequestURI() + "session=false");
			}
		} else {
			response.sendRedirect(request.getRequestURI()
					+ "session=false&inputname=bad");
		}
	}

	private boolean validInput(String projectname, String startweek,
			String endweek) {
		boolean notOk = false;
		if (projectname.length() > 10 && !Pattern.matches("\\w", projectname)) {
			notOk = true;
		}
		if (!Pattern.matches("\\d", startweek)) {
			notOk = true;
		}
		if (!Pattern.matches("\\d", endweek)) {
			notOk = true;
		}
		return !notOk;
	}

	private String addProjectGroupForm() {
		String html;
		html = "<p> <form name=" + formElement("input") + "id="
				+ formElement("addprojectgroup");
		html += " method=" + formElement("get");
		html += "<p> Project name: <input type=" + formElement("text")
				+ " projectname=" + formElement("addprojectname") + '>';
		html += "<p> Start week: <input type=" + formElement("text")
				+ " startweek=" + formElement("startweek") + '>';
		html += "<p> End week: <input type=" + formElement("text")
				+ " endweek=" + formElement("endweek") + '>';
		html += "<p> Estimated hours: <input type=" + formElement("text")
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
		sb.append("<th>Project Group</th>");
		sb.append("<th>Start Week</th>");
		sb.append("<th>End Week</th>");
		sb.append("<th>Estimated hours</th>");
		sb.append("</tr>");
		return sb.toString();
	}

	private String getPageJs() {
		return "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>";
	}

}
