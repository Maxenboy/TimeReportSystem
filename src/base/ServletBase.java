package base;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import database.Database;
import database.ProjectGroup;

/**
 * Det h\u00E4r \u00E4r superklassen f\u00F6r alla servlets i systemet. Den inkluderar 
 * grundl\u00E4ggande funktionalitet som beh\u00F6vs i de flesta servlets. 
 * 
 * Det h\u00E4r systemet kr\u00E4ver en databas. F\u00F6r anv\u00E4ndarnamn och l\u00F6senord, se database/Database.java.
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
	 * Kontrollerar om en anv\u00E4ndare \u00E4r inloggad eller inte.
	 * 
	 * @param request
	 * @return true om anv\u00E4ndaren \u00E4r inloggad, annars false.
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
	 * Kan anv\u00E4ndas f\u00F6r att skapa form-element.
	 * 
	 * @param par
	 * @return "par"
	 */
	protected String formElement(String par) {
		return '"' + par + '"';
	}

	/**
	 * Skapar headern f\u00F6r alla servlets
	 * 
	 * @return String med html-kod f\u00F6r headern.
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
	
	/**
	 * Skapar avslutningen f\u00F6r alla servlets
	 * 
	 * @return String med hmtl-kod f\u00F6r avslutningen
	 */
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
	 * Genererar huvudmenyn baserat av den nuvarande sidan
	 * och r\u00E4ttigheterna f\u00F6r anv\u00E4ndaren
	 * @param userPermission
	 * @return String med html-kod f\u00F6r huvudmenyn
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
