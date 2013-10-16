package subusersmenu;

import database.*;
import java.util.*;
import java.util.regex.Pattern;

public class Users {
	private Database db;

	public Users() {
		db = new Database();
	}

	public String userForm() {
		return null;
	}

	public boolean checkNewName(String name) {
		if (name.length() < 5 || name.length() > 10
				|| !Pattern.matches("\\w", name)) {
			return false;
		}
		for (User u : db.getUsers()) {
			if (u.getUsername().equals(name)) {
				return false;

			}
		}

		return true;
	}

	public String createPassword() {
		return null;
	}

	public String showUsers(ArrayList<User> users) {
		return null;
	}

	public void makeAdministrator(String name) {
	}

	public void unmakeAdministrator(String name) {
	}

	public boolean deactivateUser(String name) {
		return false;
	}

	public boolean addUser(String name) {
		return false;
	}

	public boolean activateUser(String name) {
		return false;
	}

}
