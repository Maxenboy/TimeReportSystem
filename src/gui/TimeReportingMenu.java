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
					+ "<li><a href=\"ShowTimeReports\">Show time reports</a></li>"
					+ "<li><a href=\"ChangeTimeReport\">Change time report</a></li>"
					+ "<li><a href=\"RemoveTimeReport\">Remove time report</a></li>"
					+ "<li><a href=\"SignTimeReport\">Sign time report</a></li>"
					+ "</ul>"; 
			break; 
		case PERMISSION_PROJ_LEADER: 
			html += ""
					+ "<li><a href=\"ShowTimeReports\">Show time reports</a></li>"
					+ "<li><a href=\"NewTimeReport\">New time report</a></li>"
					+ "<li><a href=\"ChangeTimeReport\">Change time report</a></li>"
					+ "<li><a href=\"RemoveTimeReport\">Remove time report</a></li>"
					+ "<li><a href=\"SignTimeReport\">Sign time report</a></li>"
					+ "</ul>"; 
			break; 
			
		case PERMISSION_WITHOUT_ROLE: 
			html += ""
					+ "<li><a href=\"ShowTimeReports\">Show time reports</a></li>"
					+ "<li><a href=\"NewTimeReport\">New time report</a></li>"
					+ "<li><a href=\"ChangeTimeReport\">Change time report</a></li>"
					+ "<li><a href=\"RemoveTimeReport\">Remove time report</a></li>"
					+ "</ul>"; 			
			break; 
			
		case PERMISSION_OTHER_USERS: 
			// lol u mad
			
			break; 
		}
		
		
		String outro = ""
				+ "</div>"
				+ "<div class=\"col-lg-9\">"
				+ "<section class=\"main-view\">"; 
		
		return html + outro; 
	}
}
