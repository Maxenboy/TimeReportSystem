package subusersmenu;

import gui.UsersMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Database;

@WebServlet("/ActiveStatusForUser")
public class ActiveStatusForUser extends UsersMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3023954127569399790L;
	private Users users;
	private Database db;

	public ActiveStatusForUser() {
		users = new Users();
		db = new Database();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());

		// ï¿½ï¿½NDRA TILL Rï¿½ï¿½TT ROLL I generateMainMenu (se servletBase.java fï¿½ï¿½r
		// roller)
		out.print(generateMainMenu((int) session
				.getAttribute("user_permissions")));
		out.print(generateSubMenu((int) session
				.getAttribute("user_permissions")));

		String s = groupForm();
		if (s == null) {
			out.print("<p> Inget att visa </p>");
		} else {
			if (request.getParameter("success") == null) {
				out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"Är du säker?\");if (confirmed) {$(this).submit();}});</script>");
				out.print(s);
			} else if (request.getParameter("success").equals("true")) {
				out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"Är du säker?\");if (confirmed) {$(this).submit();}});</script>");
				out.print(users.showUsers(db.getUsers()));
			} else if (request.getParameter("success").equals("false")) {
				out.print("<script>$(alert(\"Inkorrekt syntax på anändarnamnet\"))</script>"
						+ s);
			} 
		}
		out.print(getPageOutro());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession(true);
		String username = request.getParameter("username");
		String activate = request.getParameter("active");
		if (users.checkNewName(username)) {
			if (activate.equals("true")) {
				users.activateUser(username);
			} else {
				users.deactivateUser(username);
			}
			response.sendRedirect(request.getRequestURI() + "?success=true");
		} else {
			response.sendRedirect(request.getRequestURI() + "?success=false");
		}

	}

	public String groupForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("POST");
		html += "<p> Användarnamn : <input type=" + formElement("text") + " name="
				+ formElement("username") + '>';
		html += "<p> Aktivera : <input type=" + formElement("text")
				+ " active=" + formElement("active (true/false)") + '>';
		html += "<input type=" + formElement("submit") + '>';
		html += "</form>";
		return html;

	}

}
