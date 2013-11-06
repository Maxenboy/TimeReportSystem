package subProjectMembersMenu;

import java.util.*;
import database.*;

public class ProjectMembers {
	private Database db;

	/**
	 * Constructor
	 * 
	 * @param groupName
	 */
	public ProjectMembers(String groupName, Database database) {
		this.db = database;
	}

	/**
	 * Shows the users in a specific project group
	 * 
	 * @param users
	 * @return
	 */
	public String showMembers(ArrayList<User> users) {
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
		sb.append("<INPUT TYPE=" + formElement("submit") + "VALUE="
				+ formElement("H\u00E4mta anv\u00E4ndare") + ">");
		sb.append("</form>");
		sb.append("</body></html>");
		return sb.toString();
	}

	/**
	 * Changes the role of a user
	 * 
	 * @param user
	 * @param role
	 * @return
	 */
	public String changerole(User user, int role) {
		db.getUser(user.getId()).setRole(role);
		return "true";
		// vad ska returneras h�r?
	}

	/**
	 * Generates a form where it is possible to specify a project group.
	 * 
	 * @return
	 */
	public String groupForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("POST");
		html += "<p> Anv\u00E4ndarnamn : <input type=" + formElement("text") + " name="
				+ formElement("groupname") + '>';
		html += "<input type=" + formElement("Spara") + '>';
		html += "</form>";
		return html;
	}

	private String formElement(String par) {
		return '"' + par + '"';
	}

	private String buildShowUsersInGroupTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table border=" + formElement("1") + ">");
		sb.append("<tr>");
		sb.append("<th>Anv\u00E4ndarnamn</th>");
		sb.append("<th>Projektgrupp</th>");
		sb.append("<th>Roll</th>");
		sb.append("<th>V\u00E4lj</th>");
		sb.append("</tr>");
		return sb.toString();
	}

	private String createRadio(int id) {
		return "<input type=" + formElement("radio") + "name="
				+ formElement("reportId") + "value="
				+ formElement(Integer.toString(id)) + ">";
	}

}
