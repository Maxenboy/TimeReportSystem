package subProjectMembersMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import database.Database;

public class HandleProjectRoles {

	Database db = new Database();
	ProjectMembers members;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		if (request.getParameter("session") == null) {
			out.print(getPageIntro() + groupForm());
		} else if (request.getParameter("session").equals("found")) {
			members = new ProjectMembers(request.getParameter("name"));
			out.print(getPageIntro()
					+ members.showMembers(db.getUsers(Integer.parseInt(request
							.getParameter("groupname")))));
		} else if (request.getParameter("session").equals("failed")) {
			out.print(getPageIntro() + "$(alert(\"Incorrect group id\"))" + groupForm());
		} else {
			out.print(getPageIntro() + "$(alert(\"Error\"))" + groupForm());
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String stage = request.getParameter("session");
		if(stage == null) {
			String groupName = request.getParameter("name");
			if(db.getProjectGroup(Integer.parseInt(groupName)) != null) {
				response.sendRedirect(request.getRequestURI() + "session=found&groupname="+groupName);
			} else {
				response.sendRedirect(request.getRequestURI() + "session=failed");
			}
		} else if (stage.equals("found")) {
			//Continue here!
		}
		
	}

	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> The Base Block System </title>" + getPageJs()
				+ "</head>" + "<body>";
		return intro;
	}

	private String groupForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("POST");
		html += "<p> Username : <input type=" + formElement("text") + " name="
				+ formElement("groupname") + '>';
		html += "<input type=" + formElement("submit") + '>';
		html += "</form>";
		return html;
	}

	private String formElement(String par) {
		return '"' + par + '"';
	}

	private String getPageJs() {
		return "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>";
	}
}
