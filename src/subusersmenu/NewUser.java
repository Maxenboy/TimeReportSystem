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

	public NewUser() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			int permission = (Integer) session.getAttribute("user_permissions");
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
					out.print("<script>$(alert(\"Lyckades! Anv\u00E4ndarnamn: " + name + " L\u00F6senord: " + pass +"\"))</script>");
					out.print(users.showUsers(db.getUsers()));
				} else if (request.getParameter("success").equals("false")) {
					out.print("<script>$(alert(\"Inkorrekt syntax p\u00E5 anv\u00E4ndarnamnet\"))</script>"
							+ s);
				}
				
			}
			out.print(getPageOutro());
		} else {
			response.sendRedirect("LogIn");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
//			HttpSession session = request.getSession(true);
			String username = request.getParameter("username");
			if (users.checkNewName(username)) {
				users.addUser(username);
				response.sendRedirect(request.getRequestURI() + "?success=true&user="+username);
			} else {
				response.sendRedirect(request.getRequestURI() + "?success=false");
			}
		} else {
			response.sendRedirect("LogIn");
		}

	}

}
