package subprojectgroupsmenu;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;

import database.*;

public class RemoveMemberFromProjectGroup {

	ProjectGroups group = new ProjectGroups();
	Database db = new Database();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		if (request.getParameter("session") == null) {
			out.print(getPageIntro() + removeUserForm());
		} else if (request.getParameter("session").equals("sucess")) {
			out.print(getPageIntro()
					+ group.showProjectGroup(db.getUsers(Integer
							.parseInt(request.getParameter("groupid")))));
		} else {
			out.print(getPageIntro()
					+ "$(alert(\"Couldn't add project group.\"))"
					+ removeUserForm());
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String userName = request.getParameter("name");
		String groupId = request.getParameter("groupid");
		if(group.removeUserFromProjectGroup(userName, Integer.parseInt(groupId))) {
			response.sendRedirect(request.getRequestURI() + "session=sucess");
		} else {
			response.sendRedirect(request.getRequestURI() + "session=false&groupid=" +groupId);
		}
	}

	private String removeUserForm() {
		String html;
		html = "<p> <form name=" + formElement("input") + "id="
				+ formElement("removeuserfromgroup") + '>';
		html += " method=" + formElement("get");
		html += "<p> User name: <input type=" + formElement("text") + " name="
				+ formElement("name") + '>';
		html += "<p> Group id: <input type=" + formElement("text")
				+ " groupid=" + formElement("groupid") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("Remove user") + '>';
		html += "</form>" + "</body></html>";
		return html;
	}

	private String formElement(String par) {
		return '"' + par + '"';
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
