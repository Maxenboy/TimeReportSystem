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
 * Servlet implementering av LogIn
 * 
 * En login sida.
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
     * Genererar login-formul\u00E4ret. 
     * @return HTML-kod f\u00F6r fomul\u00E4ret. 
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
    
	/**
	 * Loggar ut anv\u00E4ndaren om anv\u00E4ndaren om anv\u00E4ndaren inte \u00E4r inloggad. Kollar sedan om anv\u00E4ndarnamn
	 *  och l\u00F6senord \u00E4r korrekt. \u00E4r det korrekt s\u00E5 sparas information om anv\u00E4ndaren i sessioner och 
	 *  anv\u00E4ndaren skickas vidare till v\u00E4lkomstsidan. \u00E4r anv\u00E4ndaren inaktiv s\u00E5 presenteras ett meddelande om detta.
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
       			session.setAttribute("project_group_id", u.getProjectGroupId());
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
	 * Alla f\u00F6rfr\u00E5gningar skickas vidare till doGet
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
	}

}
