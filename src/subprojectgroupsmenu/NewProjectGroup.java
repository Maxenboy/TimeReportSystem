package subprojectgroupsmenu;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;

import database.*;

public class NewProjectGroup {

	ProjectGroups group = new ProjectGroups();
	Database db = new Database();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		if(request.getParameter("session") == null) {
			out.print(getPageIntro() + addProjectGroupForm());
		} else if(request.getParameter("session").equals("sucess")) {
			out.print(getPageIntro() + showProjectGroups());
		} else {
			out.print(getPageIntro() + "$(alert(\"Incorrect input.\"))" + addProjectGroupForm());
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		String name = request.getParameter("projectname");
		String startWeek = request.getParameter("startweek");
		String endWeek = request.getParameter("endweek");
		String estimatedHours = request.getParameter("estimatedhours");
		if(group.createProjectGroup(name, startWeek, endWeek, estimatedHours)) {
			response.sendRedirect(request.getRequestURI() + "session=sucess");
		} else {
			response.sendRedirect(request.getRequestURI() + "session=false");
		}
	}

	private String addProjectGroupForm() {
		String html;
		html = "<p> <form name=" + formElement("input") + "id="
				+ formElement("addprojectgroup") + '>';
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
		html += "</form>" + "</body></html>";
		return html;
	}
	
	private String showProjectGroups() {
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=post ACTION=" + formElement("ShowUsers") + ">");
		sb.append(buildProjectGroupsTable());
		List<ProjectGroup> groups = db.getProjectGroups();
		for (ProjectGroup g: groups) {
			sb.append("<tr>");
			sb.append("<td>" + g.getProjectName()+ "</td>");
			sb.append("<td>" + g.getStartWeek() + "</td>");
			sb.append("<td>" + g.getEndWeek() + "</td>");
			sb.append("<td>" + g.getEstimatedTime() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("</form>");
		sb.append("</body></html>");
		return sb.toString();
	}
	
	private String buildProjectGroupsTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table border=" + formElement("1") + ">");
		sb.append("<tr>");
		sb.append("<th>Project Group</th>");
		sb.append("<th>Start Week</th>");
		sb.append("<th>End Week</th>");
		sb.append("<th>Estimated hours</th>");
		sb.append("</tr>");
		return sb.toString();
	}
	
	private String formElement(String par) {
		return '"' + par + '"';
	}

	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> The Base Block System </title>" + getPageJs()
				+ "</head>" + "<body>";
		return intro;
	}

	private String getPageJs() {
		return "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>";
	}

	private String getPageOutro() {
		String outro = "</body></html>";
		return outro;
	}
}
