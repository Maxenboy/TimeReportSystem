package gui;

import base.ServletBase;

public class ProjectGroupsMenu extends ServletBase {
	
	public ProjectGroupsMenu() {
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
						+ "<li><a href=\"ShowProjectGroups\">Visa projektgrupper</a></li>"
						+ "<li><a href=\"NewProjectGroup\">Ny projektgrupp</a></li>"
						+ "<li><a href=\"ActiveStatusForProjectGroup\">Aktivera/Inaktivera projektgrupp</a></li>"
						+ "<li><a href=\"AddMemberToProjectGroup\">L\u00E4gg till anv\u00E4ndare i projektgrupp</a></li>"
						+ "<li><a href=\"RemoveMemberFromProjectGroup\">Ta bort anv\u00E4ndare fr\u00E5n projektgrupp</a></li>"
						+ "<li><a href=\"HandleProjectLeader\">Hantera projektledare</a></li>"; 
				break; 
		}
		
		String outro = ""
				+ "</div>"
				+ "<div class=\"col-lg-9\">"
				+ "<section class=\"main-view\">"; 
		
		return html + outro; 
	}
}
