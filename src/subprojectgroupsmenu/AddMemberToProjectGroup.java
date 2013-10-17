package subprojectgroupsmenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import database.Database;

public class AddMemberToProjectGroup {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ProjectGroups groups = new ProjectGroups();
		HttpSession session = request.getSession(true);
		Database db = new Database();
		PrintWriter out = response.getWriter();
		switch (request.getParameter("sucess")) {
		case "null":
			out.print(getPageIntro() + groups.addUserForm());
			break;
		case "true":
			out.print(getPageIntro()
					+ groups.showProjectGroup(db.getUsers(Integer
							.parseInt(request.getParameter("groupid")))));
			break;
		case "false":
			out.print(getPageIntro() + "$(alert(\"Incorrect input.\"))"
					+ groups.addUserForm());
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ProjectGroups groups = new ProjectGroups();
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		String user = request.getParameter("name");
		String groupid = request.getParameter("groupid");
		if (groups.addUserToProjectGroup(user, Integer.parseInt(groupid))) {
			response.sendRedirect(request.getRequestURI()
					+ "success=true&groupid=" + groupid);
		} else {
			response.sendRedirect(request.getRequestURI() + "sucess=false");
		}
	}

	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> Add member to project group </title>"
				+ getPageJs() + "</head>" + "<body>";
		return intro;
	}

	private String getPageJs() {
		return "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>";
	}

}
