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

	private static final long serialVersionUID = -3023954127569399790L;
	private Users users;

	public ActiveStatusForUser() {
		users = new Users();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			int permission = (Integer) session.getAttribute("user_permissions");
			out.print(getPageIntro());
			out.print(generateMainMenu(permission, request));
			out.print(generateSubMenu(permission));
			
			
			if (request.getParameter("username") == null) {
				out.print(users.showUsers(db.getUsers()));
				out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"\u00C4r du s\u00F6ker?\");if (confirmed) {$(this).submit();}});</script>");
			} else {
				User user = db.getUser(Integer.parseInt(request
						.getParameter("username")));
				if(user.isActive()){
					db.deactivateUser(user.getId());
					out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"\u00C4r du s\u00F6ker?\");if (confirmed) {$(this).submit();}});</script>");
					out.print(users.showUsers(db.getUsers()));
				}else{
					db.activateUser(user.getId());
					out.print("<script>$(alert(\"Inkorrekt syntax p\u00E5 anv\u00E4ndarnamnet\"))</script>");
					out.print(users.showUsers(db.getUsers()));
				}
			}
			out.print(getPageOutro());
		} else {
			response.sendRedirect("LogIn");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
	}



}
