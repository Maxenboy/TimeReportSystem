package subusersmenu;

import gui.UsersMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/NewUser")
public class NewUser extends UsersMenu {
	private static final long serialVersionUID = 3336226905706349638L;

	/**
	 * Konstruktor
	 */
	public NewUser() {
		super();
	}

	/**
	 * doGet-metoden anropas alla g\u00E5nger d\u00E5 en adminstrat\u00F6r vill l\u00E4gga till en anv\u00E4ndare i systemet.
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			int permission = (Integer) session.getAttribute("user_permissions");
			if (permission == PERMISSION_ADMIN) {
				PrintWriter out = response.getWriter();
				out.print(getPageIntro());
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				String s = users.userForm();
				out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"\u00C4r du s\u00F6ker?\");if (confirmed) {$(this).submit();}});</script>");
				if (s == null) {
					out.print("<p> Inget att visa </p>");
				} else {
					if (request.getParameter("success") == null) {
						out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"\u00C4r du s\u00F6ker?\");if (confirmed) {$(this).submit();}});</script>");
						out.print(s);
					} else if (request.getParameter("success").equals("true")) {
						out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"\u00C4r du s\u00F6ker?\");if (confirmed) {$(this).submit();}});</script>");
						String name = request.getParameter("user");
						String pass = db.getUser(name).getPassword();
						out.print("<script>$(alert(\"Lyckades! Anv\u00E4ndarnamn: "
								+ name + " L\u00F6senord: " + pass
								+ "\"))</script>");
						out.print(users.showUsers(db.getUsers()));
					} else if (request.getParameter("success").equals("false")) {
						out.print("<script>$(alert(\"Ogiltigt anv\u00E4ndarnamn.\"))</script>"
								+ s);
					} else {
						out.print("<script>$(alert(\"En anv\u00E4ndare med detta namn existerar redan.\"))</script>"
								+ s);
					}
					
				}
				out.print(getPageOutro());
			} else {
				response.sendRedirect("");
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}
	/**
	 * doPost-metoden anropas alla g\u00E5nger d\u00E5 en adminstrat\u00F6r vill l\u00E4gga till en anv\u00E4ndare i systemet och validerar anv\u00E4ndarens angivna namn	 * 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (loggedIn(request)) {
			// HttpSession session = request.getSession(true);
			String username = request.getParameter("username");
			switch (users.checkNewName(username)) {
			case 1:
				response.sendRedirect(request.getRequestURI()
						+ "?success=false");
				break;
			case 2:
				response.sendRedirect(request.getRequestURI()
						+ "?success=exist");
				break;
			case 3:
				users.addUser(username);
				response.sendRedirect(request.getRequestURI()
						+ "?success=true&user=" + username);
				break;
			}

		} else {
			response.sendRedirect("LogIn");
		}

	}

}
