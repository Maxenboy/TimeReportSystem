package subusersmenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import database.*;

@WebServlet("/HandleAdminRights")
public class HandleAdminRights extends HttpServlet {

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
		if (request.getParameter("session") == null) {
			out.print(getPageIntro() + u.showUsers(db.getUsers()));
		} else if (request.getParameter("session").equals("Success")) {
			u.makeAdministrator(request.getParameter("userName"));
			out.print(getPageIntro() + u.showUsers(db.getUsers())
					+ "<script>$(alert(\"Användaren är nu administratör. Ändringar sparade.\"))</script>");
		} else if (request.getParameter("session").equals("SuccessRemove")) {
			u.unmakeAdministrator(request.getParameter("userName"));
			out.print(getPageIntro() + u.showUsers(db.getUsers())
					+ "<script>$(alert(\"Användare är ej längre administratör. Ändringar sparade.\"))</script>");
		} else {
			out.print(getPageIntro() + u.showUsers(db.getUsers())
					+ "<script>$(alert(\"Inte möjligt! Användare är med i en projektgrupp! \"))</script>");
		}
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

	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> The Base Block System </title>" + getPageJs()
				+ "</head>" + "<body>";
		return intro;
	}

	private String getPageJs() {
		return "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>";
	}
}
