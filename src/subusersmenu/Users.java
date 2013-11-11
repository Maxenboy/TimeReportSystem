package subusersmenu;

import database.*;

import java.util.*;
import java.util.regex.Pattern;

public class Users {
	private Database db;

	/**
	 * Konstruktor
	 */
	public Users(Database database) {
		this.db = database;
	}

	/**
	 * Generar ett html formul\u00E4r d\u00E4r det \u00E4r m\u00F6jligt att ange en ny anv\u00E4ndares namn 
	 * 
	 * @return html kod f\u00F6r formul\u00E4ret
	 */
	public String userForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("POST") + "onsubmit=\"return confirm('\u00C4r du s\u00E4ker?')\"" + ">";
		html += "<p> Anv\u00E4ndarnamn : <input type=" + formElement("text")
				+ " name=" + formElement("username") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("Skapa anv\u00E4ndare") + '>';
		html += "</form>";
		return html;

	}

	/**
	 * Verifierar om namnet \u00E4r mellan 5 och 10 tecken, inte redan finns i systemet och best\u00E5r av endast alfanumeriska tecken
	 * 
	 * @param name namnet som ska verifieras
	 * @return en siffra som avser vilka kriterier som godkäns. 1 f\u00F6r alla kriterier godkända, 2 returneras om ett s\u00E5dant namn redan finns i systemet och 3 om namnet inte \u00E4r mellan 5-10 tecken eller inte alfanumeriskt
	 */
	public int checkNewName(String name) {
		for (User u : db.getUsers()) {
			if (u.getUsername().equals(name)) {
				return 2;
			}
		}
		if (name.length() > 4 && name.length() < 11
				&& Pattern.matches("[a-zA-Z0-9]*", name)) {
			return 3;
		}
		return 1;
	}

	/**
	 * Skapar ett slumpm\u00E4ssigt l\u00F6senord
	 * 
	 * @return ett l\u00F6senord
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
	 * Visar alla anv\u00E4ndare i systemet i tabellform
	 * 
	 * @param users en lista på anv\u00E4ndare
	 * @return html kod i tabellform inneh\u00E5llade alla an\u00E4ndare
	 */
	public String showUsers(ArrayList<User> users) {
		if (users.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=" + formElement("get")
				+ "onsubmit=\"return confirm('\u00C4r du s\u00E4ker?')\"" + ">");
		sb.append(buildShowUsersTable());
		for (User u : users) {
			sb.append("<tr>");
			sb.append("<td>" + u.getUsername() + "</td>");
			if (u.getProjectGroupId() > 0) {
				sb.append("<td>"
						+ db.getProjectGroup(u.getProjectGroupId())
								.getProjectName() + "</td>");
			} else {
				sb.append("<td> Ingen projektgrupp </td>");
			}
			sb.append("<td>" + translateRole(u.getRole()) + "</td>");
			String active = u.isActive() ? "Aktiv" : "Inaktiv";
			sb.append("<td>" + active + "</td>");
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
	 * G\u00F6r en an\u00E4ndare till administrat\u00F6r i systemet
	 * 
	 * @param name namnet p\u00E5 anv\u00E4ndaren som ska bli administrat\u00F6r
	 */
	public void makeAdministrator(String name) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(db.getUser(name).getId(), User.ROLE_ADMIN);
		db.setUserRoles(map);
	}

	/**
	 * Tar bort rollen som administra\u00F6r från en administra\u00F6r
	 * 
	 * @param name namnet p\u00E5 anv\u00E4ndaren vars administrat\u00F6rs roll ska tas bort
	 */
	public void unmakeAdministrator(String name) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(db.getUser(name).getId(), User.ROLE_NO_ROLE);
		db.setUserRoles(map);
	}

	/**
	 * Deaktiverar en anv\u00E4ndare från systemet
	 * 
	 * @param name namnet p\u00E5 anv\u00E4ndaren som ska deaktiveras
	 * @return true om anv\u00E4ndaren lyckades deaktiveras, annars false
	 */
	public boolean deactivateUser(String name) {
		return db.deactivateUser(db.getUser(name).getId());
	}

	/**
	 * L\u00E4gger till en ny anv\u00E4ndare till systemet
	 * 
	 * @param name namnet p\u00E5 anv\u00E4ndaren som ska l\u00E4ggas till i systemet
	 * @return true om anv\u00E4ndaren las till, anars false
	 */
	public boolean addUser(String name) {
		if (checkNewName(name) == 3) {
			db.addUser(new User(name));
			return true;
		}
		return false;
	}

	/**
	 * Aktiverar en anv\u00E4ndare i systemet
	 * 
	 * @param name namnet p\u00E5 anv\u00E4ndaren som ska aktiveras
	 * @return true om anv\u00E4ndaren lyckades aktiveras, annars false
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
