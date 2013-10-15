package subprojectgroupsmenu;

import database.*;
import java.util.*;

public class ProjectGroups {

	public ProjectGroups(String name, String startWeek, String endWeek,
			String estimatedHours) {
	}
	
	public String addUserForm() {
		return null;
	}
	
	public String showProjectGroup(ArrayList<User> users) {
		return null;
	}
	
	public boolean toggleActiveProjectGroup(int id, boolean active) {
		return false;
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
