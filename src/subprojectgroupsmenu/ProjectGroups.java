package subprojectgroupsmenu;

import database.*;
import java.util.*;

public class ProjectGroups {
	private String name, startweek, endweek, estimatedHours;
	private Database db;

	public ProjectGroups(String name, String startWeek, String endWeek,
			String estimatedHours) {

		this.endweek = endWeek;
		this.name = name;
		this.startweek = startWeek;
		this.estimatedHours = estimatedHours;
		db = new Database();
	}

	public String addUserForm() {
		return null;
	}

	public String showProjectGroup(ArrayList<User> users) {
		String result = null;
		for (User u : users) {
			result += u.getUsername() + " ";
		}
		return result;
	}

	public boolean toggleActiveProjectGroup(int id, boolean active) {
		return (active) ? db.activateProjectGroup(id) : db
				.deactivateProjectGroup(id);
	}

	public boolean removeUserFromProjectGroup(String name, int id) {
		ArrayList<User> users = db.getUsers();
		int userID = 0;
		for (User u : users) {
			if (name.equals(u.getUsername())) {
				userID = u.getId();
			}

		}
		return db.removeUserFromProjectGroup(userID, id);
	}

	public boolean addUserToProjectGroup(String name, int id) {
		ArrayList<User> users = db.getUsers();
		int userID = 0;
		for (User u : users) {
			if (name.equals(u.getUsername())) {
				userID = u.getId();
			}
		}
		return db.addUserToProjectGroup(userID, id);
	}

	public void makeProjectLeader(String name, int id) {
	
	}

	public void removeProjectLeader(String name, int id) {
	}
}
