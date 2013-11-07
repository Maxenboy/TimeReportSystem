package subprojectgroupsmenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import database.ProjectGroup;
import database.User;

@WebServlet("/AddMemberToProjectGroup")
public class AddMemberToProjectGroup extends gui.ProjectGroupsMenu {

	private String groupName = "";
	private static final long serialVersionUID = -1961915720341016655L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (loggedIn(request)) {
			ProjectGroups groups = new ProjectGroups(db);
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			out.print(getPageIntro());
			int userPermission = (Integer) session
					.getAttribute("user_permissions");
			out.append(generateMainMenu(userPermission, request));
			out.print(generateSubMenu(userPermission));
			if (request.getParameter("thegroup") == null
					&& groupName.equals("")) {
				out.print(showProjectGroups());
			} else {
				if (request.getParameter("reportId") == null) {
					if (groupName.equals("")) {
						groupName = request.getParameter("thegroup");
					}
					ArrayList<User> users = getUsersWithoutProjectGroup();
					
					if (users.isEmpty()) {
						out.print("<script>$(alert(\"Finns inga anv\u00E4ndare utan projektgrupp!\"))</script>");
					} else {
						out.print(groups.showProjectGroup(users));
					}
				} else {
					if (db.getUser(
							Integer.parseInt(request.getParameter("reportId")))
							.getRole() != User.ROLE_ADMIN) {
						if (groups.addUserToProjectGroup(
								db.getUser(
										Integer.parseInt(request
												.getParameter("reportId")))
										.getUsername(), Integer
										.parseInt(groupName))) {
							out.print(groups.showProjectGroup(db
									.getUsers(Integer.parseInt(groupName))));
							groupName = "";
						} else {
							ArrayList<User> users = getUsersWithoutProjectGroup();
							out.print("<script>$(alert(\"Anv\u00E4ndaren \u00E4r redan med i en projektgrupp!\"))</script>"
									+ groups.showProjectGroup(users));
						}
					} else {
						ArrayList<User> users = getUsersWithoutProjectGroup();
						out.print("<script>$(alert(\"Administrat\u00F6rer kan ej vara med i projektgrupper.\"))</script>"
								+ groups.showProjectGroup(users));
					}
				}
			}
			out.print(getPageOutro());
		} else {
			response.sendRedirect("LogIn");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

	}
	
	private ArrayList<User> getUsersWithoutProjectGroup() {
		ArrayList<User> users = new ArrayList<User>();
		for (User u : db.getUsers()) {
			if (u.getProjectGroup() == 0) {
				users.add(u);
			}
		}
		return users;
	}

	private String showProjectGroups() {
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=" + formElement("get") + ">");
		sb.append(buildProjectGroupsTable());
		List<ProjectGroup> groups = db.getProjectGroups();
		for (ProjectGroup g : groups) {
			sb.append("<tr>");
			sb.append("<td>" + g.getProjectName() + "</td>");
			sb.append("<td>" + g.getStartWeek() + "</td>");
			sb.append("<td>" + g.getEndWeek() + "</td>");
			sb.append("<td>" + g.getEstimatedTime() + "</td>");
			if (g.isActive()) {
				sb.append("<td>" + "Aktiv" + "</td>");
			} else {
				sb.append("<td>" + "Inaktiv" + "</td>");
			}
			sb.append("<td>" + createRadio(g.getId()) + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("<input type=" + formElement("submit") + "value="
				+ formElement("V\u00E4lj grupp") + ">");
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
		sb.append("<th>Aktiv</th>");
		sb.append("<th>V\u00E4lj</th>");
		sb.append("</tr>");
		return sb.toString();
	}

	private String createRadio(int id) {
		return "<input type=" + formElement("radio") + "name="
				+ formElement("thegroup") + "value="
				+ formElement(Integer.toString(id)) + ">";
	}

}
