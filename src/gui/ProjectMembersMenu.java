package gui;

import base.ServletBase;

public class ProjectMembersMenu extends ServletBase {
	
	public ProjectMembersMenu() {
		super(); 
	}
	
	
	/**
	 * Genererar undermeny f\u00F6r Projektmedlemmar
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
		case PERMISSION_PROJ_LEADER: 	
			html += ""
					+ "<li><a href=\"ShowProjectMembers\">Visa projektmedlemmar</a></li>"
					+ "<li><a href=\"HandleProjectRoles\">Hantera projektroller</a></li>"; 			
			break; 
		case PERMISSION_WITHOUT_ROLE:
		case PERMISSION_OTHER_USERS: 
			html += ""
					+ "<li><a href=\"ShowProjectMembers\">Visa projektmedlemmar</a></li>"; 	
			break; 
		}
		
		
		String outro = ""
				+ "</div>"
				+ "<div class=\"col-lg-9\">"
				+ "<section class=\"main-view\">"; 
		
		return html + outro; 
	}
}
