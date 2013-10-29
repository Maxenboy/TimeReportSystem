package subusersmenu;

import gui.UsersMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Database;
import database.User;

@WebServlet("/HandleAdminRights")
public class HandleAdminRights extends UsersMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1859483135166153038L;
	Users u = new Users();
	Database db = new Database();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());

		// ÄNDRA TILL RÄTT ROLL I generateMainMenu (se servletBase.java för
		// roller)
		out.print(generateMainMenu((int) session
				.getAttribute("user_permissions")));
		out.print(generateSubMenu((int) session
				.getAttribute("user_permissions")));

		if (request.getParameter("session") == null) {
			out.print(u.showUsers(db.getUsers()));
		} else if (request.getParameter("session").equals("Success")) {
			u.makeAdministrator(request.getParameter("userName"));
			out.print(u.showUsers(db.getUsers())
					+ "<script>$(alert(\"Användaren är nu administratör. Ändringar sparade.\"))</script>");
		} else if (request.getParameter("session").equals("SuccessRemove")) {
			u.unmakeAdministrator(request.getParameter("userName"));
			out.print(u.showUsers(db.getUsers())
					+ "<script>$(alert(\"Användare är ej längre administratör. Ändringar sparade.\"))</script>");
		} else {
			out.print(u.showUsers(db.getUsers())
					+ "<script>$(alert(\"Inte möjligt! Användare är med i en projektgrupp! \"))</script>");
		}
		out.print(getPageOutro());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		User u = db.getUser(Integer.parseInt(request.getParameter("name")));
		if (u.getProjectGroup() == 0) {
			if (u.getRole() == u.ROLE_ADMIN) {
				response.sendRedirect(request.getRequestURI()
						+ "session=SuccessRemove&userName=" + u.getUsername());
			} else {
				response.sendRedirect(request.getRequestURI()
						+ "session=Success&userName=" + u.getUsername());
			}
		} else {
			response.sendRedirect(request.getRequestURI() + "session=failure");
		}
	}

}
