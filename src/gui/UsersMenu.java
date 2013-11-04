package gui;

import subusersmenu.Users;
import base.ServletBase;

public class UsersMenu extends ServletBase {
	
	protected Users users;
	
	public UsersMenu() {
		super(); 
		users = new Users(db);
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
					+ "<li><a href=\"ActiveStatusForUser\">Aktivera/inaktivera användare</a></li>"
					+ "<li><a href=\"HandleAdminRights\">Hantera administratörsrättigheter</a></li>"; 			
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
