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
					+ "<li><a href=\"ShowUsers\">Show users</a></li>"
					+ "<li><a href=\"NewUser\">New user</a></li>"
					+ "<li><a href=\"ActiveStatusForUser\">Activate/inactivate users</a></li>"
					+ "<li><a href=\"HandleAdminRights\">Manage admin rights</a></li>"; 			
			break; 
		case PERMISSION_OTHER_USERS: 
		case PERMISSION_WITHOUT_ROLE: 
		case PERMISSION_PROJ_LEADER: 	
			// får inte se :v höhöhö
			break; 	
		}
		
		
		String outro = ""
				+ "</div>"
				+ "<div class=\"col-lg-9\">"
				+ "<section class=\"main-view\">"; 
		
		return html + outro; 
	}
}
