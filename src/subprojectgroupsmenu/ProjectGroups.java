package subprojectgroupsmenu;

import java.util.ArrayList;

import database.Database;
import database.ProjectGroup;
import database.User;

public class ProjectGroups {
	private Database db;

	/**
	 * Konstruktor
	 * @param database databasen som ska anv\u00E4ndas
	 * 
	 */
	public ProjectGroups(Database database) {
		this.db = database;
	}
	
	/**
	 * Skapar en projektgrupp
	 * @param name namnet p\u00E5 projektgruppen
	 * @param startWeek veckan d\u00E5 projektet startar
	 * @param endWeek veckan d\u00E5 projektet slutar
	 * @param estimatedHours antalet timmar projektet estimeras till
	 * @return true om projektgruppen lyckades skapas, annars false
	 */
	public boolean createProjectGroup(String name, String startWeek, String endWeek, String estimatedHours) {
		return db.addProjectGroup(new ProjectGroup(name, Integer.parseInt(startWeek), Integer.parseInt(endWeek), Integer.parseInt(estimatedHours)));
	}

	/**
	 * Generar ett html formul\u00E4r d\u00E4r det \u00E4r m\u00F6jligt att ange anv\u00E4ndarens namn som ska l\u00E4ggas till i en projektgrupp 
	 * 
	 * @return html kod f\u00F6r formul\u00E4ret
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
				+ formElement("L\u00E4gg till anv\u00E4ndare") + '>';
		html += "</form>";
		return html;
	}

	/**
	 * Visar alla anv\u00E4ndare i en specifik grupp i tabellform
	 * 
	 * @param users en lista på anv\u00E4ndare
	 * @return html kod i tabellform inneh\u00E5llade alla an\u00E4ndare i en grupp
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
			sb.append("<td>" + u.getProjectGroupId() + "</td>");
			sb.append("<td>" + translateRole(u.getRole()) + "</td>");
			sb.append("<td>" + createRadio(u.getId()) + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("<INPUT TYPE=" + formElement("submit") + "VALUE="
				+ formElement("H\u00E4mta anv\u00E4ndare") + ">");
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
	 * Aktiverar/deaktiverar en projektgrupp beroende p\u00E5 parametern active
	 * 
	 * @param id gruppens id som ska aktiveras/deaktiveras
	 * @param active anger om gruppen ska aktiveras/deaktiveras
	 * @return true om gruppen lyckades aktiveras/deaktiveras, annars false
	 */
	public boolean toggleActiveProjectGroup(int id, boolean active) {
		if(active) {
			return db.activateProjectGroup(id);
		} else {
			return db.deactivateProjectGroup(id);
		}
	}

	/**
	 * Tar bort en anv\u00E4ndare fr\u00E5n en projektgrupp
	 * 
	 * @param name namnet på den anv\u00E4ndares om ska tas bort
	 * @param id gruppens id som anv\u00E4ndare ska tas bort ur
	 * @return true om borttagningen lyckades, annars false
	 */
	public boolean removeUserFromProjectGroup(String name, int id) {
		return db.removeUserFromProjectGroup(db.getUser(name).getId(), id);
	}

	/**
	 * L\u00E4gger till en anv\u00E4ndare till en projektgrupp
	 * 
	 * @param name namnet på den anv\u00E4ndare som ska l\u00E4ggas till i en projektgrupp
	 * @param id gruppen id som e anv\u00E4ndares ska l\u00E4ggas till i 
	 * @return true om till\u00E4ggningen lyckades, annars false
	 */
	public boolean addUserToProjectGroup(String name, int id) {
		return db.addUserToProjectGroup(db.getUser(name).getId(), id);
	}

	/**
	 * G\u00F6r en anv\u00E4ndare till projektledare i en grupp
	 * 
	 * @param name namnet p\u00E5 en anv\u00E4ndare som ska g\u00F6ras till en projektledare
	 * @param id gruppens id vars en anv\u00E4ndare ska göras till projektledare i
	 */
	public void makeProjectLeader(String name, int id) {
		db.getUser(name).setRole(2);
		db.getUser(name).setProjectGroupId(id);
	}

	/**
	 * Tar bort rollen som projektledare f\u00F6r en anv\u00E4ndare i en grupp
	 * 
	 * @param name namnet på den anv\u00E4ndare vars roll som projekledare ska tas bort
	 * @param id projektledarens grupp id
	 */
	public void removeProjectLeader(String name, int id) {
		db.getUser(name).setRole(3);
		db.getUser(name).setProjectGroupId(id);

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
