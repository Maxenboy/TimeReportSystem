package subprojectgroupsmenu;

import java.util.ArrayList;

import database.Database;
import database.ProjectGroup;
import database.User;

public class ProjectGroups {
	private String name, startweek, endweek, estimatedHours;
	private Database db;

	/**
	 * Constructor
	 * 
	 */
	public ProjectGroups(Database database) {
		this.db = database;
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
		return db.addProjectGroup(new ProjectGroup(name, Integer.parseInt(startWeek), Integer.parseInt(endWeek), Integer.parseInt(estimatedHours)));
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
		html += "<p> Ange anv\u00E4ndarnamn: <input type=" + formElement("text")
				+ " name=" + formElement("addname") + '>';
		html += "<p> Grupp id: <input type=" + formElement("text")
				+ " groupid=" + formElement("groupid") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("Lägg till användare") + '>';
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
		sb.append("<FORM METHOD=" + formElement("get") +">");
		sb.append(buildShowUsersInGroupTable());
		for (User u : users) {
			sb.append("<tr>");
			sb.append("<td>" + u.getUsername() + "</td>");
			sb.append("<td>" + u.getProjectGroup() + "</td>");
			sb.append("<td>" + translateRole(u.getRole()) + "</td>");
			sb.append("<td>" + createRadio(u.getId()) + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("<INPUT TYPE=" + formElement("submit") + "VALUE="
				+ formElement("Hämta användare") + ">");
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

	/**
	 * Activates/deactivates a project group depending on the parameter active
	 * 
	 * @param id
	 * @param active
	 * @return
	 */
	public boolean toggleActiveProjectGroup(int id, boolean active) {
		if(active) {
			return db.activateProjectGroup(id);
		} else {
			return db.deactivateProjectGroup(id);
		}
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
