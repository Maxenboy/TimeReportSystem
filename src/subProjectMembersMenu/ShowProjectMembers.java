package subProjectMembersMenu;

import gui.ProjectGroupsMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/ShowProjectMembers")
public class ShowProjectMembers extends ProjectGroupsMenu {

	private static final long serialVersionUID = -6458493959837089759L;
	private ProjectMembers pm;

	public ShowProjectMembers() {

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			out.print(getPageIntro());
			int userPermission = (Integer) session.getAttribute("user_permissions");
			out.append(generateMainMenu(userPermission, request));
			out.print(generateSubMenu(userPermission));
			
			String s = groupForm();
			if (s == null)
				out.print("<p> Inget att visa </p>");
			else if (request.getParameter("success") != null) {
				if (request.getParameter("success").equals("false")) {
					out.print("<p> Den valda gruppen finns ej </p>");
				} else {
					pm = new ProjectMembers(request.getParameter("groupname"));
					out.print(pm.showMembers(db.getUsers(Integer.valueOf(request
							.getParameter("groupname")))));
				}
			} else {
				out.print(s);
			}
			
			out.print(getPageOutro());
		} else {
			response.sendRedirect("LogIn");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			String groupname = request.getParameter("groupname");
			if (db.getProjectGroup(Integer.valueOf(groupname)) != null) {
				response.sendRedirect(request.getRequestURI() + "?success=true&groupname=" + groupname);
			} else {
				response.sendRedirect(request.getRequestURI() + "?success=false");
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}

	private String groupForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("POST");
		html += "<p> Anv\u00E4ndarnamn : <input type=" + formElement("text") + " name="
				+ formElement("groupname") + '>';
		html += "<input type=" + formElement("Spara") + '>';
		html += "</form>";
		return html;
	}
}
