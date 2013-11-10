package subprojectgroupsmenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.ProjectGroup;
import database.User;

@WebServlet("/HandleProjectLeader")
public class HandleProjectLeader extends gui.ProjectGroupsMenu {

	private static final long serialVersionUID = 2692792293983643296L;
	private String groupName = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession();
			int userPermission = (Integer) session.getAttribute("user_permissions");
			if (userPermission == PERMISSION_ADMIN) {
				PrintWriter out = response.getWriter();
				out.print(getPageIntro());
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
						ArrayList<User> users = db.getUsers(Integer
								.parseInt(groupName));
						if (users.isEmpty()) {
							out.print("<script>$(alert(\"Finns inga anv\u00E4ndare i projektgruppen!\"))</script>");
						} else {
							out.print(showProjectGroup(users));
						}
					} else {
						HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
						if (db.getUser(
								Integer.parseInt(request.getParameter("reportId")))
								.getRole() == User.ROLE_PROJECT_LEADER) {
							map.put(Integer.parseInt(request
									.getParameter("reportId")), User.ROLE_NO_ROLE);
						} else {
							map.put(Integer.parseInt(request
									.getParameter("reportId")),
									User.ROLE_PROJECT_LEADER);
						}
						db.setUserRoles(map);
						groupName = "";
						out.print(showProjectGroups());
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
	
	private String showProjectGroup(ArrayList<User> users) {
		if (users.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=" + formElement("get") + "onsubmit=\"return confirm('\u00C4r du s\u00E4ker?')\"" +">");
		sb.append(buildShowUsersInGroupTable());
		for (User u : users) {
			sb.append("<tr>");
			sb.append("<td>" + u.getUsername() + "</td>");
			sb.append("<td>" + db.getProjectGroup(u.getProjectGroup()).getProjectName() + "</td>");
			sb.append("<td>" + translateRole(u.getRole()) + "</td>");
			sb.append("<td>" + createRadio2(u.getId()) + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("<INPUT TYPE=" + formElement("submit") + "VALUE="
				+ formElement("Toggla Projektledarroll") + ">");
		sb.append("</form>");
		return sb.toString();
	}
	private String translateRole(int role) {
		switch (role) {
		case 1:
			return ("Administrat\u00F6r");
		case 2:
			return ("Projektledare");
		case 4:
			return ("Systemgrupp");
		case 5:
			return ("Systemgruppsledare");
		case 6:
			return ("Utvecklingsgrupp");
		case 7:
			return ("Testgrupp");
		case 8:
			return ("Testgruppsledare");
		default:
			return ("Utan roll");
		}
	}
	private String buildShowUsersInGroupTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table class=\"table table-bordered table-hover\"");
		sb.append("<tr>");
		sb.append("<th>Anv\u00E4ndarnamn</th>");
		sb.append("<th>Projektgrupp</th>");
		sb.append("<th>Roll</th>");
		sb.append("<th>V\u00E4lj</th>");
		sb.append("</tr>");
		return sb.toString();
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
	
	private String createRadio2(int id) {
		return "<input type=" + formElement("radio") + "name="
				+ formElement("reportId") + "value="
				+ formElement(Integer.toString(id)) + ">";
	}

}
