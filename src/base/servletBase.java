package base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
 * @author Martin Host
 * @version 1.0
 * 
 */
public class servletBase extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// Define states
	protected static final int LOGIN_FALSE = 0;
	protected static final int LOGIN_TRUE = 1;
	protected Connection conn = null;

	
	// User permissions
	protected static final int PERMISSION_ADMIN = 			1;
	protected static final int PERMISSION_PROJ_LEADER = 	2; 
	protected static final int PERMISSION_WITHOUT_ROLE = 	3; 
	protected static final int PERMISSION_OTHER_USERS = 	4; 
	
	
	/**
	 * Constructs a servlet and makes a connection to the database. It also
	 * writes all user names on the console for test purpose.
	 */
	public servletBase() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager
//					.getConnection("jdbc:mysql://vm26.cs.lth.se/puss1302?"
//							+ "user=puss1302&password=jks78ww2");
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/puss1302?user=puss1302&password=jks78ww2");
			

			// Display the contents of the database in the console.
			// This should be removed in the final version
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from users");
			while (rs.next()) {
				String name = rs.getString("username");
				System.out.println("base " + name);
			}

			stmt.close();

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
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
		Object objectState = session.getAttribute("state");
		int state = LOGIN_FALSE;
		if (objectState != null)
			state = (Integer) objectState;
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
				+ "<title> The Base Block System </title>"
				+ "<link href=\"bootstrap.min.css\" type=\"text/css\" rel=\"stylesheet\">"
				+ "</head>"
				+ "<body>";
		return intro;
	}
	
	protected String getPageOutro() {
		String html = ""
				+ "</section>"
				+ "</div>"
				+ "</div>"
				+ "</section>"
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
	protected String generateMainMenu(int userPermission) {
		String intro = ""
				+ "<section class=\"main-menu container\">"
				+ "<div class=\"row\">"
				+ "<div class=\"col-lg-8\">"
				+ "<ul class=\"nav nav-pills\">"; 
		
		switch(userPermission) {
			case PERMISSION_ADMIN:
				intro += ""
						+ "<li><a href=\"ShowProjectGroups\">Projektgrupper</a></li>"
						+ "<li><a href=\"ShowUsers\">Användare</a></li>"
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
				
			default:
				// inget händer lol
				break; 
		}
		
		
		String closure = ""
				+ "</ul>"
				+ "</div>"; 
		
		String userInfo = ""
				+ "<div class=\"col-lg-4\">"
				+ "<div class=\"row\">"
				+ "<div class=\"col-lg-8\">"; 
		
		userInfo += ""
				+ "<p>User: USERNAME</p>"
				+ "<p>Group: GROUP"
				+ "<p>Role: ROLE</p>"; 
		
		userInfo += ""
				+ "</div>"
				+ "<div class=\"col-lg-4\">"
				+ "<a href=\"#\" class=\"btn btn-danger btn-block\">Log out</a>"
				+ "</div>"
				+ "</div>"
				+ "</div>"; 
 
		
		String outro = ""
				+ "</div>"
				+ "</section>"; 
		
		return intro + closure + userInfo + outro; 
	}

}
