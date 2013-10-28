package subprojectgroupsmenu;

import java.io.*;
import java.util.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import database.Database;
import database.ProjectGroup;

@WebServlet("/ShowProjectGroups")
public class ShowProjectGroups extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6869459061396740611L;
	Database db = new Database();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		out.print(getPageIntro() + showProjectGroups());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {

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
				+ "<head><title> The Base Block System </title>"
				+ "</head>" + "<body>";
		return intro;
	}
}
