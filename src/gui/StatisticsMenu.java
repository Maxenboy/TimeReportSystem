package gui;

import base.servletBase;

public class StatisticsMenu extends servletBase {
	
	public StatisticsMenu() {
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
		case PERMISSION_PROJ_LEADER: 	
		case PERMISSION_OTHER_USERS: 
			html += ""
					+ "<li><a href=\"ShowStatistics\">Show statistics</a></li>"
					+ "<li><a href=\"BurnDown\">Show burn-down-diagram</a></li>"; 			
			break; 
		
		case PERMISSION_WITHOUT_ROLE: 
			// får inte se :v
			break; 	
		}
		
		
		String outro = ""
				+ "</div>"
				+ "<div class=\"col-lg-9\">"
				+ "<section class=\"main-view\">"; 
		
		return html + outro; 
	}
}
