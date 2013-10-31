package subusersmenu;

import gui.UsersMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.User;

@WebServlet("/HandleAdminRights")
public class HandleAdminRights extends UsersMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1859483135166153038L;
	Users u = new Users();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());

		out.print(generateMainMenu((int) session
				.getAttribute("user_permissions")));
		out.print(generateSubMenu((int) session
				.getAttribute("user_permissions")));

		if (request.getParameter("username") == null) {
			out.print(u.showUsers(db.getUsers()));
		} else {
			User user = db.getUser(Integer.parseInt(request
					.getParameter("username")));
			if (user.getProjectGroup() == 0) {
				if (user.getRole() == user.ROLE_ADMIN) {
					System.out.println("user = admin");
					u.unmakeAdministrator(user.getUsername());
					out.print(u.showUsers(db.getUsers())
							+ "<script>$(alert(\"Användare är ej längre administratör. ändringar sparade.\"))</script>");
				} else {
					u.makeAdministrator(user.getUsername());
					out.print(u.showUsers(db.getUsers())
							+ "<script>$(alert(\"Användaren är nu administratör. ändringar sparade.\"))</script>");
				}
			} else {
				out.print(u.showUsers(db.getUsers())+ "<script>$(alert(\"Inte möjligt! Användare är med i en projektgrupp! \"))</script>");
			}
		}
		out.print(getPageOutro());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

	}


}
