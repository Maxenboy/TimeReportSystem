package gui;
import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.User;

import base.ServletBase;


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
@WebServlet("")
public class WelcomePage extends ServletBase {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WelcomePage() {
        super();
    }
    
	/**
	 * Implementation of all input to the servlet. All post-messages are forwarded to this method. 
	 * 
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (loggedIn(request)) {
			// Get the session
			HttpSession session = request.getSession();
			PrintWriter out = response.getWriter();

			out.print(getPageIntro());
			int userPermission = User.ROLE_NO_ROLE;
			if(session.getAttribute("user_permissions") != null) {
				userPermission = (Integer) session.getAttribute("user_permissions");			
			}
			out.append(generateMainMenu(userPermission, request));
			out.println("<div class='container'><div class='row'>");
			out.println("<div class='col-lg-12'><h1>V\u00E4lkommen</h1><p>TidRapporteringsSystem av grupp 2 i Programvaruutveckling i stora system 2013</div>");
			out.println("</div></div>");
				out.println(getPageOutro());
		} else {
			response.sendRedirect("LogIn");
		}
	}
}