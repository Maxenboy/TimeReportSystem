package subProjectMembersMenu;

import gui.ProjectMembersMenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import database.ProjectGroup;
import database.User;

@WebServlet("/HandleProjectRoles")
public class HandleProjectRoles extends ProjectMembersMenu {

	private static final long serialVersionUID = 1L;
	private ProjectMembers members;
	private String groupName = "";
	private int state = 1;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession();
			PrintWriter out = response.getWriter();
			out.print(getPageIntro());
			int userPermission = (Integer) session
					.getAttribute("user_permissions");
			out.append(generateMainMenu(userPermission, request));
			out.print(generateSubMenu(userPermission));
			if ((Integer) session.getAttribute("role") == 1) {
				if (request.getParameter("thegroup") == null
						&& groupName.equals("")) {
					out.print(showProjectGroups());
				} else {
					if (state == 1) {
						if (groupName.equals("")) {
							groupName = request.getParameter("thegroup");
						}
						ArrayList<User> users = db.getUsers(Integer
								.parseInt(groupName));
						if (users.isEmpty()) {
							out.print("<script>$(alert(\"Finns inga användare i projektgruppen!\"))</script>");
						} else {
							out.print(showProjectGroup(users));
							state = 2;
						}
					} else {
						ArrayList<User> users = db.getUsers(Integer
								.parseInt(groupName));
						HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
						for (User u : users) {
							if (request.getParameter("" + u.getId()) != null
									&& Integer.parseInt(request.getParameter(""
											+ u.getId())) != u.getRole()) {
								map.put(u.getId(), Integer.parseInt(request
										.getParameter("" + u.getId())));
							}
						}
						db.setUserRoles(map);
						groupName = "";
						out.print(showProjectGroups());
					}
				}
			} else {
					ArrayList<User> users = db.getUsers((Integer) session
							.getAttribute("project_group_id"));
					HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
					for (User u : users) {
						if (request.getParameter("" + u.getId()) != null
								&& Integer.parseInt(request.getParameter(""
										+ u.getId())) != u.getRole()) {
							map.put(u.getId(), Integer.parseInt(request
									.getParameter("" + u.getId())));
						}
					}
					db.setUserRoles(map);
					users = db.getUsers((Integer) session
							.getAttribute("project_group_id"));
					out.print(showProjectGroup(users));
			}
			out.print(getPageOutro());
		} else {
			response.sendRedirect("LogIn");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (groupName != "") {
			response.sendRedirect(request.getRequestURI() + "&changed=true");
		}
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
				+ formElement("Välj grupp") + ">");
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

	private String createDropDown(int id, int uId) {
		String html;
		switch (id) {
		case 2:
			html = "<select name=\"" + uId + "\">"
					+ "<option value=\"2\" selected>Projektledare</option>"
					+ "<option value=\"3\">Utan roll</option>"
					+ "<option value=\"4\">Systemgrupp</option>"
					+ "<option value=\"5\">Systemledare</option>"
					+ "<option value=\"6\">Utvecklingsgrupp</option>"
					+ "<option value=\"7\">Testgrupp</option>"
					+ "<option value=\"8\">Testledare</option>" + "</select>";
			break;
		case 3:
			html = "<select name=\"" + uId + "\">"
					+ "<option value=\"2\">Projektledare</option>"
					+ "<option value=\"3\" selected>Utan roll</option>"
					+ "<option value=\"4\">Systemgrupp </option>"
					+ "<option value=\"5\">Systemledare</option>"
					+ "<option value=\"6\">Utvecklingsgrupp</option>"
					+ "<option value=\"7\">Testgrupp</option>"
					+ "<option value=\"8\">Testledare</option>" + "</select>";
			break;
		case 4:
			html = "<select name=\"" + uId + "\">"
					+ "<option value=\"2\">Projektledare</option>"
					+ "<option value=\"3\">Utan roll</option>"
					+ "<option value=\"4\" selected>Systemgrupp </option>"
					+ "<option value=\"5\">Systemledare</option>"
					+ "<option value=\"6\">Utvecklingsgrupp</option>"
					+ "<option value=\"7\">Testgrupp</option>"
					+ "<option value=\"8\">Testledare</option>" + "</select>";
			break;
		case 5:
			html = "<select name=\"" + uId + "\">"
					+ "<option value=\"2\">Projektledare</option>"
					+ "<option value=\"3\">Utan roll</option>"
					+ "<option value=\"4\">Systemgrupp </option>"
					+ "<option value=\"5\" selected>Systemledare</option>"
					+ "<option value=\"6\">Utvecklingsgrupp</option>"
					+ "<option value=\"7\">Testgrupp</option>"
					+ "<option value=\"8\">Testledare</option>" + "</select>";
			break;
		case 6:
			html = "<select name=\"" + uId + "\">"
					+ "<option value=\"2\">Projektledare</option>"
					+ "<option value=\"3\">Utan roll</option>"
					+ "<option value=\"4\">Systemgrupp </option>"
					+ "<option value=\"5\">Systemledare</option>"
					+ "<option value=\"6\" selected>Utvecklingsgrupp</option>"
					+ "<option value=\"7\">Testgrupp</option>"
					+ "<option value=\"8\">Testledare</option>" + "</select>";
			break;
		case 7:
			html = "<select name=\"" + uId + "\">"
					+ "<option value=\"2\">Projektledare</option>"
					+ "<option value=\"3\">Utan roll</option>"
					+ "<option value=\"4\">Systemgrupp </option>"
					+ "<option value=\"5\">Systemledare</option>"
					+ "<option value=\"6\">Utvecklingsgrupp</option>"
					+ "<option value=\"7\" selected>Testgrupp</option>"
					+ "<option value=\"8\">Testledare</option>" + "</select>";
			break;
		case 8:
			html = "<select name=\"" + uId + "\">"
					+ "<option value=\"2\">Projektledare</option>"
					+ "<option value=\"3\">Utan roll</option>"
					+ "<option value=\"4\">Systemgrupp </option>"
					+ "<option value=\"5\">Systemledare</option>"
					+ "<option value=\"6\">Utvecklingsgrupp</option>"
					+ "<option value=\"7\">Testgrupp</option>"
					+ "<option value=\"8\" selected>Testledare</option>" + "</select>";
			break;
		default:
			html = "";
			break;
		}
		return html;
	}

	private String showProjectGroup(ArrayList<User> users) {
		if (users.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=" + formElement("get")
				+ "onsubmit=\"return confirm('Är du säker?')\"" + ">");
		sb.append(buildShowUsersInGroupTable());
		for (User u : users) {
			sb.append("<tr>");
			sb.append("<td>" + u.getUsername() + "</td>");
			sb.append("<td>" + u.getProjectGroup() + "</td>");
			sb.append("<td>" + translateRole(u.getRole()) + "</td>");
			sb.append("<td>" + createDropDown(u.getRole(), u.getId()) + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("<INPUT TYPE=" + formElement("submit") + "VALUE="
				+ formElement("Spara") + ">");
		sb.append("</form>");
		return sb.toString();
	}

	private String translateRole(int role) {
		switch (role) {
		case 1:
			return ("Administratör");
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
		sb.append("<th>Användarnamn</th>");
		sb.append("<th>Projectgrupp</th>");
		sb.append("<th>Roll</th>");
		sb.append("<th>Välj roll</th>");
		sb.append("</tr>");
		return sb.toString();
	}

}
