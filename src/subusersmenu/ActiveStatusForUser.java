package subusersmenu;

import gui.UsersMenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.User;

@WebServlet("/ActiveStatusForUser")
public class ActiveStatusForUser extends UsersMenu {

	private static final long serialVersionUID = -3023954127569399790L;

	public ActiveStatusForUser() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			int permission = (Integer) session.getAttribute("user_permissions");
			if (permission == PERMISSION_ADMIN) {
				PrintWriter out = response.getWriter();
				out.print(getPageIntro());
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				
				if (request.getParameter("username") == null) {
					out.print(showUsers(db.getUsers(), (Integer) session.getAttribute("id")));
					out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"\u00C4r du s\u00F6ker?\");if (confirmed) {$(this).submit();}});</script>");
				} else {
					User user = db.getUser(Integer.parseInt(request
							.getParameter("username")));
					if (!session.getAttribute("name").equals(user.getUsername())) {
						if (user.isActive()) {
							db.deactivateUser(user.getId());
							out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"\u00C4r du s\u00F6ker?\");if (confirmed) {$(this).submit();}});</script>");
							out.print(showUsers(db.getUsers(), (Integer) session.getAttribute("id")));
						} else {
							db.activateUser(user.getId());
							out.print(showUsers(db.getUsers(), (Integer) session.getAttribute("id")));
						}
					} else {
						out.print("<script>$(alert(\"Du kan inte inaktivera dig sj\u00E4lv!\"))</script>");
						out.print(showUsers(db.getUsers(), (Integer) session.getAttribute("id")));
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

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

	}

	private String showUsers(ArrayList<User> users, int id) {
		if (users.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=" + formElement("get")
				+ "onsubmit=\"return confirm('\u00C4r du s\u00E4ker?')\"" + ">");
		sb.append(buildShowUsersTable());
		for (User u : users) {
			sb.append("<tr>");
			sb.append("<td>" + u.getUsername() + "</td>");
			if (u.getProjectGroup() > 0) {
				sb.append("<td>"
						+ db.getProjectGroup(u.getProjectGroup())
								.getProjectName() + "</td>");
			} else {
				sb.append("<td> Ingen projektgrupp </td>");
			}
			sb.append("<td>" + translateRole(u.getRole()) + "</td>");
			String active = u.isActive() ? "Aktiv" : "Inaktiv";
			sb.append("<td>" + active + "</td>");
			if (u.getId() != id) {
				sb.append("<td>" + createRadio(u.getId()) + "</td>");
			} else {
				sb.append("<td>" + "</td>");
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("<INPUT TYPE=" + formElement("submit") + "VALUE="
				+ formElement("Aktivera/Inaktivera anv\u00E4ndare") + ">");
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

	private String createRadio(int id) {
		return "<input type=" + formElement("radio") + "name="
				+ formElement("username") + "value="
				+ formElement(Integer.toString(id)) + ">";
	}

	private String buildShowUsersTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table class=\"table table-bordered table-hover\"");
		sb.append("<tr>");
		sb.append("<th>Anv\u00E4ndarnamn</th>");
		sb.append("<th>Projektgrupp</th>");
		sb.append("<th>Roll</th>");
		sb.append("<th>Aktiv</th>");
		sb.append("<th>V\u00E4lj</th>");
		sb.append("</tr>");
		return sb.toString();
	}

}
