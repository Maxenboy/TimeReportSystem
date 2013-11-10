package base;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import database.Database;
import database.ProjectGroup;

/**
 * This class is the superclass for all servlets in the application. It includes
 * basic functionality required by many servlets, like for example a page head
 * written by all servlets, and the connection to the database.
 * 
 * This application requires a database. For username and password, see the
 * constructor in this class.
 * 
 * <p>
 * The database can be created with the following SQL command: mysql> create
 * database base;
 * <p>
 * The required table can be created with created with: mysql> create table
 * users(name varchar(10), password varchar(10), primary key (name));
 * <p>
 * The administrator can be added with: mysql> insert into users (name,
 * password) values('admin', 'adminp');
 * 
 */
public class ServletBase extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// Define states
	protected static final int LOGIN_FALSE = 0;
	protected static final int LOGIN_TRUE = 1;
	
	// User permissions
	protected static final int PERMISSION_ADMIN = 1;
	protected static final int PERMISSION_PROJ_LEADER = 2; 
	protected static final int PERMISSION_WITHOUT_ROLE = 3; 
	protected static final int PERMISSION_OTHER_USERS = 4; 
	
	protected Database db = new Database();
	
	private static final int MAX_INACTIVE_INTERVAL = 1200; // seconds
	
	/**
	 * Constructs a servlet.
	 */
	public ServletBase() {
		
	}
	
	@Override
	public void destroy() {
		db.closeConnection();
	}

	/**
	 * Checks if a user is logged in or not.
	 * 
	 * @param request
	 *            The HTTP Servlet request (so that the session can be found)
	 * @return true if the user is logged in, otherwise false.
	 */
	protected boolean loggedIn(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
		Object objectState = session.getAttribute("state");
		int state = LOGIN_FALSE;
		if (objectState != null) {
			state = (Integer) objectState;
		}
		return (state == LOGIN_TRUE);
	}
	
	/**
	 * Can be used to construct form elements.
	 * 
	 * @param par
	 *            Input string
	 * @return output string = "par"
	 */
	protected String formElement(String par) {
		return '"' + par + '"';
	}

	/**
	 * Constructs the header of all servlets.
	 * 
	 * @return String with html code for the header.
	 */
	protected String getPageIntro() {
		String intro = "<html>"
				+ "<head>"
				+ "<title> TidRapporteringsSystemet </title>"
				+ "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>"
				+ "<link href=\"bootstrap.min.css\" type=\"text/css\" rel=\"stylesheet\">"
				+ "</head>"
				+ "<body style=\"padding-top: 80px\">";
		return intro;
	}
		
	protected String getPageOutro() {
		String html = ""
				+ "</section>"
				+ "</div>"
				+ "</div>"
				+ "</section>"
				+ "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>"
				+ "</body>"
				+ "</html>"; 	
		
		return html;
	}
	
	/** 
	 * Constructs a main menu based on the current page
	 * and the permission of the user
	 * @param userPermission Check the final ints above
	 * @return HTML code for the menu
	 */
	protected String generateMainMenu(int userPermission, HttpServletRequest request) {
		String intro = ""
				+ "<section class=\"main-menu container\">"
				+ "<div class=\"row\">"
				+ "<div class=\"col-lg-8\">"
				+ "<ul class=\"nav nav-pills\">"; 
		
		switch(userPermission) {
			case PERMISSION_ADMIN:
				intro += ""
						+ "<li><a href=\"ShowProjectGroups\">Projektgrupper</a></li>"
						+ "<li><a href=\"ShowUsers\">Anv\u00E4ndare</a></li>"
						+ "<li><a href=\"ShowProjectMembers\">Projektmedlemmar</a></li>"
						+ "<li><a href=\"ShowTimeReports\">Tidrapportering</a></li>"
						+ "<li><a href=\"Statistics\">Statistik</a></li>"; 
				break; 
				
			case PERMISSION_PROJ_LEADER: 
			case PERMISSION_OTHER_USERS: 
				intro += ""
						+ "<li><a href=\"ShowProjectMembers\">Projektmedlemmar</a></li>"
						+ "<li><a href=\"ShowTimeReports\">Tidrapportering</a></li>"
						+ "<li><a href=\"Statistics\">Statistik</a></li>"; 				
				break;
				
			case PERMISSION_WITHOUT_ROLE:
				intro += ""
						+ "<li><a href=\"ShowProjectMembers\">Projektmedlemmar</a></li>"
						+ "<li><a href=\"ShowTimeReports\">Tidrapportering</a></li>"
						+ "<li><a href=\"Statistics\">Statistik</a></li>";
				break; 
		}
		
		HttpSession session = request.getSession();
		
		String closure = ""
				+ "</ul>"
				+ "</div>"; 
		
		String userInfo = ""
				+ "<div class=\"col-lg-4\">"
				+ "<div class=\"row\">"
				+ "<div class=\"col-lg-8\">"; 
		
		userInfo += ""
				+ "<p>Anv\u00E4ndare: " + session.getAttribute("name") +"</p>"
				+ "<p>Grupp: " + translateProjectGroupId((int) session.getAttribute("project_group_id")) + "</p>"
				+ "<p>Roll: "+ translateRole((int) session.getAttribute("role")) +"</p>"; 
		
		userInfo += ""
				+ "</div>"
				+ "<div class=\"col-lg-4\">"
				+ "<a href =" + formElement("LogIn") + " class='btn btn-danger btn-block'> Logga ut</a>"
				+ "</div>"
				+ "</div>"
				+ "</div>"; 
 
		
		String outro = ""
				+ "</div>"
				+ "</section>"; 
		
		return intro + closure + userInfo + outro; 
	}
	
	private String translateRole(int role) {
		switch (role) {
		case 1:
			return ("Administrat\u00F6r");
		case 2:
			return ("Projektledare");
		case 4:
			return ("Systemgrupp");
		case 5:
			return ("Systemgruppsledare");
		case 6:
			return ("Utvecklingsgrupp");
		case 7:
			return ("Testgrupp");
		case 8:
			return ("Testgruppsledare");
		default:
			return ("Utan roll");
		}
	}
	
	private String translateProjectGroupId(int projectGroupId) {
		if(projectGroupId == 0) {
			return "Ingen projektgrupp";
		}
		ProjectGroup pg = db.getProjectGroup(projectGroupId);
		if(pg == null) {
			return "Ingen projektgrupp";
		} else {
			return pg.getProjectName();
		}		
	}

}
