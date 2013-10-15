package subprojectgroupsmenu;

import database.*;
import java.util.*;

public class ProjectGroups {
	private String name, startweek, endweek, estimatedHours;
	private HashMap<Integer, User> users;

	public ProjectGroups(String name, String startWeek, String endWeek,
			String estimatedHours) {

		this.endweek = endWeek;
		this.name = name;
		this.startweek = startWeek;
		this.estimatedHours = estimatedHours;
		users = new HashMap<Integer, User>();
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
		boolean result = false;
		User u = users.get(id);
		if (u != null) {
			u.setActive(active);
			result = true;
		}
		return result;
	}

	public boolean removeUserFromProjectGroup(String name, int id) {
		return false;
	}

	public boolean addUserToProjectGroup(String name, int id) {
		return false;
	}

	public void makeProjectLeader(String name, int id) {
	}

	public void removeProjectLeader(String name, int id) {
	}
}
