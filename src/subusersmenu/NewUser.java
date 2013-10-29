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
	private Database db;

	public NewUser() {
		users = new Users();
		db = new Database();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		// ÄNDRA TILL RÄTT ROLL I generateMainMenu (se servletBase.java för roller) 
				out.print(generateMainMenu(1));
				out.print(generateSubMenu(1));
		String s = users.userForm();
		if (s == null) {
			out.print("<p> Nothing to show </p>");
		} else {
			switch (request.getParameter("success")) {
			case "null":
				out.print(s);
				break;
			case "true":
				out.print(users.showUsers(db.getUsers()));
				break;
			case "false":
				out.print(getPageIntro()
						+ "$(alert(\"incorrect syntax on the user name\"))" + s);
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
			response.sendRedirect(request.getRequestURI() + "success=true");
		} else {
			response.sendRedirect(request.getRequestURI() + "success=false");
		}

	}

	private String getPageJs() {
		return "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>"
				+ "$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"Are you sure?\");if (confirmed) {$(this).submit();}});";
	}

}
