package gui;

import base.ServletBase;

public class StatisticsMenu extends ServletBase {
	
	public StatisticsMenu() {
		super(); 
	}

	/**
	 * Genererar undermeny f\u00F6r Statistik
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
		case PERMISSION_OTHER_USERS: 
		case PERMISSION_WITHOUT_ROLE: 
			html += ""
					+ "<li><a href=\"Statistics\">Visa statistik</a></li>"
					+ "<li><a href=\"BurnDown\">Visa burn-down-diagram</a></li>"; 			
			break; 	
		}
		
		String outro = ""
				+ "</div>"
				+ "<div class=\"col-lg-9\">"
				+ "<section class=\"main-view\">"; 
		
		return html + outro; 
	}
}
