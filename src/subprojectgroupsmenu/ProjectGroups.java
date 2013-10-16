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
	public ProjectGroups(String name, String startWeek, String endWeek,
			String estimatedHours) {

		this.endweek = endWeek;
		this.name = name;
		this.startweek = startWeek;
		this.estimatedHours = estimatedHours;
		db = new Database();
	}

	/**
	 * Generates a form for adding new users
	 * 
	 * @return HTML code for the form
	 */
	public String addUserForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("get");
		html += "<p> Add user name: <input type=" + formElement("text")
				+ " name=" + formElement("addname") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("Add user") + '>';
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
		String result = null;
		for (User u : users) {
			result += u.getUsername() + " ";
		}
		return result;
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
		User u = search(db.getUsers());
		int userID = u.getId();
		return db.removeUserFromProjectGroup(userID, id);
	}

	/**
	 * Adds a user to a project group
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	public boolean addUserToProjectGroup(String name, int id) {
		User u = search(db.getUsers());
		int userID = u.getId();
		return db.addUserToProjectGroup(userID, id);
	}

	/**
	 * Makes a user project leader of a project group
	 * 
	 * @param name
	 * @param id
	 */
	public void makeProjectLeader(String name, int id) {
		User u = search(db.getUsers());
		u.setRole(2);
		u.setProjectGroup(id);
	}

	/**
	 * Removes the role project leader from a user
	 * 
	 * @param name
	 * @param id
	 */
	public void removeProjectLeader(String name, int id) {
		User u = search(db.getUsers());
		u.setRole(3);
		u.setProjectGroup(id);

	}

	private User search(ArrayList<User> users) {
		for (User u : users) {
			if (name.equals(u.getUsername())) {
				return u;
			}
		}
		return null;
	}

	private String formElement(String par) {
		return '"' + par + '"';
	}
}
