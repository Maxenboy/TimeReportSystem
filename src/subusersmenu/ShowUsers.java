package subusersmenu;

import gui.UsersMenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import database.User;
import base.*;

@WebServlet("/ShowUsers")
public class ShowUsers extends UsersMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2190552005706112015L;
	private Users users;

	public ShowUsers() {
		users = new Users();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		int permission = (Integer) session.getAttribute("user_permissions");
		out.print(getPageIntro());
		out.print(generateMainMenu(permission, request));
		out.print(generateSubMenu(permission));
		String s = users.showUsers(db.getUsers());
		if (s == null)
			out.print("<p> Inget att visa </p>");
		else {
			if ((Integer) session.getAttribute("user_permissions") == 1) {
				out.print(adminShowUsers(db.getUsers()));
			} else {
				out.print(s);
			}
		}
		out.print(getPageOutro());

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

	}

	private String adminShowUsers(ArrayList<User> users) {
		if (users.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=post>");
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
			sb.append("<td>" + u.isActive() + "</td>");
			sb.append("<td>" + u.getPassword() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("</form>");

		return sb.toString();
	}

	private String translateRole(int role) {
		switch (role) {
		case 1:
			return ("Administrator");
		case 2:
			return ("Project Leader");
		case 4:
			return ("System Group");
		case 5:
			return ("System Group Leader");
		case 6:
			return ("Development Group");
		case 7:
			return ("Test Group");
		case 8:
			return ("Test Leader");
		default:
			return ("Unknown Role");
		}
	}

	private String buildShowUsersTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table class=\"table table-bordered table-hover\"");
		sb.append("<tr>");
		sb.append("<th>Anv��ndarnamn</th>");
		sb.append("<th>Projektgrupp</th>");
		sb.append("<th>Roll</th>");
		sb.append("<th>Aktiv</th>");
		sb.append("<th>L��senord</th>");
		sb.append("</tr>");
		return sb.toString();
	}
}
