package subProjectMembersMenu;

import java.util.*;
import database.*;

public class ProjectMembers {
	private Database db;

	/**
	 * Konstruktor
	 * 
	 * @param groupName namnet p\u00E5 den grupp man vill anv\u00E4nda
	 * @param database databasen gruppe finns i
	 */
	public ProjectMembers(String groupName, Database database) {
		this.db = database;
	}

	/**
	 * Visar anv\u00E4ändarna i en specefik grupp i tabellform
	 * 
	 * @param users en lista på anv\u00E4ndare
	 * @return html kod i tabellform inneh\u00E5llade alla an\u00E4ndare i en grupp
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
			sb.append("<td>" + u.getProjectGroupId() + "</td>");
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
	 * \u00C4ndrar rollen på en anv\u00E4ndare
	 * 
	 * @param user anv\u00E4ndaren vars roll ska \u00E4ndras
	 * @param role rollen som anv\u00E4ndaren ska f\u00E5
	 * @return true
	 */
	public String changerole(User user, int role) {
		db.getUser(user.getId()).setRole(role);
		return "true";
	}

	/**
	 * Generar ett html formul\u00E4r d\u00E4r det \u00E4r m\u00F6jligt att ange en grupps namn 
	 * 
	 * @return html kod f\u00F6r formul\u00E4ret
	 */
	public String groupForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("POST");
		html += "<p> Gruppnamn : <input type=" + formElement("text") + " name="
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
