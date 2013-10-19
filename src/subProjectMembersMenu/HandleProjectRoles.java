package subProjectMembersMenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.*;

import database.Database;
import database.User;

public class HandleProjectRoles {

	Database db = new Database();
	ProjectMembers members;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		if (request.getParameter("session") == null) {
			out.print(getPageIntro() + groupForm());
		} else if (request.getParameter("session").equals("found")) {
			members = new ProjectMembers(request.getParameter("name"));
			out.print(getPageIntro()
					+ showMembers(db.getUsers(Integer.parseInt(request
							.getParameter("groupname")))));
		} else if (request.getParameter("session").equals("failed")) {
			out.print(getPageIntro() + "$(alert(\"Incorrect group id\"))"
					+ groupForm());
		} else {
			out.print(getPageIntro() + "$(alert(\"Error\"))" + groupForm());
		}
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
			String groupName = request.getParameter("name");
			if (db.getProjectGroup(Integer.parseInt(groupName)) != null) {
				response.sendRedirect(request.getRequestURI()
						+ "session=found&groupname=" + groupName);
			} else {
				response.sendRedirect(request.getRequestURI()
						+ "session=failed");
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
		sb.append("<br> New project role: <input type=" + formElement("text")
				+ "role=" + formElement("role") + ">");
		sb.append("<INPUT TYPE=" + formElement("submit") + "VALUE="
				+ formElement("Spara") + ">");
		sb.append("</form>");
		sb.append("</body></html>");
		return sb.toString();
	}

	private String buildShowUsersInGroupTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table border=" + formElement("1") + ">");
		sb.append("<tr>");
		sb.append("<th>Username</th>");
		sb.append("<th>Project group</th>");
		sb.append("<th>Role</th>");
		sb.append("<th>Select</th>");
		sb.append("</tr>");
		return sb.toString();
	}

	private String createRadio(int id) {
		return "<input type=" + formElement("radio") + "name="
				+ formElement("reportId") + "value="
				+ formElement(Integer.toString(id)) + ">";
	}

	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> The Base Block System </title>" + getPageJs()
				+ "</head>" + "<body>";
		return intro;
	}

	private String groupForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("POST");
		html += "<p> Username : <input type=" + formElement("text") + " name="
				+ formElement("groupname") + '>';
		html += "<input type=" + formElement("submit") + '>';
		html += "</form>";
		return html;
	}

	private String formElement(String par) {
		return '"' + par + '"';
	}

	private String getPageJs() {
		return "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>";
	}
}
