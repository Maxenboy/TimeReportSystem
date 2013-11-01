package gui;

import base.servletBase;

public class UsersMenu extends servletBase {
	
	public UsersMenu() {
		super(); 
	}

	public String generateSubMenu(int role) {
		String html = ""
				+ "<section class=\"main-content container\">"
				+ "<div class=\"row\">"
				+ "<div class=\"col-lg-3\">"
				+ "<ul class=\"nav nav-pills nav-stacked\">"; 
		
		
		switch(role) {
		case PERMISSION_ADMIN: 
			html += ""
					+ "<li><a href=\"ShowUsers\">Visa användare</a></li>"
					+ "<li><a href=\"NewUser\">Ny användare</a></li>"
					+ "<li><a href=\"ActiveStatusForUser\">Aktivera/inaktiver användare</a></li>"
					+ "<li><a href=\"HandleAdminRights\">Hantera administratörrättigheter</a></li>"; 			
			break; 
		case PERMISSION_OTHER_USERS: 
		case PERMISSION_WITHOUT_ROLE: 
		case PERMISSION_PROJ_LEADER: 	
			// f��r inte se :v h��h��h��
			break; 	
		}
		
		
		String outro = ""
				+ "</div>"
				+ "<div class=\"col-lg-9\">"
				+ "<section class=\"main-view\">"; 
		
		return html + outro; 
	}
}
