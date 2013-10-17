package subusersmenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import database.Database;

public class NewUser {
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
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession(true);
		String username = request.getParameter("username");
		if (users.checkNewName(username)) {
			users.addUser(username);
			response.sendRedirect(request.getRequestURI() + "success=true");
		} else {
			response.sendRedirect(request.getRequestURI() + "success=true");
		}

	}

	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> Add member to project group </title>"
				+ getPageJs() + "</head>" + "<body>";
		return intro;
	}

	private String getPageJs() {
		return "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>"
				+ "$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"Are you sure?\");if (confirmed) {$(this).submit();}});";
	}

}
