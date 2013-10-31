package subusersmenu;

import gui.UsersMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.User;


@WebServlet("/ActiveStatusForUser")
public class ActiveStatusForUser extends UsersMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3023954127569399790L;
	private Users users;

	public ActiveStatusForUser() {
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

		
		if (request.getParameter("username") == null) {
			out.print(users.showUsers(db.getUsers()));
			out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"Är du säker?\");if (confirmed) {$(this).submit();}});</script>");
		} else {
			User user = db.getUser(Integer.parseInt(request
					.getParameter("username")));
			if(user.isActive()){
				db.deactivateUser(user.getId());
				out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"Är du säker?\");if (confirmed) {$(this).submit();}});</script>");
				out.print(users.showUsers(db.getUsers()));
			}else{
				db.activateUser(user.getId());
				out.print("<script>$(alert(\"Inkorrekt syntax p� an�ndarnamnet\"))</script>");
				out.print(users.showUsers(db.getUsers()));
			}
		}
		out.print(getPageOutro());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		

	}



}
