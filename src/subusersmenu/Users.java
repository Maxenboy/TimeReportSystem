package subusersmenu;

import database.*;

import java.util.*;
import java.util.regex.Pattern;

public class Users {
	private Database db;

	/**
	 * Constructor
	 */
	public Users(Database database) {
		this.db = database;
	}

	/**
	 * Generates a form where it is possible to specify a new user.
	 * 
	 * @return
	 */
	public String userForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("POST");
		html += "<p> Användarnamn : <input type=" + formElement("text") + " name="
				+ formElement("username") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("Skapa användare") + '>';
		html += "</form>";
		return html;

	}

	/**
	 * Checks if the name is acceptable
	 * 
	 * @param name
	 * @return
	 */
	public int checkNewName(String name) {
		if (name.length() < 5 || name.length() > 10
				|| Pattern.matches("[^a-zA-Z0-9]*", name)) {
			return 1;
		}
		for (User u : db.getUsers()) {
			if (u.getUsername().equals(name)) {
				return 2;
			}
		}
		return 3;
	}

	/**
	 * Creates a random password
	 * 
	 * @return
	 */
	public String createPassword() {
		Random rand = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			sb.append(Character.toChars(rand.nextInt(25) + 97));
		}
		return sb.toString();
	}

	/**
	 * Shows all the users
	 * 
	 * @param users
	 * @return
	 */
	public String showUsers(ArrayList<User> users) {
		if (users.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=" + formElement("get") + "onsubmit=\"return confirm('Är du säker?')\"" + ">");
		sb.append(buildShowUsersTable());
		for (User u : users) {
			sb.append("<tr>");
			sb.append("<td>" + u.getUsername() + "</td>");
			if (u.getProjectGroup() > 0) {
				sb.append("<td>"
						+ db.getProjectGroup(u.getProjectGroup())
								.getProjectName() + "</td>");
			} else {
				sb.append("<td> Ingen projektgrupp </td>");
			}
			sb.append("<td>" + translateRole(u.getRole()) + "</td>");
			String active = u.isActive() ? "Aktiv" : "Inaktiv";
			sb.append("<td>"+ active + "</td>");
			sb.append("<td>" + createRadio(u.getId()) + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("<INPUT TYPE=" + formElement("submit") + "VALUE="
				+ formElement("Spara") + ">");
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
	 * Makes a user administrator
	 * 
	 * @param name
	 */
	public void makeAdministrator(String name) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(db.getUser(name).getId(), User.ROLE_ADMIN);
		db.setUserRoles(map);
	}

	/**
	 * Removes the role administrator from a user
	 * 
	 * @param name
	 */
	public void unmakeAdministrator(String name) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(db.getUser(name).getId(), User.ROLE_NO_ROLE);
		db.setUserRoles(map);
	}

	/**
	 * Deactivates a user
	 * 
	 * @param name
	 * @return
	 */
	public boolean deactivateUser(String name) {
		return db.deactivateUser(db.getUser(name).getId());
	}

	/**
	 * Adds a user to the system
	 * 
	 * @param name
	 * @return
	 */
	public boolean addUser(String name) {
		if (checkNewName(name)==3) {
			db.addUser(new User(name));
			return true;
		}
		return false;
	}

	/**
	 * Activates a user
	 * 
	 * @param name
	 * @return
	 */
	public boolean activateUser(String name) {
		return db.activateUser(db.getUser(name).getId());
	}

	private String formElement(String par) {
		return '"' + par + '"';
	}

	private String createRadio(int id) {
		return "<input type=" + formElement("radio") + "name="
				+ formElement("username") + "value="
				+ formElement(Integer.toString(id)) + ">";
	}

	private String buildShowUsersTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table class=\"table table-bordered table-hover\"");
		sb.append("<tr>");
		sb.append("<th>Anv\u00E4ndarnamn</th>");
		sb.append("<th>Projektgrupp</th>");
		sb.append("<th>Roll</th>");
		sb.append("<th>Aktiv</th>");
		sb.append("<th>V\u00E4lj</th>");
		sb.append("</tr>");
		return sb.toString();
	}

}
