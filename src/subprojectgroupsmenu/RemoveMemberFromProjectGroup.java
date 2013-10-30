package subprojectgroupsmenu;

import java.io.*;
import java.util.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import database.*;

@WebServlet("/RemoveMemberFromProjectGroup")
public class RemoveMemberFromProjectGroup extends gui.ProjectGroupsMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4921473121575805469L;
	ProjectGroups group = new ProjectGroups();
	Database db = new Database();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		out.print(generateMainMenu((Integer) session.getAttribute("user_permissions")));
		out.print(generateSubMenu((Integer) session.getAttribute("user_permissions")));
		if (request.getParameter("session") == null) {
			out.print(removeUserForm());
		} else if (request.getParameter("session").equals("sucess")) {
			out.print(group.showProjectGroup(db.getUsers(Integer
					.parseInt(request.getParameter("groupid")))));
		} else {
			out.print("<script>$(alert(\"Couldn't add project group.\"))</script>"
					+ removeUserForm());
		}
		out.print(getPageOutro());

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		String userName = request.getParameter("name");
		String groupId = request.getParameter("groupid");
		if (group.removeUserFromProjectGroup(userName,
				Integer.parseInt(groupId))) {
			response.sendRedirect(request.getRequestURI() + "session=sucess");
		} else {
			response.sendRedirect(request.getRequestURI()
					+ "session=false&groupid=" + groupId);
		}
	}

	private String removeUserForm() {
		String html;
		html = "<p> <form name=" + formElement("input") + "id="
				+ formElement("removeuserfromgroup");
		html += " method=" + formElement("get");
		html += "<p> User name: <input type=" + formElement("text") + " name="
				+ formElement("name") + '>';
		html += "<p> Group id: <input type=" + formElement("text")
				+ " groupid=" + formElement("groupid") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("Remove user") + '>';
		html += "</form>";
		return html;
	}

	
}
