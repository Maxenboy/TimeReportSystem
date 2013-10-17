package subprojectgroupsmenu;

import database.*;

import java.util.*;

public class ProjectGroups {
	private String name, startweek, endweek, estimatedHours;
	private Database db;

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param startWeek
	 * @param endWeek
	 * @param estimatedHours
	 */
	public ProjectGroups() {

		/*
		 * this.endweek = endWeek; this.name = name; this.startweek = startWeek;
		 * this.estimatedHours = estimatedHours;
		 */
		db = new Database();
	}

	/**
	 * Generates a form for adding users to a project group.
	 * 
	 * @return HTML code for the form
	 */
	public String addUserForm() {
		
		String html;
		html = "<p> <form name=" + formElement("input") + "id=" + formElement("addusertogroup") + '>';
		html += " method=" + formElement("get");
		html += "<p> Add user name: <input type=" + formElement("text")
				+ " name=" + formElement("addname") + '>';
		html += "<p> Group id: <input type=" + formElement("text")
				+ " groupid=" + formElement("groupid") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("Add user") + '>';
		html += "</form>"+ "</body></html>";
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
				+ formElement("Get Users") + ">");
		sb.append("</form>");
		sb.append("</body></html>");
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
}
