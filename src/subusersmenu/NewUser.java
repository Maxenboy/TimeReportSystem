package subusersmenu;

import gui.UsersMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Database;

@WebServlet("/NewUser")
public class NewUser extends UsersMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3336226905706349638L;
	/**
	 * 
	 */
	private Users users;

	public NewUser() {
		users = new Users();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		out.print(generateMainMenu((int) session
				.getAttribute("user_permissions")));
		out.print(generateSubMenu((int) session
				.getAttribute("user_permissions")));
		String s = users.userForm();
		out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"�r du s�ker?\");if (confirmed) {$(this).submit();}});</script>");
		if (s == null) {
			out.print("<p> Inget att visa </p>");
		} else {
			if (request.getParameter("success") == null) {
				out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"�r du s�ker?\");if (confirmed) {$(this).submit();}});</script>");
				out.print(s);
			} else if (request.getParameter("success").equals("true")) {
				out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"�r du s�ker?\");if (confirmed) {$(this).submit();}});</script>");
				String name = request.getParameter("user");
				String pass = db.getUser(name).getPassword();
				out.print("<script>$(alert(\"Lyckades! Användarnamn: " + name + " Lösenord: " + pass +"\"))</script>");
				out.print(users.showUsers(db.getUsers()));
			} else if (request.getParameter("success").equals("false")) {
				out.print("<script>$(alert(\"Inkorrekt syntax p� anv�ndarnamnet\"))</script>"
						+ s);
			}

		}
		out.print(getPageOutro());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession(true);
		String username = request.getParameter("username");
		if (users.checkNewName(username)) {
			users.addUser(username);
			response.sendRedirect(request.getRequestURI() + "?success=true&user="+username);
		} else {
			response.sendRedirect(request.getRequestURI() + "?success=false");
		}

	}

}
