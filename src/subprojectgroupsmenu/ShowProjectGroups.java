package subprojectgroupsmenu;

import gui.ProjectGroupsMenu;

import java.io.*;
import java.util.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import database.ProjectGroup;

@WebServlet("/ShowProjectGroups")
public class ShowProjectGroups extends ProjectGroupsMenu {
	
	private static final long serialVersionUID = -6869459061396740611L;
	/**
	 * doGet-metoden anropas alla g\u00E5nger d\u00E5 en adminstrat\u00F6r blir presenterad med en lista av alla projektgrupper i systemet.
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
				out.print(showProjectGroups() + getPageOutro());
			} else {
				response.sendRedirect("");
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
		sb.append("<table class=\"table table-bordered table-hover\">");
		sb.append("<tr>");
		sb.append("<th>Projektgrupp</th>");
		sb.append("<th>Starvecka</th>");
		sb.append("<th>Slutvecka</th>");
		sb.append("<th>Estimerat antal timmar</th>");
		sb.append("</tr>");
		return sb.toString();
	}
}
