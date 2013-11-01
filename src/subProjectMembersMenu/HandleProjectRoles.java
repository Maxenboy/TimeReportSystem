package subProjectMembersMenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import database.User;

@WebServlet("/HandleProjectRoles")
public class HandleProjectRoles extends gui.UsersMenu{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ProjectMembers members;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		int userPermission = (Integer) session.getAttribute("user_permissions");
		out.append(generateMainMenu(userPermission, request));
		out.print(generateSubMenu(userPermission));
		if (request.getParameter("session") == null) {
			out.print(groupForm());
		} else if (request.getParameter("session").equals("found")) {
			members = new ProjectMembers(request.getParameter("name"));
			out.print(showMembers(db.getUsers(Integer.parseInt(request
							.getParameter("groupname")))));
		} else if (request.getParameter("session").equals("failed")) {
			out.print("<script>$(alert(\"Inkorrekt grupp-id\"))</script>"
					+ groupForm());
		} else {
			out.print("<script>$(alert(\"Error\"))</script>" + groupForm());
		}
		out.print(getPageOutro());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		String stage = request.getParameter("session");
		if (stage.equals("found")) {
			String id = request.getParameter("reportId");
			String role = request.getParameter("role");
			members.changerole(db.getUser(Integer.parseInt(id)),
					Integer.parseInt(role));
		} else {
			String groupName = request.getParameter("groupname");
			if (db.getProjectGroup(Integer.parseInt(groupName)) != null) {
				response.sendRedirect(request.getRequestURI()
						+ "?session=found&groupname=" + groupName);
			} else {
				response.sendRedirect(request.getRequestURI()
						+ "?session=failed");
			}
		}

	}

	private String showMembers(ArrayList<User> users) {
		if (users.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=post ACTION=" + formElement("ShowUsers") + ">");
		sb.append(buildShowUsersInGroupTable());
		for (User u : users) {
			sb.append("<tr>");
			sb.append("<td>" + u.getUsername() + "</td>");
			sb.append("<td>" + u.getProjectGroup() + "</td>");
			sb.append("<td>" + u.getRole() + "</td>");
			sb.append("<td>" + createRadio(u.getId()) + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("<br> Ny projektroll: <input type=" + formElement("text")
				+ "role=" + formElement("role") + ">");
		sb.append("<INPUT TYPE=" + formElement("submit") + "VALUE="
				+ formElement("Spara") + ">");
		sb.append("</form>");
		return sb.toString();
	}

	private String buildShowUsersInGroupTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table class=\"table table-bordered table-hover\"" + ">");
		sb.append("<tr>");
		sb.append("<th>Anv��ndarnamn</th>");
		sb.append("<th>Projektgrupp</th>");
		sb.append("<th>Roll</th>");
		sb.append("<th>V��lj</th>");
		sb.append("</tr>");
		return sb.toString();
	}

	private String createRadio(int id) {
		return "<input type=" + formElement("radio") + "name="
				+ formElement("reportId") + "value="
				+ formElement(Integer.toString(id)) + ">";
	}

	private String groupForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("POST");
		html += "<p> Gruppnamn : <input type=" + formElement("text") + " name="
				+ formElement("groupname") + '>';
		html += "<input type=" + formElement("Spara") + '>';
		html += "</form>";
		return html;
	}

	
}
