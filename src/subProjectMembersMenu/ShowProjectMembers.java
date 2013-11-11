package subProjectMembersMenu;

import gui.ProjectMembersMenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.ProjectGroup;
import database.User;

@WebServlet("/ShowProjectMembers")
public class ShowProjectMembers extends ProjectMembersMenu {

	private static final long serialVersionUID = -6458493959837089759L;
	/**
	 * Konstruktor
	 */
	public ShowProjectMembers() {

	}
	/**
	 * doGet-metoden anropas d\u00E5 anv\u00E4ndaren vill se projektmedlemmar i en grupp.
	 * Om anv\u00E4ndaren \u00E4r en administrat\u00E5r s\u00E5 visas tabell inneh\u00E5llande alla medlemmar i en grupp.
	 * Om anv\u00E4ndaren \u00E4r utan en roll visas en varningstext.
	 * Annars visas en tabell med de projektmedlemmar som finns i anv\u00E4ndarens grupp.
	 */

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			out.print(getPageIntro());
			int userPermission = (Integer) session.getAttribute("user_permissions");
			out.append(generateMainMenu(userPermission, request));
			out.print(generateSubMenu(userPermission));
			switch (userPermission) {
			case PERMISSION_WITHOUT_ROLE:
				out.println("<p style='color: red;'>Du \u00E4r inte tilldelad n\u00E5gon roll i projektet och har d\u00E4rf\u00F6r inte tillg\u00E5ng till den h\u00E4r funktionen. Kontakta din projektledare.</p>");
				break;
			case PERMISSION_ADMIN:
				if (request.getParameter("thegroup") == null) {
					out.print(showProjectGroups());
				} else {
					ArrayList<User> users = db.getUsers(Integer
							.parseInt(request.getParameter("thegroup")));
					out.print("<H1> Projektgrupp: " + db.getProjectGroup(Integer.parseInt(request.getParameter("thegroup"))).getProjectName() + "</H1><br>" +showProjectGroup(users));
				}
				break;
			default:
				ArrayList<User> users = db.getUsers((Integer) session.getAttribute("project_group_id"));
				out.print(db.getProjectGroup((Integer) session.getAttribute("project_group_id")).getProjectName() + "<br>" +showProjectGroup(users));
			}
			out.print(getPageOutro());
		} else {
			response.sendRedirect("LogIn");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
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

	private String showProjectGroup(ArrayList<User> users) {
		if (users.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=" + formElement("get")
				+ "onsubmit=\"return confirm('\u00C4r du s\u00E4ker?')\"" + ">");
		sb.append(buildShowUsersInGroupTable());
		for (User u : users) {
			sb.append("<tr>");
			sb.append("<td>" + u.getUsername() + "</td>");
			sb.append("<td>" + db.getProjectGroup(u.getProjectGroupId()).getProjectName() + "</td>");
			sb.append("<td>" + translateRole(u.getRole()) + "</td>");
			String active = u.isActive() ? "Aktiv" : "Inaktiv";
			sb.append("<td>" + active + "</td>");
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
		sb.append("<th>Projektgrupp</th>");
		sb.append("<th>Roll</th>");
		sb.append("<th>Aktiv</th>");
		sb.append("</tr>");
		return sb.toString();
	}
}
