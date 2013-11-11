package gui;

import subusersmenu.Users;
import base.ServletBase;

public class UsersMenu extends ServletBase {
	
	protected Users users;
	
	public UsersMenu() {
		super(); 
		users = new Users(db);
	}
	
	/**
	 * Genererar undermeny f\u00F6r Användare
	 * @param role
	 * @return String med html-kod f\u00F6r undermeny.
	 */
	public String generateSubMenu(int role) {
		String html = ""
				+ "<section class=\"main-content container\">"
				+ "<div class=\"row\">"
				+ "<div class=\"col-lg-3\">"
				+ "<ul class=\"nav nav-pills nav-stacked\">"; 
		
		
		switch(role) {
		case PERMISSION_ADMIN: 
			html += ""
					+ "<li><a href=\"ShowUsers\">Visa anv\u00E4ndare</a></li>"
					+ "<li><a href=\"NewUser\">Ny anv\u00E4ndare</a></li>"
					+ "<li><a href=\"ActiveStatusForUser\">Aktivera/inaktivera anv\u00E4ndare</a></li>"
					+ "<li><a href=\"HandleAdminRights\">Hantera administrat\u00F6rsr\u00E4ttigheter</a></li>"; 			
			break; 
		case PERMISSION_OTHER_USERS: 
		case PERMISSION_WITHOUT_ROLE: 
		case PERMISSION_PROJ_LEADER: 	
			break; 	
		}
		
		
		String outro = ""
				+ "</div>"
				+ "<div class=\"col-lg-9\">"
				+ "<section class=\"main-view\">"; 
		
		return html + outro; 
	}
}
