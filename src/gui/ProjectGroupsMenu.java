package gui;

import base.servletBase;

public class ProjectGroupsMenu extends servletBase {
	
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
				/*
				 * Visa projektgrupper
				 * Ny projektgrupp
				 * Aktivera/inaktivera
				 * L������gg till anv������ndare i projektgrupp
				 * Ta bort anv������ndare i projektgrupp
				 * Hantera projektledare
				 */
				
				html += ""
						+ "<li><a href=\"ShowProjectGroups\">Show project groups</a></li>"
						+ "<li><a href=\"NewProjectGroup\">New project group</a></li>"
						+ "<li><a href=\"ActiveStatusForProjectGroup\">Activate/inactivate project groups</a></li>"
						+ "<li><a href=\"AddMemberToProjectGroup\">Add users to project groups</a></li>"
						+ "<li><a href=\"RemoveMemberFromProjectGroup\">Remove a users from project groups</a></li>"
						+ "<li><a href=\"HandleProjectLeader\">Manage project leaders</a></li>"; 
				break; 
		}
		
		String outro = ""
				+ "</div>"
				+ "<div class=\"col-lg-9\">"
				+ "<section class=\"main-view\">"; 
		
		return html + outro; 
	}
}
