package subProjectMembersMenu;
import java.util.*;
import database.*;

public class ProjectMembers {
	private int groupName;
	private Database db;

	/**
	 * Constructor
	 * @param groupName
	 */
	public ProjectMembers(String groupName) {
		this.groupName=Integer.parseInt(groupName);
		db= new Database();
	}
	
	/**
	 * Shows the users in a specific project group
	 * @param users
	 * @return
	 */
	public String showMembers(ArrayList<User> users) {
		String result = null;
		for (User u : users) {
			result += u.getUsername() + " ";
		}
		return result;
	}
	
	/**
	 * Changes the role of a user
	 * @param user
	 * @param role
	 * @return
	 */
	public String changerole(User user, int role) {
		db.getUser(user.getId()).setRole(role);
		return " ";
		//vad ska returneras här?
	}
	
	/**
	 * Generates a form where it is possible to specify a project group.
	 * @return
	 */
	public String groupForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("get");
		html += "<p> Which group : <input type=" + formElement("text")
				+ " name=" + formElement("addname") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("Get group") + '>';
		html += "</form>";
		return html;
	}
	private String formElement(String par) {
		return '"' + par + '"';
	}
	
}
