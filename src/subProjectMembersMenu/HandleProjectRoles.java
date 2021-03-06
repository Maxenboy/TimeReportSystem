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
	private String groupName = "";
	private int state = 1;
	/**
	 * doGet-metoden anropas d\u00E5 anv\u00E4ndaren vill \u00E4ndra roller på anv\u00E4ndare i en grupp.
	 * Om anv\u00E4ndaren \u00E4r en administrat\u00E5r s\u00E5 visas först alla grupper, sedan alla medlemmar i den valda gruppen där det är möjligt att \u00E4ndra roller f\u00F6r projektmedlemmar.
	 * Om anv\u00E4ndaren \u00E4r en projektledare, \u00E4r det endast m\u00F6jligt att \u00E4ndra roller i ens egna projektgrupp.
	 * F\u00F6r andra anv\u00E4ndare \u00E4r det inte m\u00F6jligt att g\u00F6ra n\u00E5got.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession();
			PrintWriter out = response.getWriter();
			out.print(getPageIntro());
			boolean changed = false;
			int userPermission = (Integer) session.getAttribute("user_permissions");
			out.append(generateMainMenu(userPermission, request));
			out.print(generateSubMenu(userPermission));
			switch (userPermission) {
			case PERMISSION_WITHOUT_ROLE:
			case PERMISSION_OTHER_USERS:
				response.sendRedirect("");
				return;
			case PERMISSION_ADMIN:
				if (request.getParameter("thegroup") == null && groupName.equals("")) {
					out.print(showProjectGroups());
				} else {
					if (state == 1) {
						if (groupName.equals("")) {
							groupName = request.getParameter("thegroup");
						}
						ArrayList<User> users = db.getUsers(Integer
								.parseInt(groupName));
						if (users.isEmpty()) {
							out.print("<script>$(alert(\"Finns inga anv\u00E4ndare i projektgruppen!\"))</script>");
						} else {
							out.print(showProjectGroup(users, -1));
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
								changed = true;

							}
						}
						db.setUserRoles(map);
						groupName = "";
						out.print(showProjectGroups());
					}
				}
				break;
			case PERMISSION_PROJ_LEADER:
				ArrayList<User> users = db.getUsers((Integer) session
						.getAttribute("project_group_id"));
				HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
				for (User u : users) {
					if (request.getParameter("" + u.getId()) != null
							&& Integer.parseInt(request.getParameter(""
									+ u.getId())) != u.getRole()) {
						map.put(u.getId(),
								Integer.parseInt(request.getParameter(""
										+ u.getId())));
						changed = true;
						
					}
				}
				db.setUserRoles(map);
				users = db.getUsers((Integer) session.getAttribute("project_group_id"));
				out.print(showProjectGroup(users, (Integer) session.getAttribute("id")));
				break;
			}
			if(changed) {
				out.print("<script>$(alert(\"\u00C4ndringar \u00E4r sparade.\"))</script>");
			}
			out.print(getPageOutro());
		} else {
			response.sendRedirect("LogIn");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!groupName.equals("")) {
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
				+ formElement("V\u00E4lj grupp") + ">");
		sb.append("</form>");
		return sb.toString();
	}

	private String buildProjectGroupsTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table class=\"table table-bordered table-hover\">");
		sb.append("<tr>");
		sb.append("<th>Projektgrupp</th>");
		sb.append("<th>Startvecka</th>");
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
					+ "<option value=\"8\" selected>Testledare</option>"
					+ "</select>";
			break;
		default:
			html = "";
			break;
		}
		return html;
	}

	private String showProjectGroup(ArrayList<User> users, int id) {
		if (users.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=" + formElement("get") + ">");
		sb.append(buildShowUsersInGroupTable());
		for (User u : users) {
			sb.append("<tr>");
			sb.append("<td>" + u.getUsername() + "</td>");
			sb.append("<td>" + db.getProjectGroup(u.getProjectGroupId()).getProjectName() + "</td>");
			sb.append("<td>" + translateRole(u.getRole()) + "</td>");
			if (id != u.getId()) {
				sb.append("<td>" + createDropDown(u.getRole(), u.getId())
						+ "</td>");
			}
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
		sb.append("<th>Projectgrupp</th>");
		sb.append("<th>Roll</th>");
		sb.append("<th>V\u00E4lj roll</th>");
		sb.append("</tr>");
		return sb.toString();
	}

}
