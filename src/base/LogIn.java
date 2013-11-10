package base;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import subTimeReportMenu.ChangeTimeReport;
import subTimeReportMenu.NewTimeReport;
import subTimeReportMenu.RemoveTimeReport;
import subTimeReportMenu.ShowTimeReports;
import subTimeReportMenu.SignTimeReports;
import database.User;

/**
 * Servlet implementation class LogIn
 * 
 * A log-in page. 
 * 
 * The first thing that happens is that the user is logged out if he/she is logged in. 
 * Then the user is asked for name and password. 
 * If the user is logged in he/she is directed to the functionality page. 
 * 
 */
@WebServlet("/LogIn")
public class LogIn extends ServletBase {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogIn() {
        super();
    }
    
    /**
     * Generates a form for login. 
     * @return HTML code for the form
     */
    protected String loginRequestForm() {
    	String html = "<p>Ange anv\u00E4ndarnamn och l\u00F6senord:</p>";
    	html += "<p> <form name=" + formElement("input");
    	html += " method=" + formElement("post");
    	html += "<p> Anv\u00E4ndarnamn: <input type=" + formElement("text") + " name=" + formElement("user") + '>'; 
    	html += "<p> L\u00F6senord: <input type=" + formElement("password") + " name=" + formElement("password") + '>';  
    	html += "<p> <input type=" + formElement("submit") + "value=" + formElement("Logga In") + '>';
    	return html;
    }
    
//    /**
//     * Checks with the database if the user should be accepted
//     * @param name The name of the user
//     * @param password The password of the user
//     * @return true if the user should be accepted
//     */
//    private boolean checkUser(String name, String password) {
//		
//		boolean userOk = false;
//		boolean userChecked = false;
//		
//		try{
//			
//			Statement stmt = conn.createStatement();		    
//		    ResultSet rs = stmt.executeQuery("select * from users"); 
//		    while (rs.next( ) && !userChecked) {
//		    	String nameSaved = rs.getString("username"); 
//		    	String passwordSaved = rs.getString("password");
//		    	if (name.equals(nameSaved)) {
//		    		userChecked = true;
//		    		userOk = password.equals(passwordSaved);
//		    	}
//		    }
//		    stmt.close();
//		} catch (SQLException ex) {
//		    System.out.println("SQLException: " + ex.getMessage());
//		    System.out.println("SQLState: " + ex.getSQLState());
//		    System.out.println("VendorError: " + ex.getErrorCode());
//		}
//		return userOk;
//	}

    
    
	/**
	 * Implementation of all input to the servlet. All post-messages are forwarded to this method. 
	 * 
	 * First logout the user, then check if he/she has provided a username and a password. 
	 * If he/she has, it is checked with the database and if it matches then the session state is 
	 * changed to login, the username that is saved in the session is updated, and the user is 
	 * relocated to the functionality page. 
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get the session
		HttpSession session = request.getSession(true);
		
		int state;

		PrintWriter out = response.getWriter();
		out.println(getPageIntro());
		
		// Log out if logged in
		if (loggedIn(request)) {
			session.invalidate();
			out.println("<p style='color: red;'>Du \u00E4r nu utloggad</p>");
		}
		
		String name;
		String password;
				
        name = request.getParameter("user"); // get the string that the user entered in the form
        password = request.getParameter("password"); // get the entered password
//        if (name != null && password != null) {
        
        User u = db.loginUser(name, password);
        if(u != null) {
        	if(u.isActive()) {
//        	if (checkUser(name, password)) {
        		state = LOGIN_TRUE;
       			session.setAttribute("state", state);  // save the state in the session
       			session.setAttribute("name", name);  // save the name in the session
       			session.setAttribute("id", u.getId());
       			session.setAttribute("role", u.getRole());
       			session.setAttribute("project_group_id", u.getProjectGroup());
       			session.setAttribute("active", u.isActive());
       			session.setAttribute("signState", SignTimeReports.FIRST);
       			session.setAttribute("newReportState", NewTimeReport.FIRST);
       			session.setAttribute("changeReportState", ChangeTimeReport.FIRST);
       			session.setAttribute("removeReportState",RemoveTimeReport.FIRST);
       			session.setAttribute("showReportState", ShowTimeReports.FIRST);
       			if(u.getRole() == User.ROLE_ADMIN) {
       				session.setAttribute("user_permissions", PERMISSION_ADMIN);
       			} else if (u.getRole() == User.ROLE_PROJECT_LEADER) {
       				session.setAttribute("user_permissions", PERMISSION_PROJ_LEADER);
       			} else if(u.getRole() == User.ROLE_NO_ROLE) {
       				session.setAttribute("user_permissions", PERMISSION_WITHOUT_ROLE);
       			} else {
       				session.setAttribute("user_permissions", PERMISSION_OTHER_USERS);
       			}
       			response.sendRedirect("");
        	} else {
        		out.println("<p style='color: red;'>Du \u00E4r inte aktiverad i TidRapporteringsSystemet. Kontakta administrat\u00F6r.</p>");
        		out.println(loginRequestForm());
        	}
//       		}
//       		else {
//       			out.println("<p>That was not a valid user name / password. </p>");
//       			out.println(loginRequestForm());
//       		}
       	} else if(name == null || password == null || name.length() == 0 || password.length() == 0) {
    		out.println(loginRequestForm());
       	} else { // name was null, probably because no form has been filled out yet. Display form.
//       		out.println("<p>That was not a valid user name / password. </p>");
       		out.println("<p style='color: red;'>Inloggning misslyckades</p>");
       		out.println(loginRequestForm());
       	}
		
		out.println("</body></html>");
	}

	/**
	 * All requests are forwarded to the doGet method. 
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
	}

}
