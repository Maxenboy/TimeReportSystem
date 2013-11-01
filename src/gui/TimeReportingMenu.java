package gui;

import base.servletBase;

public class TimeReportingMenu extends servletBase {
	protected static final int PERMISSION_ADMIN = 			1;
	protected static final int PERMISSION_PROJ_LEADER = 	2; 
	protected static final int PERMISSION_WITHOUT_ROLE = 	3; 
	protected static final int PERMISSION_OTHER_USERS = 	4; 
	
	
	public TimeReportingMenu() {
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
					+ "<li><a href=\"ShowTimeReports\">Visa tidrapporter</a></li>"
					+ "<li><a href=\"ChangeTimeReport\">\u00C4ndra tidrapport</a></li>"
					+ "<li><a href=\"RemoveTimeReport\">Ta bort tidrapport</a></li>"
					+ "<li><a href=\"SignTimeReports\">Signera tidrapport</a></li>"
					+ "</ul>"; 
			break; 
		case PERMISSION_PROJ_LEADER: 
			html += ""
					+ "<li><a href=\"ShowTimeReports\">Visa tidrapporter</a></li>"
					+ "<li><a href=\"NewTimeReport\">Ny tidrapport</a></li>"
					+ "<li><a href=\"ChangeTimeReport\">\u00C4ndra tidrapport</a></li>"
					+ "<li><a href=\"RemoveTimeReport\">Ta bort tidrapport</a></li>"
					+ "<li><a href=\"SignTimeReports\">Signera tidrapport</a></li>"
					+ "</ul>"; 
			break; 
			
		case PERMISSION_WITHOUT_ROLE: 
			html += ""
					+ "<li><a href=\"ShowTimeReports\">Visa tidrapporter</a></li>"
					+ "<li><a href=\"NewTimeReport\">Ny tidrapport</a></li>"
					+ "<li><a href=\"ChangeTimeReport\">\u00C4ndra tidrapport</a></li>"
					+ "<li><a href=\"RemoveTimeReport\">Ta bort tidrapport</a></li>"
					+ "</ul>"; 			
			break; 
			
		case PERMISSION_OTHER_USERS: 
			html += ""
					+ "<li><a href=\"ShowTimeReports\">Visa tidrapporter</a></li>"
					+ "<li><a href=\"NewTimeReport\">Ny tidrapport</a></li>"
					+ "<li><a href=\"ChangeTimeReport\">\u00C4ndra tidrapport</a></li>"
					+ "<li><a href=\"RemoveTimeReport\">Ta bort tidrapport</a></li>"
					+ "</ul>"; 			
			break; 
		}
		
		
		String outro = ""
				+ "</div>"
				+ "<div class=\"col-lg-9\">"
				+ "<section class=\"main-view\">"; 
		
		return html + outro; 
	}
}
