package subusersmenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Database;

@WebServlet("/ActiveStatusForUser")
public class ActiveStatusForUser extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3023954127569399790L;
	private Users users;
	private Database db;

	public ActiveStatusForUser() {
		users = new Users();
		db=new Database();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		String s = groupForm();
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
						+ "<script>$(alert(\"incorrect syntax on the user name\"))</script>" + s);
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession(true);
		String username = request.getParameter("username");
		String activate = request.getParameter("active");
		if (users.checkNewName(username)) {
			if (activate.equals("true")) {
				users.activateUser(username);
			}else{
				users.deactivateUser(username);
			}
			response.sendRedirect(request.getRequestURI() + "success=true");
		}else{
			response.sendRedirect(request.getRequestURI() + "success=false");
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

	public String groupForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("POST");
		html += "<p> Username : <input type=" + formElement("text") + " name="
				+ formElement("username") + '>';
		html += "<p> Activate : <input type=" + formElement("text")
				+ " active=" + formElement("active") + '>';
		html += "<input type=" + formElement("submit") + '>';
		html += "</form>";
		return html;

	}

	private String formElement(String par) {
		return '"' + par + '"';
	}
}
