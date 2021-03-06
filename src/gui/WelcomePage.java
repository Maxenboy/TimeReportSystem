package gui;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import base.ServletBase;
import database.User;


@WebServlet("/Welcome")
public class WelcomePage extends ServletBase {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WelcomePage() {
        super();
    }
    
	/**
	 * Implementering av all input till servleten. Alla post-meddelanden skickas vidare till den h\u00E4r metoden.
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
			out.println("<div class='col-lg-12'><h1>V\u00E4lkommen</h1><p>TidRapporteringsSystem av grupp 2 i Programvaruutveckling f\u00F6r stora system 2013</div>");
			if(((int) session.getAttribute("project_group_id") == 0) && (userPermission != User.ROLE_ADMIN)) {
				out.println("<div class='col-lg-12'><p style='color: red;'>Du \u00E4r inte indelad i n\u00E5gon projektgrupp i TidRapporteringsSystemet. Kontakta administrat\u00F6r.</p></div>");
			}
			out.println("</div></div>");
			out.println(getPageOutro());
		} else {
			response.sendRedirect("LogIn");
		}
	}
}