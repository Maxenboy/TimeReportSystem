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

	private static final long serialVersionUID = 1859483135166153038L;
	/**
	 * doGet-metoden anropas alla g\u00E5nger d\u00E5 en adminstrat\u00F6r vill ge/ta bort rollen administrat\u00F6r f\u00F6r an\u00E4ndare i systemet
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession();
			int permission = (Integer) session.getAttribute("user_permissions");
			if (permission == PERMISSION_ADMIN) {
				PrintWriter out = response.getWriter();
				out.print(getPageIntro());
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				
				if (request.getParameter("username") == null) {
					out.print(users.showUsers(db.getUsers()));
				} else {
					User user = db.getUser(Integer.parseInt(request
							.getParameter("username")));
					if (user.getProjectGroupId() == 0) {
						if (user.getId() != (Integer) session.getAttribute("id")) {
							if (user.getRole() == User.ROLE_ADMIN) {
								users.unmakeAdministrator(user.getUsername());
								out.print(users.showUsers(db.getUsers())
										+ "<script>$(alert(\"Anv\u00E4ndare \u00E4r ej l\u00E4ngre administrat\u00F6r. \u00C4ndringar sparade.\"))</script>");
							} else {
								users.makeAdministrator(user.getUsername());
								out.print(users.showUsers(db.getUsers())
										+ "<script>$(alert(\"Anv\u00E4ndaren \u00E4r nu administrat\u00F6r. \u00C4ndringar sparade.\"))</script>");
							}
						} else {
							out.print(users.showUsers(db.getUsers())
									+ "<script>$(alert(\"Inte m\u00F6jligt! Anv\u00E4ndare \u00E4r med i en projektgrupp! \"))</script>");
						}
					} else {
						out.print(users.showUsers(db.getUsers())
								+ "<script>$(alert(\"Admin kan inte ta bort sina egna r\u00E4ttigheter! \"))</script>");
					}
				}
				out.print(getPageOutro());
			} else  {
				response.sendRedirect("");
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

	}

}
