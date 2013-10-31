package subprojectgroupsmenu;

import database.*;

import java.util.*;

import javax.servlet.annotation.WebServlet;

public class ProjectGroups {
	private String name, startweek, endweek, estimatedHours;
	private Database db;

	/**
	 * Constructor
	 * 
	 */
	public ProjectGroups() {
		db = new Database();
	}
	
	/**
	 * Creates a project group.
	 * @param name
	 * @param startWeek
	 * @param endWeek
	 * @param estimatedHours
	 * @return
	 */
	public boolean createProjectGroup(String name, String startWeek, String endWeek, String estimatedHours) {
		if(db.addProjectGroup(new ProjectGroup(1, name, true, Integer.parseInt(startWeek), Integer.parseInt(endWeek), Integer.parseInt(estimatedHours)))) {
			return true;
		}
		return false;
	}

	/**
	 * Generates a form for adding users to a project group.
	 * 
	 * @return HTML code for the form
	 */
	public String addUserForm() {
		String html;
		html = "<p> <form name=" + formElement("input") + "id=" + formElement("addusertogroup");
		html += " method=" + formElement("get");
		html += "<p> Ange anv�ndarnamn: <input type=" + formElement("text")
				+ " name=" + formElement("addname") + '>';
		html += "<p> Grupp id: <input type=" + formElement("text")
				+ " groupid=" + formElement("groupid") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("L�gg till anv�ndare") + '>';
		html += "</form>";
		return html;
	}

	/**
	 * Shows the users in a specific project group
	 * 
	 * @param users
	 * @return
	 */
	public String showProjectGroup(ArrayList<User> users) {
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
				+ formElement("H�mta anv�ndare") + ">");
		sb.append("</form>");
		return sb.toString();
	}

	/**
	 * Activates/deactivates a project group depending on the parameter active
	 * 
	 * @param id
	 * @param active
	 * @return
	 */
	public boolean toggleActiveProjectGroup(int id, boolean active) {
		return (active) ? db.activateProjectGroup(id) : db
				.deactivateProjectGroup(id);
	}

	/**
	 * Removes a user from a project group
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	public boolean removeUserFromProjectGroup(String name, int id) {
		return db.removeUserFromProjectGroup(db.getUser(name).getId(), id);
	}

	/**
	 * Adds a user to a project group
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	public boolean addUserToProjectGroup(String name, int id) {
		return db.addUserToProjectGroup(db.getUser(name).getId(), id);
	}

	/**
	 * Makes a user project leader of a project group
	 * 
	 * @param name
	 * @param id
	 */
	public void makeProjectLeader(String name, int id) {
		db.getUser(name).setRole(2);
		db.getUser(name).setProjectGroup(id);
	}

	/**
	 * Removes the role project leader from a user
	 * 
	 * @param name
	 * @param id
	 */
	public void removeProjectLeader(String name, int id) {
		db.getUser(name).setRole(3);
		db.getUser(name).setProjectGroup(id);

	}

	private String formElement(String par) {
		return '"' + par + '"';
	}

	private String buildShowUsersInGroupTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table class=\"table table-bordered table-hover\"");
		sb.append("<tr>");
		sb.append("<th>Anv�ndarnamn</th>");
		sb.append("<th>Projectgrupp</th>");
		sb.append("<th>Roll</th>");
		sb.append("<th>V�lj</th>");
		sb.append("</tr>");
		return sb.toString();
	}

	private String createRadio(int id) {
		return "<input type=" + formElement("radio") + "name="
				+ formElement("reportId") + "value="
				+ formElement(Integer.toString(id)) + ">";
	}
}
